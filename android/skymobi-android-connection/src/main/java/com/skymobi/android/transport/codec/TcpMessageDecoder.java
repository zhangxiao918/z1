package com.skymobi.android.transport.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * a decoder for {@link ImageRequest} objects
 *
 * @author The Apache MINA Project (dev@mina.apache.org)
 */

public class TcpMessageDecoder implements ProtocolDecoder {
	private static final Logger logger = LoggerFactory.getLogger(TcpMessageDecoder.class);
	
	/**
	 * 对输入内容的解码
	 */
	public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out)
			throws Exception {
		/*
		//字符串解码
		IoBuffer buf = IoBuffer.allocate(100).setAutoExpand(true);  
        CharsetDecoder cd = Charset.forName("UTF-8").newDecoder();  
        while(in.hasRemaining()){
        	buf.put(in.get());
    	}
        buf.flip();
        out.write(buf.getString(cd));
        */
		IoBuffer buf = IoBuffer.allocate(1024);    
        while(in.hasRemaining()){
        	buf.put(in.get());
    	}
        buf.flip();
        byte[] bs = buf.array();
        logger.info(" > decode.buf.length:{}",bs.length);
        logger.info(" > decode.buf.conent:{}",bs);
        out.write(bs);
//        out.write(in);
    }

	public static String WritebyteToString(byte[] data, byte fillbyte) {
		int t = data.length;
		for (int i = 0; i < data.length; i++) {
			if (data[i] == fillbyte) {
				t = i;
				break;
			}
		}
		return new String(data, 0, t);
	}
	
	public void dispose(IoSession arg0) throws Exception {
		logger.debug("TcpMessageDecoder.dispose:" + arg0);
	}

	public void finishDecode(IoSession arg0, ProtocolDecoderOutput arg1)
			throws Exception {
		logger.debug("TcpMessageDecoder.finishDecode:" + arg0);
	}

}
