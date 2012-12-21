/**
 * 
 */
package com.skymobi.android.transport.protocol.esb.hdr.a2m;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skymobi.android.bean.bytebean.codec.AbstractPrimitiveCodec;
import com.skymobi.android.bean.bytebean.core.ByteFieldCodec;
import com.skymobi.android.bean.bytebean.core.DecContext;
import com.skymobi.android.bean.bytebean.core.DecResult;
import com.skymobi.android.bean.bytebean.core.EncContext;
import com.skymobi.android.bean.esb.annotation.EsbField;
import com.skymobi.android.transport.protocol.esb.hdr.ETFTerminalMessageHeader;


//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|           srcmodule(2)        |        terminalModule(2)      |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|           length(2)           |        flags(2)               |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|           msgCode(2)          |    nResult(1) | application...|
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|application data field(...)                                    |
//|                                                               |
//|                                                               |
//|                                                               |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+

/**
 * @author Marvin.Ma
 *
 */
public class SanpTerminalMsg {
	
	@EsbField(index = 1)
	private	ETFTerminalMessageHeader	terminalMessageHeader;
	
	//	use custom codec to deal msgBody's length calc
	@EsbField(index = 2, customCodec = MyByteArrayCodec.class )
	private byte[] msgBody;
	
	public final static class MyByteArrayCodec extends AbstractPrimitiveCodec 
		implements ByteFieldCodec {

	    private static final Logger logger = 
	    	LoggerFactory.getLogger(MyByteArrayCodec.class);
	    
	    public DecResult decode(DecContext ctx) {
	        SanpTerminalMsg	owner = (SanpTerminalMsg)ctx.getDecOwner();
	        
	        //	msgBody's length = hdr's length field value - hdr's size (10 bytes)
	        int     arrayLength = owner.terminalMessageHeader.getMessageBodySize();
	        byte[] bytes = ctx.getDecBytes();
	        
	        if ( bytes.length < arrayLength ) {
	            String  errmsg = "SanpTerminalMsg's MyByteArrayCodec: not enough bytes for decode, need [" 
	            	+ arrayLength + "], actually [" + bytes.length +"].";
	            if ( null != ctx.getField() ) {
	                errmsg += "/ cause field is [" + ctx.getField() + "]";
	            }
	            logger.error(errmsg);
	            throw   new RuntimeException(errmsg);
	            //return makeDecResult( new byte[0], bytes );
	        }
	        
	        return  new DecResult( (byte[])ArrayUtils.subarray(bytes, 0, arrayLength), 
	                ArrayUtils.subarray(bytes, arrayLength, bytes.length));
	    }

	    public byte[] encode(EncContext ctx) {
	        byte[]          array = (byte[])ctx.getEncObject();
	        byte[]          bytes = null;
	        
	        return  ArrayUtils.addAll( bytes, array);
	    }

	    public Class<?>[] getFieldType() {
	        return new Class<?>[]{ byte[].class };
	    }
	}

    public String toString() {
        
        return  ToStringBuilder.reflectionToString(this, 
                            ToStringStyle.SHORT_PREFIX_STYLE);
    }

	/**
	 * @return the msgBody
	 */
	public byte[] getMsgBody() {
		return msgBody;
	}

	/**
	 * @return the terminalMessageHeader
	 */
	public ETFTerminalMessageHeader getETFTerminalMessageHeader() {
		return terminalMessageHeader;
	}

	/**
	 * @param msgBody the msgBody to set
	 */
	public SanpTerminalMsg setMsgBody(byte[] msgBody) {
		this.msgBody = msgBody;
		return	this;
	}

	/**
	 * @param terminalMessageHeader the terminalMessageHeader to set
	 */
	public SanpTerminalMsg setETFTerminalMessageHeader(
			ETFTerminalMessageHeader terminalMessageHeader) {
		this.terminalMessageHeader = terminalMessageHeader;
		return	this;
	}
}
