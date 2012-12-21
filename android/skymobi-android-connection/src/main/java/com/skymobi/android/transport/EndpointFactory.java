package com.skymobi.android.transport;

import org.apache.mina.core.session.IoSession;

public interface EndpointFactory {
	public Endpoint createEndpoint(IoSession session);
}
