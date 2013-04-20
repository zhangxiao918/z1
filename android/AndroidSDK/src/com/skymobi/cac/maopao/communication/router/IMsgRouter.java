package com.skymobi.cac.maopao.communication.router;

import com.skymobi.cac.maopao.communication.aidl.IMsgService;
import com.skymobi.cac.maopao.xip.ResponseCallBack;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.XipMessage;

public interface IMsgRouter {
	
	public int sendAndWait(XipMessage msg, ResponseCallBack<?> callback);
	public void cancelWait(int msgSeq);
	
	public int send(XipMessage msg);
	public void addHandler(int msgCode, MsgHandler<?> handler);
	public void addHandler(Class<? extends XipBody> cls, MsgHandler<?> handler);
	public void removeHandler(int msgCode, MsgHandler<?> handler);
	public void removeHandler(Class<? extends XipBody> cls, MsgHandler<?> handler);
	
	public void setMsgService(IMsgService msgService, boolean remote);
	
	public void shutDown();
}
