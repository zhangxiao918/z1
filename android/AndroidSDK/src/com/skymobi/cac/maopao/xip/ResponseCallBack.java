package com.skymobi.cac.maopao.xip;

import java.lang.reflect.ParameterizedType;

import com.skymobi.cac.maopao.xip.annotation.XipSignal;

public abstract class ResponseCallBack <T extends XipBody> {

	public long sendTime;
	
	public int msgCode;
	
	public abstract void onResponse(T resp);
	
	public void onCancel(){
	
	}
	
	@SuppressWarnings("unchecked")
	public ResponseCallBack(){
		ParameterizedType superclass = (ParameterizedType) getClass()
				.getGenericSuperclass();
		Class<T> respClass = (Class<T>) superclass.getActualTypeArguments()[0];
		XipSignal attr = respClass.getAnnotation(XipSignal.class);
		if (null == attr) {
			throw new RuntimeException(
					"invalid xip body, no messageCode found.");
		}
		msgCode = attr.messageCode();
	}

	public int getMsgCode() {
		return msgCode;
	}
	

}
