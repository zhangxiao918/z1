package com.skymobi.cac.maopao.xip;

import android.os.Handler;
import android.os.Message;

import com.skymobi.cac.maopao.communication.router.MsgHandler;
import com.skymobi.cac.maopao.xip.XipBody;

public class MsgXipBodyHandler implements MsgHandler<XipBody> {

	private int mMsgId = 0;
	private Handler mHandler; 

	public MsgXipBodyHandler(int msgId,Handler handler) {
		mMsgId = msgId;
		mHandler = handler;
	}

	@Override
	public boolean onMessage(XipBody message) {
		Message msg = mHandler.obtainMessage(mMsgId);
		msg.obj = message;
		mHandler.sendMessage(msg);
		return false;
	}
};