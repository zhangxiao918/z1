package com.skymobi.cac.maopao.communication.router;

import com.skymobi.cac.maopao.xip.XipBody;

public interface MsgHandler<T extends XipBody> {
	
	/**
	 * 在同一个消息有多个handlers,返回true，则先加入队列的handler不会再去处理
	 *
	 * @param message
	 * @return
	 */
	public boolean onMessage(T message);
	
}
