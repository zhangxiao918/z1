/**
 * 
 */
package com.skymobi.android.transport;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hp
 *
 */
public class DefaultEndpointFactory implements EndpointFactory {

	private static final Logger logger = LoggerFactory.getLogger(DefaultEndpointFactory.class);
    private BlockingQueue<Object>	pendings = null;
    
    private MutableEndpoint endPoint;
    
    public DefaultEndpointFactory(boolean shareSendQueue, int cachedMessageCount) {
    	if ( shareSendQueue ) {
    		this.pendings = new LinkedBlockingQueue<Object>(cachedMessageCount);
    	}
    	else {
    		this.pendings = null;
    	}
    }
    
	/* (non-Javadoc)
	 * @see com.skymobi.transport.EndpointFactory#createEndpoint(org.apache.mina.core.session.IoSession)
	 */
	public MutableEndpoint createEndpoint(IoSession session) {
//		MutableEndpoint endpoint = createEndpoint();
		if(null != endPoint){
			endPoint.setIoSession(session);
			if ( null != pendings ) {
				endPoint.setSendQueue(pendings);
			}
			endPoint.start();
		}
		return endPoint;
	}
	
	public void stop() {
		if ( null != this.pendings ) {
			this.pendings.clear();
		}
	}
	
	public void send(Object bean) {
		if ( null != this.pendings ) {
			pendings.add(bean);
		}
		else {
			throw new RuntimeException("Unsupport Operation for send.");
		}
	}

	public int getPendingCount() {
		if ( null != this.pendings ) {
			return	this.pendings.size();
		}
		else {
			return	-1;
		}
	}
	
	
    public MutableEndpoint getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(MutableEndpoint endPoint) {
		this.endPoint = endPoint;
	}

}