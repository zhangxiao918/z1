/**
 * 
 */
package com.skymobi.android.transport.protocol.pf.codec;

import org.apache.commons.lang.ArrayUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skymobi.android.bean.bytebean.core.BeanFieldCodec;
import com.skymobi.android.bean.pf.annotation.PFSignal;
import com.skymobi.android.bean.pf.core.IPFSignal;
import com.skymobi.android.transport.protocol.pf.PFHeader;
import com.skymobi.android.util.ByteUtils;


/**
 * @author isdom
 *
 */
public class MinaPfEncoder implements ProtocolEncoder  {
	
    private static final Logger logger = LoggerFactory.getLogger(MinaPfEncoder.class);
    private int dumpBytes = 256;
    private	boolean	isDebugEnabled;
    private	BeanFieldCodec pfBeanCodec;
    
	/**
	 * @return the pfBeanCodec
	 */
	public BeanFieldCodec getPfBeanCodec() {
		return pfBeanCodec;
	}

	/**
	 * @param pfBeanCodec the pfBeanCodec to set
	 */
	public void setPfBeanCodec(BeanFieldCodec pfBeanCodec) {
		this.pfBeanCodec = pfBeanCodec;
	}

	private byte[] encodePf( IPFSignal signal) throws Exception {
    	//	once
        byte[] bytesBody =
        	pfBeanCodec.encode(
    			pfBeanCodec.getEncContextFactory().createEncContext(
    					signal, signal.getClass(), null));
        
        int	bodyLength = (null != bytesBody) ? bytesBody.length : 0;
        
		PFSignal attr = signal.getClass().getAnnotation(PFSignal.class);
		if ( null == attr ) {
			throw new RuntimeException("invalid pf signal, bcs of no messageCode.");
		}
		
		//	create pf header
		PFHeader hdr = new PFHeader();
		
		hdr.setMsgCode(attr.messageCode());
		hdr.setResult(signal.getResult());
		hdr.setSeqId(signal.getSeqId());
		hdr.setMsgbodylen( bodyLength );
		
        byte[] bytes = ArrayUtils.addAll(
        	pfBeanCodec.encode(
    				pfBeanCodec.getEncContextFactory().createEncContext(
    						hdr, PFHeader.class, null) ),
    		bytesBody );
        
        if ( logger.isTraceEnabled() && isDebugEnabled ) {
            logger.trace( "encode PfMessage {} \r\nand PfMessage raw bytes \r\n{}", 
            		signal, ByteUtils.bytesAsHexString(bytes, dumpBytes) );
        }
        
        return	bytes;
	}
	
	/* (non-Javadoc)
     * @see org.apache.mina.filter.codec.ProtocolEncoder#encode(org.apache.mina.core.session.IoSession, java.lang.Object, org.apache.mina.filter.codec.ProtocolEncoderOutput)
     */
    public void encode(IoSession session, Object message, ProtocolEncoderOutput out)
            throws Exception {

    	byte[] bytes = null;
    	if ( message instanceof IPFSignal ) {
    		bytes = encodePf((IPFSignal)message);
    	}
    	else {
	        throw   new RuntimeException("encode: bean " + message 
	        		+ " is not PFSignal.");
    	}
        if ( null != bytes ) {
            if ( logger.isDebugEnabled() && isDebugEnabled) {
                logger.debug("session {} \r\nsend signal {}", session, message);
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
