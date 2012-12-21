package com.skymobi.android.transport.codec;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkyAndroidEncoder implements ProtocolEncoder {

	private static final Logger logger = LoggerFactory.getLogger(SkyAndroidEncoder.class);
	private final Charset charset = Charset.forName("UTF-8"); 
	
	/**
	 * »±…Ÿ±‡¬Îƒ⁄»›
	 */
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out)
			throws Exception {
		
	}

	public void dispose(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub
		logger.debug("TcpMessageDecoder.dispose:" + arg0);
		
	}

    
}
