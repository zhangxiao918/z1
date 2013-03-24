
package com.skymobi.cac.maopao.communication.service;

import android.util.Log;

import com.skymobi.cac.maopao.communication.AccessPipelineState;
import com.skymobi.cac.maopao.communication.MsgLevel;
import com.skymobi.cac.maopao.communication.TestServerInfo;
import com.skymobi.cac.maopao.communication.domains.TimeoutHandler;
import com.skymobi.cac.maopao.passport.android.util.ByteUtils;
import com.skymobi.cac.maopao.utils.d;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.MessageCodecAgent;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PiplelineThread extends Thread {

    public static final String TAG = "PiplelineThread";

    private AtomicBoolean inited = new AtomicBoolean(false); // 用来保证只初始化一次

    private AtomicLong mLastHeartBeatTime = new AtomicLong(0);
    private AtomicInteger heartBeatSeq = new AtomicInteger(1);
    private static final int HEART_BEAT_MSG_INTERVAL = 17000;

    protected AtomicInteger msgSeq = new AtomicInteger(0); // 消息发送序列号
    protected AtomicInteger msgAck = new AtomicInteger(0); // 消息接收序列号
    protected byte[] mEncryptKey;

    protected AtomicBoolean failOverLock = new AtomicBoolean(false);

    protected boolean loginOk = false;
    protected boolean shutDown = false; // socket 关闭开关。设置为true，socket会停止接收数据，并关闭
    protected boolean startHb = false; // 如果不需要再UA成功登陆不成功的情况下发心跳，此标志位可以不要

    private AbstractRemoteMsgService service;
    private Socket mSocket = null;

    protected Map<Integer, Long> callBackMap = new ConcurrentHashMap<Integer, Long>();

    protected AtomicLong lastReceiveTime = new AtomicLong(); // 最后一条消息的接收时间
    private final static int CALL_BACK_CHECK_INTERVAL = 1;

    public AccessPipelineState state = AccessPipelineState.IDLE;

    public String ip;
    public int port;

    public PiplelineThread(AbstractRemoteMsgService service) {
        this.service = service;
    }

    @Override
    public void run() {
        while (true) {
            if (shutDown) {
                releaseSocket();
                shutDown = false;
                return;
            }

            switch (state) {
                case IDLE:
                    if (!loginOk) {
                        createSocket();
                        if (!connect(ip, port)) {
                            Log.i(TAG, "fail connet socket server");
                            releaseSocket();
                            service.terminate();
                            return;
                        } else {
                            Log.i(TAG, "success connet socket server");
                            service.startAuthentication();
                        }
                    } else {
                        service.failOver();
                    }

                    break;
                case CONNECTED:

                    ByteBuffer recvData = receive();
                    if (null != recvData) {
                        Log.i(TAG,
                                "CONNECTED recvData: "
                                        + ByteUtils.bytesAsHexString(recvData.array(), 256));
                        service.dataDispatch(recvData);
                    }

                    sendHeartbeatReq();

                    if (System.currentTimeMillis() - lastReceiveTime.get() > TestServerInfo.NODATA_RECEIVE_TIMEOUT) {
                        Log.e(TAG,
                                "no data receive from server for a long time. connection is  lose");
                        service.onServiceError("重连失败请重新登录");
                    }
                    break;
                case AUTHENTICATION:
                    recvData = receive();
                    if (null != recvData) {
                        Log.i(TAG,
                                "AUTHENTICATION recvData: "
                                        + ByteUtils.bytesAsHexString(recvData.array(), 256));
                        service.dataDispatch(recvData);
                    }
                    break;
            }

            checkTimeoutHandlers(); // 针对重连，登录Access等动作的超时检测

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void releaseSocket() {
        if (mSocket != null) {
            try {
                mSocket.shutdownInput();
                mSocket.shutdownOutput();
                mSocket.close();
                while (!mSocket.isClosed()) { // make sure the socket is really
                                              // closed
                    Thread.sleep(10); //
                }
            } catch (IOException e) {
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                mSocket = null;
            }
        }
    }

    public void createSocket() {

        releaseSocket();
        mSocket = new Socket();
        try {
            mSocket.setSoTimeout(TestServerInfo.SOTIMEOUT);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        this.state = AccessPipelineState.AUTHENTICATION;
    }

    public boolean connect(String ip, int port) {
        boolean ret = false;

        try {
            InetAddress address = InetAddress.getByName(ip);
            InetSocketAddress remoteAddr = new InetSocketAddress(address, port);
            mSocket.connect(remoteAddr, TestServerInfo.CONNECTTIMEOUT);
            mSocket.setKeepAlive(true);
            if (mSocket.isConnected()) {
                state = AccessPipelineState.AUTHENTICATION;
                ret = true;
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;

    }

    public ByteBuffer receive() {
        try {
            if (mSocket.isClosed()) {
                Log.d(TAG, "socket is closed, ignore receive action");
                return null;
            }
            InputStream is = mSocket.getInputStream();
            if (0 >= is.available()) {
                return null;
            }
            byte[] headbyte = new byte[BasisMessageConstants.PACKET_COMM_HEAD_LEN];
            int headlen = BasisMessageConstants.PACKET_COMM_HEAD_LEN;
            int offset = 0;
            int reads = 0;
            do {
                reads = is.read(headbyte, offset, headlen - offset);
                if (-1 != reads) {
                    offset += reads;
                }
            } while (offset != BasisMessageConstants.PACKET_COMM_HEAD_LEN);

            ByteBuffer headbuff = ByteBuffer.wrap(headbyte);
            headbuff.order(ByteOrder.BIG_ENDIAN);

            int length = (int) headbuff.getChar(4);// 获取通用消息头中的length值
            length -= BasisMessageConstants.PACKET_COMM_HEAD_LEN;
            byte[] bodybyte = new byte[length];
            offset = 0;
            reads = 0;
            do {
                reads = is.read(bodybyte, offset, length - offset);
                if (-1 != reads) {
                    offset += reads;
                }
            } while (offset != length);

            ByteBuffer buff = ByteBuffer.allocate(BasisMessageConstants.PACKET_COMM_HEAD_LEN
                    + length);

            buff.order(ByteOrder.BIG_ENDIAN);
            buff.put(headbyte);
            buff.put(bodybyte);

            this.lastReceiveTime.set(System.currentTimeMillis());

            return buff;
        } catch (IOException se) {
            se.printStackTrace();
            service.onMsg(MsgLevel.WARN, "连接失败");
            this.state = AccessPipelineState.IDLE;
            return null;
        }
    }

    public synchronized boolean doSend(byte[] sendData, boolean encrypt, boolean seq) {
        boolean ret = false;
        if (AccessPipelineState.CONNECTED != state &&
                state != AccessPipelineState.AUTHENTICATION) {
            Log.i(TAG, "msg can not be send because the connection is lost");
            service.onServiceError("重连失败，请重新登录");
            return ret;
        }

        try {
            byte[] reqBytes = sendData;
            if (mEncryptKey != null && encrypt && seq) {
                reqBytes = MessageCodecAgent.getInstance().encryptReqBytes(
                        sendData, msgSeq.incrementAndGet(), msgAck.get(), mEncryptKey);
            }

            Log.i(TAG, "reqBytes = " + ByteUtils.bytesAsHexString(reqBytes, 256));
            OutputStream os = mSocket.getOutputStream();
            os.write(reqBytes);
            os.flush();
            ret = true;
            Log.d(TAG, "current seq:" + msgSeq.get());
        } catch (IOException se) {
            Log.d(TAG, "send data ERROR:" + se.getMessage());
            se.printStackTrace();
            state = AccessPipelineState.IDLE;
            service.onMsg(MsgLevel.WARN, "连接失败");
        }
        return ret;
    }

    protected void sendHeartbeatReq() {
        if (!this.startHb || this.state != AccessPipelineState.CONNECTED) {
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        if ((currentTimeMillis - this.lastReceiveTime.get()) < HEART_BEAT_MSG_INTERVAL) {
            return;
        }
        if (currentTimeMillis - this.mLastHeartBeatTime.get() < HEART_BEAT_MSG_INTERVAL) {
            return;
        }
        this.mLastHeartBeatTime.set(currentTimeMillis);

        ByteBuffer sendbuf = TestServerInfo.sendHeartbeatReq();

        heartBeatSeq.incrementAndGet();
        Log.d(TAG, "sendHeartbeatReq to access");
        doSend(sendbuf.array(), false, false);
    }

    public void reset() {
        shutDown = false;
        msgSeq.set(0);
        msgAck.set(0);
        loginOk = false;
        startHb = false;
        TimeoutHandler.timeoutHandlerMap.clear();
    }

    protected void checkTimeoutHandlers() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - TimeoutHandler.lastTimeoutHandlerCheckTime < CALL_BACK_CHECK_INTERVAL * 1000) {
            return;
        }
        synchronized (TimeoutHandler.timeoutHandlerMap) {
            Set<String> timeOutHandlers = new HashSet<String>();
            for (String key : TimeoutHandler.timeoutHandlerMap.keySet()) {
                TimeoutHandler handler = TimeoutHandler.timeoutHandlerMap.get(key);
                if (currentTime - handler.mSendTimeMillions < handler.mTimeOut * 1000) {
                    continue;
                }
                timeOutHandlers.add(key);
            }
            for (String key : timeOutHandlers) {
                TimeoutHandler handler = TimeoutHandler.timeoutHandlerMap.remove(key);
                handler.mHandler.run();
            }
        }
    }

    /**
     * 初始化服务端基本信息。
     */
    public void init() {
        if (!inited.getAndSet(true)) {
            d.i(TestServerInfo.BYTES);
            ip = TestServerInfo.SERVER_ACCESS_IP;
            port = TestServerInfo.SERVER_ACCESS_PORT;
            Log.i(TAG, "access ip:" + ip + " , port:" + port);
        }
    }
}
