/**
 * 
 */
package com.skymobi.android.transport;

import java.util.concurrent.BlockingQueue;

import org.apache.mina.core.session.IoSession;

/**
 * @author hp
 *
 */
public abstract class AbstractEndpoint implements MutableEndpoint {
	
	protected	Receiver		nextReceiver;
	protected 	IoSession		session = null;
    
    protected 	BlockingQueue<Object>	pendings = null;
    
    protected	String	objectNamePrefix;
    
	/**
	 * @return the objectNamePrefix
	 */
	public String getObjectNamePrefix() {
		return objectNamePrefix;
	}

	/**
	 * @param objectNamePrefix the objectNamePrefix to set
	 */
	public void setObjectNamePrefix(String objectNamePrefix) {
		this.objectNamePrefix = objectNamePrefix;
	}

	/* (non-Javadoc)
	 * @see com.skymobi.transport.Sender#send(java.lang.Object)
	 */
	public void send(Object bean) {
    	pendings.add(bean);
	}

	/* (non-Javadoc)
	 * @see com.skymobi.transport.Receiver#messageReceived(org.apache.mina.core.session.IoSession, java.lang.Object)
	 */
	public void messageReceived(IoSession session, Object msg) {
		if ( null != nextReceiver ) {
			this.nextReceiver.messageReceived(session, msg);
		}
	}

	public void setIoSession(IoSession session) {
		this.session = session;
	}

	/* (non-Javadoc)
	 * @see com.skymobi.transport.Endpoint#stop()
	 */
	public void stop() {
		if(null != session)
		{
			session  = null;
		}
	}

	public void setSendQueue(BlockingQueue<Object> queue) {
		this.pendings = queue;
	}

	/**
	 * @param nextReceiver the nextReceiver to set
	 */
	public void setNextReceiver(Receiver nextReceiver) {
		this.nextReceiver = nextReceiver;
	}

	public int getPendingCount() {
		if ( null != this.pendings ) {
			return	this.pendings.size();
		}
		else {
			return	-1;
		}
	}
}
