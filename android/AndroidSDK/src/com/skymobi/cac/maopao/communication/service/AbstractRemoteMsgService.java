
package com.skymobi.cac.maopao.communication.service;

import android.app.Service;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.skymobi.cac.maopao.communication.AccessPipelineState;
import com.skymobi.cac.maopao.communication.MsgLevel;
import com.skymobi.cac.maopao.communication.aidl.IMsgCallback;
import com.skymobi.cac.maopao.xip.AccessHeader;

import java.nio.ByteBuffer;

public abstract class AbstractRemoteMsgService extends Service {

    private static final String TAG = "AbstractRemoteMsgService";
    protected final static int MAX_RECONNECT_TIMES = 3;

    protected PiplelineThread mThread = new PiplelineThread(this);
    public final RemoteCallbackList<IMsgCallback> msgCallbacks = new RemoteCallbackList<IMsgCallback>();

    protected void onMsg(MsgLevel level, String detail) {
        if (mThread.shutDown) {
            Log.i(TAG, "msg service is aleady in shutdown state. just ignore msg:" + detail);
            return;
        }

        synchronized (msgCallbacks) {
            final int N = msgCallbacks.beginBroadcast();
            for (int i = 0; i < N; i++) {
                try {
                    msgCallbacks.getBroadcastItem(i).onMsg(level.getId(), detail);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            msgCallbacks.finishBroadcast();
        }
    }

    protected void onReceive(int msgCode, AccessHeader header, byte[] bytes) {
        synchronized (msgCallbacks) {
            final int N = msgCallbacks.beginBroadcast();
            for (int i = 0; i < N; i++) {
                try {
                    Log.d(TAG,
                            "receive msg, msg code:" + msgCode + " hex:0x"
                                    + Integer.toHexString(msgCode));
                    msgCallbacks.getBroadcastItem(i).receive(msgCode, header, bytes);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            msgCallbacks.finishBroadcast();
        }
    }

    protected void onConnect() {
        synchronized (msgCallbacks) {
            // 解决在重连成功后出现java.lang.IllegalStateException，
            // 即没有调用beginBroadcast()就调用finishBroadcast。
            msgCallbacks.beginBroadcast();
            msgCallbacks.finishBroadcast();
        }
    }

    protected void onServiceError(String why) {
        onMsg(MsgLevel.ERROR, why);
        terminate();
        mThread.failOverLock.set(false);
    }

    protected void terminate() {
        Log.d(TAG, "wait for thread to be shut down.");
        mThread.reset();
        mThread.shutDown = true;
        mThread.state = AccessPipelineState.IDLE;

        synchronized (msgCallbacks) {
            final int N = msgCallbacks.beginBroadcast();
            for (int i = 0; i < N; i++) {
                try {
                    msgCallbacks.getBroadcastItem(i).onTerminate();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            msgCallbacks.finishBroadcast();
        }
    }

    protected boolean recvHeartbeatResp() {
        Log.d(TAG, "recv heartbeat resp!");
        return true;
    }

    /**
     * 开始登录，对于联网游戏，从此处开始建立与接入服务器的链接 对于局域网游戏，从此处开始与建立了服务器的玩家建立连接
     */
    protected abstract void startAuthentication();

    /**
     * 在连接出现异常时尝试重连
     */
    protected abstract void failOver();

    /**
     * 分发数据
     * 
     * @param recvData
     */
    protected abstract void dataDispatch(ByteBuffer recvData);
}
