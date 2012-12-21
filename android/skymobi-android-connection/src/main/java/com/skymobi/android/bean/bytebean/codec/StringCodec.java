/**
 * 
 */
package com.skymobi.android.bean.bytebean.codec;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class StringCodec extends AbstractPrimitiveCodec implements ByteFieldCodec {
    private static final Logger logger = LoggerFactory.getLogger(StringCodec.class);

    public DecResult decode(DecContext ctx) {
        byte[] bytes = ctx.getDecBytes();
        ByteFieldDesc   desc = ctx.getFieldDesc();
        if ( null == desc ) {
            throw   new RuntimeException("StringCodec: ByteFieldDesc is null");
        }
        
        int length = desc.getFixedLength();
        
        //锟教讹拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷欠锟斤拷锟揭ワ拷锟斤拷址锟斤拷锟斤拷锟斤拷锟斤拷纸锟�
        boolean needFilter = false;
        if( length > 0 )needFilter = true;
        
        if ( length < 0 ) {
            length = desc.getStringLengthInBytes( ctx.getDecOwner() );
        }
        
        if ( length < 0 ) {
            throw   new RuntimeException("StringCodec: length < 0");
        }
        NumberCodec     numberCodec = ctx.getNumberCodec();

        String  charset = numberCodec.convertCharset( desc.getCharset() );
        
        String  ret = null;
        
        if ( length > bytes.length ) {
            String  errmsg = "StringCodec: not enough bytes for decode, need [" + length 
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
            	byte[] values = ArrayUtils.subarray(bytes, 0, length);
            	
            	//锟斤拷锟斤拷锟街凤拷锟斤拷锟斤拷锟斤拷锟街斤拷
            	if( needFilter && charset.startsWith("UTF-16") ){
                	int len = values.length;
                	for(;len>1;){
                		if((values[len-1] & 0xFF) == 0 && (values[len-2] & 0xFF) == 0){
                			len -= 2;
                		}else break;
                	}
                	values = ArrayUtils.subarray(values, 0, len);
                }

                ret = new String(values, charset);
            } catch (UnsupportedEncodingException e) {
                logger.error( "StringCodec", e);
            }
        }
        return new DecResult( ret, ArrayUtils.subarray(bytes, length, bytes.length));
    }

    public byte[] encode(EncContext ctx) {
        String  value = (String)ctx.getEncObject();
        ByteFieldDesc   desc = ctx.getFieldDesc();
        NumberCodec     numberCodec = ctx.getNumberCodec();
        
        if ( null == desc ) {
            throw   new RuntimeException("StringCodec: ByteFieldDesc is null");
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
                logger.error( "StringCodec", e);
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
