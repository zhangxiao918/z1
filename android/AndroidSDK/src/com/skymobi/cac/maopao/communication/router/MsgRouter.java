package com.skymobi.cac.maopao.communication.router;

import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;
import com.skymobi.cac.maopao.communication.aidl.IMsgCallback;
import com.skymobi.cac.maopao.communication.aidl.IMsgService;
import com.skymobi.cac.maopao.xip.AccessHeader;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.MessageCodecAgent;
import com.skymobi.cac.maopao.xip.ResponseCallBack;
import com.skymobi.cac.maopao.xip.XipBeanLoader;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.XipMessage;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

public class MsgRouter implements IMsgRouter {

	private IMsgService msgService;
	
	private IMsgCallback msgCallback;
	
	private final static String TAG = "MsgRouter";
	
	protected Map<Integer, List<MsgHandler<?>>> msgHandlerMap 
			= new ConcurrentHashMap<Integer, List<MsgHandler<?>>>();
	
	protected Map<Integer, ResponseCallBack> callBackMap 
			= new ConcurrentHashMap<Integer, ResponseCallBack>();
	
	protected Map<Integer, List<XipBody>> unHandledMsgMap 
			= new ConcurrentHashMap<Integer, List<XipBody>>();
	
	protected boolean shutdown;
	
	public void setMsgService(IMsgService msgService, boolean remote){
		if(this.msgService != null){
			try {
				msgService.unregisterCallback(msgCallback);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		
		this.msgService = msgService;
		if(remote){
			this.msgCallback = new IMsgCallback.Stub() {

				@Override
				public void receive(int msgCode, AccessHeader header,
						byte[] bytes) throws RemoteException {
					doOnReceive(msgCode, header, bytes);
					
				}

				@Override
				public void onMsg(int level, String detail)
						throws RemoteException {
					// TODO 处理全局推送消息
					
				}

				@Override
				public void onTerminate() throws RemoteException {
					// TODO 连接出现问题，断开，需要处理
				}
			};
		}else{
			this.msgCallback = new IMsgCallback(){
				@Override
				public IBinder asBinder() {
					return new IBinder(){
						@Override
						public void dump(FileDescriptor fd, String[] args)
								throws RemoteException {
							
						}
						@Override
						public String getInterfaceDescriptor()
								throws RemoteException {
							return null;
						}

						@Override
						public boolean isBinderAlive() {
							return true;
						}

						@Override
						public void linkToDeath(DeathRecipient recipient,
								int flags) throws RemoteException {
							
						}

						@Override
						public boolean pingBinder() {
							return true;
						}

						@Override
						public IInterface queryLocalInterface(String descriptor) {
							return null;
						}

						@Override
						public boolean transact(int code, Parcel data,
								Parcel reply, int flags) throws RemoteException {
							return false;
						}

						@Override
						public boolean unlinkToDeath(DeathRecipient recipient,
								int flags) {
							return false;
						}
						
					};
				}

				@Override
				public void receive(int msgCode, AccessHeader header, byte[] bytes)
						throws RemoteException {
					doOnReceive(msgCode, header, bytes);
				}

				@Override
				public void onMsg(int level, String detail)
						throws RemoteException {
					// TODO 处理全局推送消息。
					
				}

				@Override
				public void onTerminate() throws RemoteException {
					// TODO 连接出现问题，断开，需要处理
					
				}
			};
		}
		try {
			this.msgService.registerCallback(msgCallback);
			if(this.msgService.loginOk()){
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public MsgRouter(IMsgService msgService, boolean remote){
		this.setMsgService(msgService, remote);
	}
	
	public MsgRouter(){
		
	}
	
	@Override
	public void cancelWait(int msgCode) {
		this.callBackMap.remove(msgCode);
	}

	@Override
	public int send(XipMessage msg) {
		try {
			if(msgService == null || !msgService.loginOk()){
				Log.e(TAG, "msg with code:" + msg.getHeader().getMsgCodeStr() + " can not be send because the connection is not ready");
				return -1;
			}
		} catch (RemoteException e1) {
			e1.printStackTrace();
			return -1;
		}
		int seq = -1;
		try {
			byte[] bytes = MessageCodecAgent.getInstance().encodeXip(msg);
			boolean encryptAndSeq = (msg.getHeader().getFlags() & BasisMessageConstants.MSG_FLAG_ENCRYPT_SEQ) == BasisMessageConstants.MSG_FLAG_ENCRYPT_SEQ;
			if(!encryptAndSeq){
				Log.d(TAG, "msg with code " + msg.getHeader().getMsgCodeStr()+" is not encrypted!");
			}
			msgService.send(bytes, encryptAndSeq, encryptAndSeq);
		} catch (Exception e) {
			Log.e(TAG,"exception while encode message."+ e.getMessage());
			e.printStackTrace();
		}
		return seq;
		
	}


	@Override
	public void addHandler(int msgCode, MsgHandler<?> handler) {
		synchronized(msgHandlerMap){
			List<MsgHandler<?>> hList = msgHandlerMap.get(msgCode);
			if(hList == null){
				hList = new CopyOnWriteArrayList<MsgHandler<?>>();
				msgHandlerMap.put(msgCode, hList);
			}
			hList.add(handler);
			List<XipBody> mList = unHandledMsgMap.get(msgCode);
			if(null != mList) {
				for(XipBody msg : mList) {
					((MsgHandler)handler).onMessage(msg);
				}
				mList.clear();
			}
		}
	}

	@Override
	public void addHandler(Class<? extends XipBody> cls, MsgHandler<?> handler) {
		if (XipBody.class.isAssignableFrom(cls)) {
			XipSignal attr = cls.getAnnotation(XipSignal.class);
			if (null != attr) {
				this.addHandler(attr.messageCode(), handler);
			}
		}
	}

	@Override
	public void removeHandler(int msgCode, MsgHandler<?> handler){
		synchronized(msgHandlerMap){
			List<MsgHandler<?>> hList = msgHandlerMap.get(msgCode);
			if(hList != null){
				hList.remove(handler);
			}
			unHandledMsgMap.remove(msgCode);
		}
	}
	
	@Override
	public void removeHandler(Class<? extends XipBody> cls, MsgHandler<?> handler){
		if (XipBody.class.isAssignableFrom(cls)) {
			XipSignal attr = cls.getAnnotation(XipSignal.class);
			if (null != attr) {
				this.removeHandler(attr.messageCode(), handler);
			}
		}
	}
	
	@Override
	public int sendAndWait(XipMessage msg, ResponseCallBack<?> callback) {
		callback.sendTime = System.currentTimeMillis();
		int msgCode = callback.getMsgCode();
		this.callBackMap.put(msgCode, callback);
		this.send(msg);
		return msgCode;
	}

	@Override
	public void shutDown() {
		this.callBackMap.clear();
		this.msgHandlerMap.clear();
		shutdown = true;
		if(this.msgService == null){
			return;
		}
		try {
			this.msgService.unregisterCallback(msgCallback);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	
	
	private void doOnReceive(int msgCode, AccessHeader header,  byte[] bytes){
		Class respClass = XipBeanLoader.msgCodeXipMap.get(msgCode);
		Log.d(TAG, "receive msg, msg code:"+ msgCode + " hex:0x"+ Integer.toHexString(msgCode));
		XipBody message = null;
		if(respClass != null){
			if(bytes != null){
				message = MessageCodecAgent.getInstance().decodeXip(bytes, header, respClass);
			}
		}else{
			Log.i(TAG,"no resp class correspanding to msg code :"+ msgCode);
			return;
		}
		
		List<MsgHandler<?>> handlers = this.msgHandlerMap.get(msgCode);
		ResponseCallBack callBack = this.callBackMap.remove(msgCode);
		
		if((handlers == null || handlers.isEmpty()) && callBack == null){
			Log.w(TAG, "no handlers and callback defined for msgCode:" + this.getMsgCodeStr(msgCode));
			if(null != message) {
				List<XipBody> list = unHandledMsgMap.get(msgCode);
				if(null == list) {
					list = new ArrayList<XipBody>();
					unHandledMsgMap.put(msgCode, list);
				}
				list.add(message);
			}
			return;
		}
		
		if(callBack != null){
			callBack.onResponse(message);
		}
		
		if(handlers != null && !handlers.isEmpty()){
			callMsgHandler(message, handlers);
		}
	}

	/**
	 * 后进先出的形式处理消息，
	 * 
	 * @param message
	 * @param handlers
	 */
	private void callMsgHandler(XipBody message, List<MsgHandler<?>> handlers) {
		List<MsgHandler<?>> temp = new ArrayList<MsgHandler<?>>();
		temp.addAll(handlers);
		final int length = temp.size();
		for(int i = length - 1;i>=0;i--){
			MsgHandler handler = temp.get(i);
			if(handler.onMessage(message)){
				break;
			}
		}
		temp.clear();
	}
	
	public String getMsgCodeStr(int msgCode){
		return msgCode + "/0x" + Integer.toHexString(msgCode);
	}
}
