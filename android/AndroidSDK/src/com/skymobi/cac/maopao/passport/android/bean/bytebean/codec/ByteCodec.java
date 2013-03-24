/**
 * 
 */
package com.skymobi.cac.maopao.passport.android.bean.bytebean.codec;

import org.apache.commons.lang.ArrayUtils;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.core.ByteFieldCodec;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.core.DecContext;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.core.DecResult;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.core.EncContext;



/**
 * @author isdom
 *
 */
public class ByteCodec extends AbstractPrimitiveCodec implements ByteFieldCodec {

    //private static final Logger logger = LoggerFactory.getLogger(ByteCodec.class);
    
    public DecResult decode(DecContext ctx) {
        byte[] bytes = ctx.getDecBytes();
        if ( bytes.length < 1 ) {
            String  errmsg = "ByteCodec: not enough bytes for decode, need [1], actually [" 
                                + bytes.length +"].";
            if ( null != ctx.getField() ) {
                errmsg += "/ cause field is [" + ctx.getField() + "]";
            }
            //logger.error(errmsg);
            throw   new RuntimeException(errmsg);
            //return makeDecResult( (byte)0, bytes );
        }
        return  new DecResult( bytes[0], ArrayUtils.subarray(bytes, 1, bytes.length));
    }

    public byte[] encode(EncContext ctx) {
        return  new byte[]{ ((Byte)ctx.getEncObject()).byteValue() };
    }

    public Class<?>[] getFieldType() {
        return new Class<?>[]{ byte.class, Byte.class };
    }
}
