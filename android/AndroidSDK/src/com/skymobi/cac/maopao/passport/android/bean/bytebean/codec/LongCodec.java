/**
 * 
 */
package com.skymobi.cac.maopao.passport.android.bean.bytebean.codec;

import org.apache.commons.lang.ArrayUtils;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.core.ByteFieldCodec;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.core.DecContext;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.core.DecResult;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.core.EncContext;
import com.skymobi.cac.maopao.passport.android.util.NumberCodec;



/**
 * @author isdom
 *
 */
public class LongCodec extends AbstractPrimitiveCodec implements ByteFieldCodec {

    //private static final Logger logger = LoggerFactory.getLogger(LongCodec.class);
    
    public DecResult decode(DecContext ctx) {
        byte[] bytes    = ctx.getDecBytes();
        int byteLength  = ctx.getByteSize();
        NumberCodec numberCodec = ctx.getNumberCodec();
        
        if ( byteLength > bytes.length ) {
            String  errmsg = "LongCodec: not enough bytes for decode, need [" + byteLength 
                            + "], actually [" + bytes.length +"].";
            if ( null != ctx.getField() ) {
                errmsg += "/ cause field is [" + ctx.getField() + "]";
            }
            //logger.error(errmsg);
            throw   new RuntimeException(errmsg);
            //return makeDecResult( retLong, bytes );
        }

        return  new DecResult( numberCodec.bytes2Long(bytes, byteLength), 
                    ArrayUtils.subarray(bytes, byteLength, bytes.length));
    }

    public byte[] encode(EncContext ctx) {
        long enc = ((Long)ctx.getEncObject()).longValue();
        int byteLength = ctx.getByteSize();
        NumberCodec numberCodec = ctx.getNumberCodec();
        
        return  numberCodec.long2Bytes(enc, byteLength);
    }

    public Class<?>[] getFieldType() {
        return new Class<?>[]{long.class, Long.class };
    }
}
