/**
 * 
 */
package com.skymobi.android.transport;

import org.apache.mina.core.session.IoSession;

/**
 * @author hp
 *
 */
public interface Receiver {
	public void messageReceived(IoSession session, Object msg);
}
