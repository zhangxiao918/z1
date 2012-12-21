package com.skymobi.android.transport;

import java.util.concurrent.BlockingQueue;

import org.apache.mina.core.session.IoSession;

public interface MutableEndpoint extends Endpoint {
	public void start();
	public void setIoSession(IoSession session);
	public void setSendQueue(BlockingQueue<Object> queue);
}
