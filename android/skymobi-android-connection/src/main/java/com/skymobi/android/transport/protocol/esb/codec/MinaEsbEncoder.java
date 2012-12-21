/**
 * 
 */
package com.skymobi.android.transport.protocol.esb.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skymobi.android.bean.bytebean.core.BeanFieldCodec;
import com.skymobi.android.bean.esb.core.EsbHeaderable;
import com.skymobi.android.util.ByteUtils;

/**
 * @author isdom
 *
 */
public class MinaEsbEncoder implements ProtocolEncoder  {
    
    private static final Logger logger 
    	= LoggerFactory.getLogger(MinaEsbEncoder.class);
    
    private int 			dumpBytes = 256;
    private	boolean			isDebugEnabled;
    private	BeanFieldCodec 	esbBeanCodec;
    
    private	int	myESBAddr;

	/**
	 * @return the myESBAddr
	 */
	public int getMyESBAddr() {
		return myESBAddr;
	}

	/**
	 * @param myESBAddr the myESBAddr to set
	 */
	public void setMyESBAddr(int myESBAddr) {
		this.myESBAddr = myESBAddr;
	}

	/**
	 * @return the esbBeanCodec
	 */
	public BeanFieldCodec getEsbBeanCodec() {
		return esbBeanCodec;
	}

	/**
	 * @param esbBeanCodec the esbBeanCodec to set
	 */
	public void setEsbBeanCodec(BeanFieldCodec esbBeanCodec) {
		this.esbBeanCodec = esbBeanCodec;
	}

	/* (non-Javadoc)
     * @see org.apache.mina.filter.codec.ProtocolEncoder#encode(org.apache.mina.core.session.IoSession, java.lang.Object, org.apache.mina.filter.codec.ProtocolEncoderOutput)
     */
    public void encode(IoSession session, Object message, ProtocolEncoderOutput out)
            throws Exception {

    	byte[] bytes = EsbSignalCodecUtils.encodeSignal(
    			(EsbHeaderable) message, esbBeanCodec, myESBAddr);
    	
        if ( null != bytes ) {
            if ( logger.isDebugEnabled() && isDebugEnabled) {
                logger.debug("session {} \r\nsend signal {}", session, message);
            }
            if ( logger.isTraceEnabled() && isDebugEnabled ) {
                logger.trace( "and raw bytes \r\n{}", 
                		ByteUtils.bytesAsHexString(bytes, dumpBytes) );
            }
            out.write( IoBuffer.wrap(bytes) );
        }
        else {
            logger.error("encode: " + message + " can not generate byte stream.");
        }
    }

	public boolean isDebugEnabled() {
		return isDebugEnabled;
	}

	public void setDebugEnabled(boolean isDebugEnabled) {
		this.isDebugEnabled = isDebugEnabled;
	}

	public int getDumpBytes() {
		return dumpBytes;
	}
	
    /**
     * @param dumpBytes the dumpBytes to set
     */
    public void setDumpBytes(int dumpBytes) {
        this.dumpBytes = dumpBytes;
    }

	public void dispose(IoSession session) throws Exception {
	}
}
