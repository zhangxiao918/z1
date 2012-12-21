/**
 * 
 */
package com.skymobi.android.transport.protocol.esb.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skymobi.android.bean.bytebean.core.BeanFieldCodec;
import com.skymobi.android.bean.esb.core.EsbHeaderable;
import com.skymobi.android.bean.util.meta.Int2TypeMetainfo;
import com.skymobi.android.transport.protocol.esb.ESBFixedHeader;
import com.skymobi.android.util.ByteUtils;

/**
 * @author isdom
 *
 */
public class MinaEsbDecoder extends CumulativeProtocolDecoder {
	
    private static final Logger logger = 
    	LoggerFactory.getLogger(MinaEsbDecoder.class);
    
    private final AttributeKey FIXED_HEADER = 
    	new AttributeKey(getClass(), "EsbFixedHeader");

    private	BeanFieldCodec 		esbBeanCodec;
    private Int2TypeMetainfo 	esbTypeMetainfo;
    
    private	int					maxMessageLength = -1;
    
    private int dumpBytes = 256;
    private	boolean	isDebugEnabled;
    
	/* (non-Javadoc)
     * @see org.apache.mina.filter.codec.CumulativeProtocolDecoder#doDecode(org.apache.mina.core.session.IoSession, org.apache.mina.core.buffer.IoBuffer, org.apache.mina.filter.codec.ProtocolDecoderOutput)
     */
    @Override
    protected boolean doDecode(IoSession session, IoBuffer in,
            ProtocolDecoderOutput out) throws Exception {
        
    	ESBFixedHeader fixedHdr = (ESBFixedHeader)session.getAttribute(FIXED_HEADER);
    	
        if ( null == fixedHdr ) {
            if ( in.remaining() < ESBFixedHeader.FIXED_HEADER_SIZE ) {
                return false;
            }
            else {
                if ( logger.isTraceEnabled() && isDebugEnabled ) {
                    logger.trace("parse header... try parse...");
                }
                byte[] headerBytes = new byte[ESBFixedHeader.FIXED_HEADER_SIZE];
                in.get(headerBytes);
                
                if ( logger.isTraceEnabled() && isDebugEnabled) {
                    logger.trace("header raw bytes \r\n{}", 
                    		ByteUtils.bytesAsHexString(headerBytes, dumpBytes) );
                }
                
                
            	fixedHdr = EsbSignalCodecUtils.decodeSignalHeader(headerBytes, esbBeanCodec);
            	
            	if ( null == fixedHdr ) {
            		logger.error( " Can't decode esb's fixed header" );
            		return	false;
            	}
            	
                if ( logger.isTraceEnabled() && isDebugEnabled) {
                    logger.trace("fixed header {}", fixedHdr);
                }
                
                if ( maxMessageLength > 0 ) {
                	if ( fixedHdr.getLength() > maxMessageLength ) {
                		logger.error( "header.length {} exceed maxMessageLength {}, "
                				+ " so drop this connection.\r\ndump bytes received:\r\n"
                				+ ByteUtils.bytesAsHexString(headerBytes, dumpBytes), 
                				fixedHdr.getLength(), maxMessageLength );
                		session.close(true);
                		return	false;
                	}
                }
                
                // Update the session attribute.
                recordFixedHeader(session, fixedHdr);
            }
        }
        
        int bodyLength = fixedHdr.getLength() - ESBFixedHeader.FIXED_HEADER_SIZE;
        if ( in.remaining() < bodyLength ) {
            return false;
        }
        else {
            //  涓轰笅涓�鍦ㄥ悓涓�ession涓婅繘琛寈ip鎺ュ彈鍒濆鍖栫幆澧�
            removeSessionHeaders(session);
            
            byte[] bytes = new byte[bodyLength];
            in.get(bytes);
            
            if ( logger.isTraceEnabled() && isDebugEnabled) {
                logger.trace("body raw bytes \r\n{}", 
                		ByteUtils.bytesAsHexString(bytes, dumpBytes) );
            }
            
            EsbHeaderable signal = EsbSignalCodecUtils.decodeSignalBody(
            		fixedHdr, bytes, esbBeanCodec, esbTypeMetainfo);
            
            if ( null != signal ) {
                if ( logger.isDebugEnabled() && isDebugEnabled) {
                    logger.debug("session {} \r\nrecv signal {}", session, signal);
                }
                
                out.write( signal );
                
                return true;
            }
            else {
	            if ( logger.isTraceEnabled() && isDebugEnabled) {
	                logger.trace("can't decode esb signal body, " +
	                		"maybe unknow protocol code {} \r\n", fixedHdr.getProtocolCode() );
	            }
	            
	        	return	false;
            }
        }
    }

	@Override
    public void dispose(IoSession session) throws Exception {
        super.dispose(session);
        
        //  our dispose
        removeSessionHeaders(session);
    }
    
    private void recordFixedHeader(IoSession session, ESBFixedHeader header) {
        session.setAttribute(FIXED_HEADER, header);
    }
    
    private void removeSessionHeaders(IoSession session) {
        session.removeAttribute(FIXED_HEADER);
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

	/**
	 * @return the esbTypeMetainfo
	 */
	public Int2TypeMetainfo getEsbTypeMetainfo() {
		return esbTypeMetainfo;
	}

	/**
	 * @param esbTypeMetainfo the esbTypeMetainfo to set
	 */
	public void setEsbTypeMetainfo(Int2TypeMetainfo esbTypeMetainfo) {
		this.esbTypeMetainfo = esbTypeMetainfo;
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
