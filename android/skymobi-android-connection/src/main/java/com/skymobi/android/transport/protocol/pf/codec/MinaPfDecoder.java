/**
 * 
 */
package com.skymobi.android.transport.protocol.pf.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skymobi.android.bean.bytebean.core.BeanFieldCodec;
import com.skymobi.android.bean.bytebean.core.DecContext;
import com.skymobi.android.bean.pf.core.IPFSignal;
import com.skymobi.android.bean.util.meta.Int2TypeMetainfo;
import com.skymobi.android.transport.protocol.pf.PFHeader;
import com.skymobi.android.util.ByteUtils;

/**
 * 
 * @author Shawn.Du
 *
 */
public class MinaPfDecoder extends CumulativeProtocolDecoder {

    private static final Logger logger = LoggerFactory.getLogger(MinaPfDecoder.class);
    
    private final AttributeKey HEADER = new AttributeKey(getClass(), "PfHeader");

    private	BeanFieldCodec 		pfBeanCodec;
    private Int2TypeMetainfo 	pfTypeMetainfo;
    
    private	int					maxMessageLength = -1;
    
    private int dumpBytes = 256;
    private	boolean	isDebugEnabled;
    
	/* (non-Javadoc)
     * @see org.apache.mina.filter.codec.CumulativeProtocolDecoder#doDecode(org.apache.mina.core.session.IoSession, org.apache.mina.core.buffer.IoBuffer, org.apache.mina.filter.codec.ProtocolDecoderOutput)
     */
    @Override
    protected boolean doDecode(IoSession session, IoBuffer in,
            ProtocolDecoderOutput out) throws Exception {
        
    	PFHeader hdr = (PFHeader)session.getAttribute(HEADER);
    	
    	int headerSize = pfBeanCodec.getStaticByteSize(PFHeader.class);
    	
        if ( null == hdr ) {
            if ( in.remaining() < headerSize ) {
                return false;
            }
            else {
                if ( logger.isTraceEnabled() && isDebugEnabled ) {
                    logger.trace("parse header... try parse...");
                }
                byte[] headerBytes = new byte[headerSize];
                in.get(headerBytes);
                
                if ( logger.isTraceEnabled() && isDebugEnabled) {
                    logger.trace("header raw bytes \r\n{}", 
                    		ByteUtils.bytesAsHexString(headerBytes, dumpBytes) );
                }
                
            	hdr = (PFHeader)pfBeanCodec.decode(
            			pfBeanCodec.getDecContextFactory().createDecContext(
            					headerBytes, PFHeader.class, null, null) ).getValue();
                if ( logger.isTraceEnabled() && isDebugEnabled) {
                    logger.trace("header {}", hdr);
                }
                
                if ( maxMessageLength > 0 ) {
                	if ( hdr.getMsgbodylen()+pfBeanCodec.getStaticByteSize(PFHeader.class) > maxMessageLength ) {
                		logger.error( "header.length {} exceed maxMessageLength {}, "
                				+ " so drop this connection.\r\ndump bytes received:\r\n"
                				+ ByteUtils.bytesAsHexString(headerBytes, dumpBytes), 
                				hdr.getMsgbodylen()+pfBeanCodec.getStaticByteSize(PFHeader.class), maxMessageLength );
                		session.close(true);
                		return	false;
                	}
                }
                
                // Update the session attribute.
                setSessionHeader(session, hdr);
            }
        }
        
        int bodyLength = hdr.getMsgbodylen();
        if ( in.remaining() < bodyLength ) {
            return false;
        }
        else {
            //  涓轰笅涓�鍦ㄥ悓涓�ession涓婅繘琛寈ip鎺ュ彈鍒濆鍖栫幆澧�
            removeSessionHeader(session);
            
            byte[] bytes = new byte[bodyLength];
            in.get(bytes);
            
            if ( logger.isTraceEnabled() && isDebugEnabled) {
                logger.trace("body raw bytes \r\n{}", ByteUtils.bytesAsHexString(bytes, dumpBytes) );
            }
            
            IPFSignal signal = null;
            Class<?> type = pfTypeMetainfo.find(hdr.getMsgCode());
            if ( null == type ) {
            	throw new RuntimeException("unknow message code:" + hdr.getMsgCode());
            }
            
            DecContext decCtx = pfBeanCodec.getDecContextFactory().createDecContext(
					bytes, type, null, null);
            
			decCtx.setProperty(IPFSignal.class.getSimpleName(), hdr);
					
            signal = (IPFSignal)pfBeanCodec.decode(decCtx).getValue();
            
            signal.setResult( hdr.getResult() );
            signal.setSeqId( hdr.getSeqId() );
    		
            if ( logger.isDebugEnabled() && isDebugEnabled) {
                logger.debug("session {} \r\nrecv signal {}", session, signal);
            }

            out.write( signal );
            
            return true;
        }
    }

    @Override
    public void dispose(IoSession session) throws Exception {
        super.dispose(session);
        
        //  our dispose
        removeSessionHeader(session);
    }
    
    private void setSessionHeader(IoSession session, PFHeader header) {
        session.setAttribute(HEADER, header);
    }
    
    private void removeSessionHeader(IoSession session) {
        session.removeAttribute(HEADER);
    }

    /**
     * @param dumpBytes the dumpBytes to set
     */
    public void setDumpBytes(int dumpBytes) {
        this.dumpBytes = dumpBytes;
    }
    
	public int getDumpBytes() {
		return dumpBytes;
	}

	public boolean isDebugEnabled() {
		return isDebugEnabled;
	}

	public void setDebugEnabled(boolean isDebugEnabled) {
		this.isDebugEnabled = isDebugEnabled;
	}

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

	/**
	 * @return the pfTypeMetainfo
	 */
	public Int2TypeMetainfo getPfTypeMetainfo() {
		return pfTypeMetainfo;
	}

	/**
	 * @param pfTypeMetainfo the pfTypeMetainfo to set
	 */
	public void setPfTypeMetainfo(Int2TypeMetainfo pfTypeMetainfo) {
		this.pfTypeMetainfo = pfTypeMetainfo;
	}

	/**
	 * @return the maxMessageLength
	 */
	public int getMaxMessageLength() {
		return maxMessageLength;
	}

	/**
	 * @param maxMessageLength the maxMessageLength to set
	 */
	public void setMaxMessageLength(int maxMessageLength) {
		this.maxMessageLength = maxMessageLength;
	}

}
