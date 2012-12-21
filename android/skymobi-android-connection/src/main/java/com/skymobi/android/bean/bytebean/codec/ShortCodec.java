/**
 * 
 */
package com.skymobi.android.bean.bytebean.codec;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skymobi.android.bean.bytebean.core.ByteFieldCodec;
import com.skymobi.android.bean.bytebean.core.DecContext;
import com.skymobi.android.bean.bytebean.core.DecResult;
import com.skymobi.android.bean.bytebean.core.EncContext;
import com.skymobi.android.util.NumberCodec;

/**
 * @author isdom
 *
 */
public class ShortCodec extends AbstractPrimitiveCodec implements ByteFieldCodec {

    private static final Logger logger = LoggerFactory.getLogger(ShortCodec.class);
    
    public DecResult decode(DecContext ctx) {
        byte[] bytes    = ctx.getDecBytes();
        int byteLength  = ctx.getByteSize();
        NumberCodec numberCodec = ctx.getNumberCodec();
        
        if ( byteLength > bytes.length ) {
            String  errmsg = "ShortCodec: not enough bytes for decode, need [" + byteLength 
                                + "], actually [" + bytes.length +"].";
            if ( null != ctx.getField() ) {
                errmsg += "/ cause field is [" + ctx.getField() + "]";
            }
            logger.error(errmsg);
            throw   new RuntimeException(errmsg);
            //return makeDecResult( retShort, bytes );
        }
        return  new DecResult( numberCodec.bytes2Short(bytes, byteLength), 
                    ArrayUtils.subarray(bytes, byteLength, bytes.length));
    }

    public byte[] encode(EncContext ctx) {
        short enc = ((Short)ctx.getEncObject()).shortValue();
        int byteLength = ctx.getByteSize();
        NumberCodec numberCodec = ctx.getNumberCodec();
        
        return  numberCodec.short2Bytes(enc, byteLength);
    }

    public Class<?>[] getFieldType() {
        return new Class<?>[]{ short.class, Short.class };
    }
}
