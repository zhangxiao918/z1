/**
 * 
 */
package com.skymobi.android.bean.bytebean.codec;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skymobi.android.bean.bytebean.core.ByteFieldCodec;
import com.skymobi.android.bean.bytebean.core.ByteFieldDesc;
import com.skymobi.android.bean.bytebean.core.DecContext;
import com.skymobi.android.bean.bytebean.core.DecResult;
import com.skymobi.android.bean.bytebean.core.EncContext;


/**
 * @author isdom
 *
 */
public class ByteArrayCodec extends AbstractPrimitiveCodec implements ByteFieldCodec {

    private static final Logger logger = LoggerFactory.getLogger(ByteArrayCodec.class);
    
    public DecResult decode(DecContext ctx) {
        byte[] bytes = ctx.getDecBytes();
        final   ByteFieldDesc desc = ctx.getFieldDesc();
        int     arrayLength = 0;
        
        if ( null == desc ) {
            //  deal with array
            //  first get a short array dimension
//            Object result = bytes2Short.transform(
//                    fillDecParams( params, bytes, short.class, null, 
//                            getDecNumberCodec(input),getDecOwner(input) ));
//            arrayLength = (Short)getDecResultValue(result);
//            bytes = getDecResultRemainBytes(result);
            throw   new RuntimeException("invalid bytearray env.");
        }
        else if ( desc.hasLength() ) {
            //  已经有字段记录数组长度了
            arrayLength = desc.getLength( ctx.getDecOwner());
        }
        else if ( desc.getFixedLength() > 0 ) {
            arrayLength = desc.getFixedLength();
        }
        else {
            throw   new RuntimeException("invalid bytearray env.");
        }
        
        if ( bytes.length < arrayLength ) {
            String  errmsg = "ByteArrayCodec: not enough bytes for decode, need [" + arrayLength 
                            + "], actually [" + bytes.length +"].";
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
        ByteFieldDesc   desc = ctx.getFieldDesc();
        byte[]          bytes = null;
        
        if ( null == desc ) {
//            bytes = (byte[])short2Bytes.transform(
//                    fillEncParams( params, (short)array.length, short.class, null, getEncNumberCodec(input)) );
            throw   new RuntimeException("invalid bytearray env.");
        }
        else if ( !desc.hasLength() && desc.getFixedLength() < 0 ) {
            throw   new RuntimeException("invalid bytearray env.");
        }
        else {
            //  已经存在字段记录数组长度
        }
        
        return  ArrayUtils.addAll( bytes, array);
    }

    public Class<?>[] getFieldType() {
        return new Class<?>[]{ byte[].class };
    }
}
