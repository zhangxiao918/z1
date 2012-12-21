package com.skymobi.android.transport.receiver;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skymobi.android.transport.Receiver;

public class TcpReceiver implements Receiver {

	private static final Logger logger = LoggerFactory.getLogger(TcpReceiver.class);
	
	public synchronized void messageReceived(IoSession session, Object msg) {
		if(null != session)
		{
			logger.debug(" > receiver:{}",msg);
		}
	}

}
