/**
 * 
 */
package com.skymobi.android.bean.pf.codec;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skymobi.android.bean.bytebean.codec.AbstractPrimitiveCodec;
import com.skymobi.android.bean.bytebean.core.ByteFieldCodec;
import com.skymobi.android.bean.bytebean.core.ByteFieldDesc;
import com.skymobi.android.bean.bytebean.core.DecContext;
import com.skymobi.android.bean.bytebean.core.DecResult;
import com.skymobi.android.bean.bytebean.core.EncContext;
import com.skymobi.android.util.NumberCodec;

/**
 * @author isdom
 *
 */
public class StringCodecForLengthInBytes extends AbstractPrimitiveCodec 
	implements ByteFieldCodec {
    private static final Logger logger = 
    	LoggerFactory.getLogger(StringCodecForLengthInBytes.class);

    public DecResult decode(DecContext ctx) {
        byte[] bytes = ctx.getDecBytes();
        ByteFieldDesc   desc = ctx.getFieldDesc();
        if ( null == desc ) {
            throw   new RuntimeException("StringCodecForLengthInBytes: ByteFieldDesc is null");
        }
        
        int length = desc.getFixedLength();
        
        if ( length < 0 ) {
            length = desc.getLength( ctx.getDecOwner() );
        }
        
        if ( length < 0 ) {
            throw   new RuntimeException("StringCodecForLengthInBytes: length < 0");
        }
        NumberCodec     numberCodec = ctx.getNumberCodec();

        String  charset = numberCodec.convertCharset( desc.getCharset() );
        
        String  ret = null;
        
        if ( length > bytes.length ) {
            String  errmsg = "StringCodecForLengthInBytes: not enough bytes for decode, need [" + length 
                                + "], actually [" + bytes.length +"].";
            if ( null != ctx.getField() ) {
                errmsg += "/ cause field is [" + ctx.getField() + "]";
            }
            logger.error(errmsg);
            throw   new RuntimeException(errmsg);
            //return makeDecResult( ret, bytes);
        }
        
        if ( length > 0 ) {
            try {
                ret = new String(ArrayUtils.subarray(bytes, 0, length), charset);
            } catch (UnsupportedEncodingException e) {
                logger.error( "StringCodecForLengthInBytes", e);
            }
        }
        return new DecResult( ret, ArrayUtils.subarray(bytes, length, bytes.length));
    }

    public byte[] encode(EncContext ctx) {
        String  value = (String)ctx.getEncObject();
        ByteFieldDesc   desc = ctx.getFieldDesc();
        NumberCodec     numberCodec = ctx.getNumberCodec();
        
        if ( null == desc ) {
            throw   new RuntimeException("StringCodecForLengthInBytes: ByteFieldDesc is null");
        }

        String  charset = numberCodec.convertCharset( desc.getCharset() );

        byte[] bytes = null;
        
        if ( null == value ) {
            bytes = new byte[0];
        }
        else {
            try {
                bytes = value.getBytes(charset);
            } catch (UnsupportedEncodingException e) {
                logger.error( "StringCodecForLengthInBytes", e);
            }
        }
        
        int fixedLength = desc.getFixedLength();
        if ( fixedLength >= 0 ) {
            while ( bytes.length < fixedLength  ) {
                // fill pending with 0
                bytes = ArrayUtils.add(bytes, (byte)0);
            }
            while ( bytes.length > fixedLength  ) {
                // fill pending with 0
                bytes = ArrayUtils.remove(bytes, bytes.length-1);
            }
            
        }
        
        return  bytes;
    }

    public Class<?>[] getFieldType() {
        return new Class<?>[]{ String.class };
    }
}
