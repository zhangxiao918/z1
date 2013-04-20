
package com.skymobi.cac.maopao.communication.service;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.skymobi.cac.maopao.communication.AccessPipelineState;
import com.skymobi.cac.maopao.communication.MsgLevel;
import com.skymobi.cac.maopao.communication.ServerConstantsInfo;
import com.skymobi.cac.maopao.communication.aidl.IMsgCallback;
import com.skymobi.cac.maopao.communication.aidl.IMsgService;
import com.skymobi.cac.maopao.communication.domains.TimeoutHandler;
import com.skymobi.cac.maopao.passport.android.util.ByteUtils;
import com.skymobi.cac.maopao.xip.AccessHeader;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class WanMsgService extends AbstractRemoteMsgService {

    private final static String TAG = "WanMsgService";

    public final static String CLASSNAME = "com.skymobi.cac.maopao.communication.service.WanMsgService";

    private final static String RECONNCET_TIMEOUT_KEY = "Reconnect_timeout_key";
    private int mRedirectTimes;
    private int mReconnectTimes;
    private int mAccessIndex;
    private int mAuthCode;

    private final static int RECONNECT_INTERVAL = 5;

    private IMsgService.Stub binder = new IMsgService.Stub() {

        private boolean checkAuthorised() throws RemoteException {
            PackageManager pm = getPackageManager();

            int match = pm.checkSignatures(getCallingUid(), getApplicationInfo().uid);
            if (match == PackageManager.SIGNATURE_MATCH) {
                return true;
            }
            /* Crash the calling application if it doesn't catch */
            Log.e(TAG, "signature is not matched!");
            throw new RemoteException();

        }

        @Override
        public void registerCallback(IMsgCallback cb)
                throws RemoteException {
            this.checkAuthorised();
            if (cb != null)
                msgCallbacks.register(cb);
        }

        @Override
        public void unregisterCallback(IMsgCallback cb)
                throws RemoteException {
            this.checkAuthorised();
            if (cb != null)
                msgCallbacks.unregister(cb);
        }

        @Override
        public void send(byte[] bytes, boolean encrypt, boolean seq) throws RemoteException {
            this.checkAuthorised();
            mThread.doSend(bytes, encrypt, seq);
        }

        @Override
        public boolean loginOk() throws RemoteException {
            this.checkAuthorised();
            return mThread.loginOk;
        }

        @Override
        public byte[] getEncryptKey() throws RemoteException {
            this.checkAuthorised();
            return mThread.mEncryptKey;
        }

    };

    @Override
    public IBinder onBind(Intent bundle) {
        init();
        Log.d(TAG, "init wan msg service on onBind");
        startThread();
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private void startThread() {
        if (!mThread.isAlive()) {
            mThread.start();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        init();
        startThread();
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        terminate();

    }

    private void init() {
        mThread.init();
    }

    @Override
    protected void startAuthentication() {
        this.sendUAInfoReq();
    }

    @Override
    protected void failOver() {
        if (mThread.failOverLock.getAndSet(true)) {
            Log.d(TAG, "fail over in progress");
            return;
        }
        if (!mThread.loginOk) {
            onServiceError("重连失败，请重新登录");
            return;
        }

        while (MAX_RECONNECT_TIMES > mReconnectTimes) {
            if (this.reconnect()) {
                mThread.state = AccessPipelineState.AUTHENTICATION;
                Log.d(TAG, "send reconnect req.");
                this.sendReconnectReq();
                mThread.failOverLock.set(false);
                return;
            } else {
                onMsg(MsgLevel.WARN, "重连失败，请重新登录");
                try {
                    Thread.sleep(RECONNECT_INTERVAL * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
        onServiceError("重连失败，请重新登录");
        return;
    }

    @Override
    protected void dataDispatch(ByteBuffer recvData) {
        Log.i(TAG, "dataDispatch is working ... ");
        recvData.position(0);
        char srcId = recvData.getChar();
        char dstID = recvData.getChar();// 获取通用消息头中的DstID值
        char length = recvData.getChar();
        char flag = recvData.getChar();// 获取通用消息头中的flag值

        recvData.position(BasisMessageConstants.PACKET_COMM_HEAD_LEN);

        int seqid = 0;
        // 设置msgAck
        // if ((flag & BasisMessageConstants.PACKET_TYPE_SEQ) != 0x0) {
        // seqid = recvData.getChar();
        //
        // if (seqid != (mThread.msgAck.get()+1)) {
        // Log.d(TAG,"msgAck:"+mThread.msgAck+"seqid:"+seqid);
        // mThread.msgSeq.set(seqid);
        // }
        // mThread.msgAck.set(seqid);
        //
        // char c = recvData.getChar();
        // }

        int msgCode = recvData.getChar();
        Log.d(TAG,
                "receive msg seq id:" + seqid + " msg code:" + msgCode + " hex:0x"
                        + Integer.toHexString(msgCode));

        synchronized (mThread.callBackMap) {
            mThread.callBackMap.remove(msgCode);
        }
        int remainData = recvData.remaining();
        byte[] bodyBytes = new byte[remainData];

        recvData.get(bodyBytes);
        Log.i(TAG, "bodyBytes : " + ByteUtils.bytesAsHexString(bodyBytes, 256));

        if (BasisMessageConstants.MSGCODE_MAIN_RECONNECT_RSP == msgCode) { // 重连消息
            this.recvReconnectResp(bodyBytes[0]);
        } else if (BasisMessageConstants.MSGCODE_ACCESS_TCP_UA_NEW_RESP == msgCode) { // UA认证响应
            this.recvUAInfoResp(bodyBytes);
        } else if (BasisMessageConstants.MSGCODE_MAIN_HEARTBEAT_RSP == msgCode) { // 心跳响应
            this.recvHeartbeatResp();
        } else {
            AccessHeader header = new AccessHeader();
            header.setDstModule(dstID);
            header.setSrcModule(srcId);
            header.setFlags((short) flag);
            header.setLength((short) length);
            header.setMsgCode(msgCode);
            onReceive(msgCode, header, bodyBytes);
        }
    }

    private void sendReconnectReq() {
        mThread.state = AccessPipelineState.AUTHENTICATION;
        ByteBuffer temp = ByteBuffer.allocate(10);
        temp.order(ByteOrder.BIG_ENDIAN);

        temp.putChar(BasisMessageConstants.MSGCODE_MAIN_RECONNECT_REQ);
        temp.putInt(this.mAccessIndex);
        temp.putInt(this.mAuthCode);

        int length = 8 + 10;
        ByteBuffer sendbuf = ByteBuffer.allocate(length + 7);
        sendbuf.order(ByteOrder.BIG_ENDIAN);
        sendbuf.put(ServerConstantsInfo.AUTH);// 握手字符串
        sendbuf.putChar(BasisMessageConstants.EXT_ID_BASE);
        sendbuf.putChar(BasisMessageConstants.SMOD_ID_ACCESS_TCP);
        sendbuf.putChar((char) length);
        sendbuf.putChar((char) BasisMessageConstants.PACKET_TYPE_ENCRYPT);// flag
        sendbuf.put(temp.array());
        TimeoutHandler.addTimeoutHandler(RECONNCET_TIMEOUT_KEY, new Runnable() {

            @Override
            public void run() {
                Log.d(TAG, "no reconncet resp from server after 5 seconds.");
                recvReconnectResp((byte) BasisMessageConstants.EC_FAILURE);
            }
        }, 5);
        mThread.doSend(sendbuf.array(), false, false);

    }

    private boolean recvReconnectResp(byte result) {
        TimeoutHandler.removeTimeoutHandler(RECONNCET_TIMEOUT_KEY);
        Log.d(TAG, "recv reconnect resp!");
        switch ((int) result) {
            case BasisMessageConstants.EC_SUCCESS: {
                mThread.state = AccessPipelineState.CONNECTED;
                mReconnectTimes = 0;
                onConnect();
                break;
            }
            case BasisMessageConstants.EC_FAILURE: {
                onMsg(MsgLevel.ERROR, "重连失败，请重新登录");
                terminate();
                break;
            }
        }
        return true;
    }

    private boolean sendUAInfoReq() {
        ByteBuffer sendbuf = ServerConstantsInfo.getUAByteBuffer(mThread.msgSeq,
                mThread.msgAck,
                mRedirectTimes);
        return mThread.doSend(sendbuf.array(), false, false);

    }

    private void redirect(String ipaddr, int ipport) {
        Log.d(TAG, "redirecting");

        mThread.msgSeq.set(0);
        mThread.msgAck.set(0);
        mThread.ip = ipaddr;
        mThread.port = ipport;

        mThread.releaseSocket();
        mThread.createSocket();
        if (mThread.connect(ipaddr, ipport)) {
            this.sendUAInfoReq();
        } else {
            mThread.releaseSocket();
            this.terminate();
        }
    }

    private boolean recvUAInfoResp(byte[] bytes) {
        ByteBuffer data = ByteBuffer.wrap(bytes);
        Log.d(TAG, "recv UA resp!");
        byte result = data.get();
        switch ((int) result) {
            case BasisMessageConstants.EC_SUCCESS: {
                mAccessIndex = data.getInt();
                mAuthCode = data.getInt();
                byte[] ownipAddr = new byte[4];
                data.get(ownipAddr);
                int ownipPort = (int) data.getChar();
                String ipaddr;
                try {
                    ipaddr = InetAddress.getByAddress(ownipAddr).getHostAddress();
                    Log.d(TAG, "own ip:" + ipaddr + "ownPort:" + ownipPort);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }

                mThread.mEncryptKey = new byte[4];
                data.get(mThread.mEncryptKey);

                mThread.startHb = true;
                mThread.loginOk = true;

                // 收到UA认证成功，通知调用者开始启动登陆。采用广播的方法。
                mThread.state = AccessPipelineState.CONNECTED;
                sendBroadcast(ServiceMessageTag.getNotifyActionLogin());

                break;
            }
            case BasisMessageConstants.EC_FAILURE: {
                onMsg(MsgLevel.ERROR, "连接失败");
                terminate();
                break;
            }
            case BasisMessageConstants.EC_REDIRECT: {
                byte[] ipAddr = new byte[4];
                for (int i = 0; i < 4; i++) {
                    ipAddr[i] = data.get();
                }
                int ipPort = (int) data.getChar();
                try {
                    String ipaddr = InetAddress.getByAddress(ipAddr).getHostAddress();
                    redirect(ipaddr, ipPort);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                mRedirectTimes++;
                break;
            }
        }
        return true;
    }

    private synchronized boolean reconnect() {
        Log.d(TAG, "reconnect");
        onMsg(MsgLevel.WARN, "正在重连");
        mReconnectTimes += 1;

        mThread.releaseSocket();
        mThread.createSocket();
        return mThread.connect(mThread.ip, mThread.port);
    }

}
