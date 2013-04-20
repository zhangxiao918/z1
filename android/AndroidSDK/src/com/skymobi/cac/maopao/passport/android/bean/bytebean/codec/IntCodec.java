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
public class IntCodec extends AbstractPrimitiveCodec implements ByteFieldCodec {

   // private static final Logger logger = LoggerFactory.getLogger(IntCodec.class);
    
    public DecResult decode(DecContext ctx) {
        byte[] bytes    = ctx.getDecBytes();
        int byteLength  = ctx.getByteSize();
        NumberCodec numberCodec = ctx.getNumberCodec();
        
        if ( byteLength > bytes.length ) {
            String  errmsg = "IntCodec: not enough bytes for decode, need [" + byteLength 
                                + "], actually [" + bytes.length +"].";
            if ( null != ctx.getField() ) {
                errmsg += "/ cause field is [" + ctx.getField() + "]";
            }
           // logger.error(errmsg);
            throw   new RuntimeException(errmsg);
            //return makeDecResult( retInt, bytes );
        }
        
        return  new DecResult( numberCodec.bytes2Int(bytes, byteLength), 
                    ArrayUtils.subarray(bytes, byteLength, bytes.length));
    }

    public byte[] encode(EncContext ctx) {
        int enc = ((Integer)ctx.getEncObject()).intValue();
        int byteLength = ctx.getByteSize();
        NumberCodec numberCodec = ctx.getNumberCodec();
        
        return  numberCodec.int2Bytes(enc, byteLength);
    }

    public Class<?>[] getFieldType() {
        return new Class<?>[]{ int.class, Integer.class };
    }
}
