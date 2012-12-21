/**
 * 
 */
package com.skymobi.android.transport;

import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;

/**
 * @author hp
 *
 */
public class TransportUtils {
    private static final AttributeKey TRANSPORT_ENDPOINT = 
    	new AttributeKey(TransportUtils.class, "TRANSPORT_ENDPOINT");
    
    private static final String TRANSPORT_SENDER = "TRANSPORT_SENDER";
	
	public static void attachEndpointToSession(IoSession session, Endpoint endpoint) {
		session.setAttribute(TRANSPORT_ENDPOINT, endpoint);
	}
	
	public static Endpoint getEndpointOfSession(IoSession session) {
		return	(Endpoint)session.getAttribute(TRANSPORT_ENDPOINT);
	}
	
	public static Object attachSender(Object mutablePropertyable, Sender sender) {
//		if ( mutablePropertyable instanceof MutablePropertyable) {
//			((MutablePropertyable)mutablePropertyable).setProperty(
//					TRANSPORT_SENDER, sender);
//		}
		
		return	mutablePropertyable;
	}
	
	public static Sender getSenderOf(Object propertyable) {
//		if ( propertyable instanceof Propertyable) {
//			return (Sender)((Propertyable)propertyable).getProperty(TRANSPORT_SENDER);
//		}
		return null;
	}
}
