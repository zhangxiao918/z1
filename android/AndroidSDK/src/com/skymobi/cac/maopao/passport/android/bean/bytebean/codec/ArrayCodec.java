/**
 * 
 */
package com.skymobi.cac.maopao.passport.android.bean.bytebean.codec;

import java.lang.reflect.Array;

import org.apache.commons.lang.ArrayUtils;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.core.ByteFieldCodec;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.core.ByteFieldDesc;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.core.DecContext;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.core.DecContextFactory;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.core.DecResult;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.core.EncContext;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.core.EncContextFactory;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.core.FieldCodecCategory;



/**
 * @author isdom
 *
 */
public class ArrayCodec extends AbstractCategoryCodec implements ByteFieldCodec {

//	private	DecContextFactory	decContextFactory;
//	private	EncContextFactory	encContextFactory;
	
    public DecResult decode(DecContext ctx) {
        byte[] bytes        = ctx.getDecBytes();
        Class<?> fieldClass = ctx.getDecClass();
        Class<?> compomentClass = fieldClass.getComponentType();
        final   ByteFieldDesc desc = ctx.getFieldDesc();
        int     arrayLength = 0;
        
        if ( null == desc || !desc.hasLength() ) {
            //  deal with array
            //  first get a short array dimension
//            Object result = bytes2Short.transform(
//                    fillDecParams( params, bytes, short.class, null, 
//                            getDecNumberCodec(input),getDecOwner(input) ));
//            arrayLength = (Short)getDecResultValue(result);
//            bytes = getDecResultRemainBytes(result);
            throw   new RuntimeException("invalid array env.");
        }
        else {
            //  宸茬粡链夊瓧娈佃褰曟暟缁勯昵搴︿简
            arrayLength = desc.getLength( ctx.getDecOwner() );
        }

        Object array = null;
        if ( arrayLength > 0 ) {
//            if (logger.isDebugEnabled()) {
//                logger.debug( "bytes2Array: decode Array length [" +  arrayLength +"]" );
//            }
            array = Array.newInstance(compomentClass, arrayLength);
            ByteFieldCodec  anyCodec = ctx.getCodecOf(FieldCodecCategory.ANY);
            
            for ( int idx = 0; idx < arrayLength; idx++ ) {
                DecResult ret = anyCodec.decode(
                		ctx.getDecContextFactory().createDecContext(
                				bytes, compomentClass, ctx.getDecOwner(), null) );
                Array.set(array, idx, ret.getValue() );
                bytes = ret.getRemainBytes();
            }
        }
        
        return  new DecResult( array, bytes);
    }

    public byte[] encode(EncContext ctx) {
        Object  array       = ctx.getEncObject();
        Class<?> fieldClass = ctx.getEncClass();
        Class<?> compomentClass = fieldClass.getComponentType();
        int arrayLength = ( null != array ? Array.getLength(array) : 0);

        ByteFieldDesc   desc = ctx.getFieldDesc();
        byte[]          bytes = null;
        
        if ( null == desc || !desc.hasLength() ) {
//            bytes = (byte[])short2Bytes.transform(
//                    fillEncParams( params, (short)arrayLength, short.class, null,
//                            getEncNumberCodec(input)
//                            ) );
            throw   new RuntimeException("invalid array env.");
        }
        else {
            // 
        }
        ByteFieldCodec  anyCodec = ctx.getCodecOf(FieldCodecCategory.ANY);
        
        for ( int idx = 0; idx < arrayLength; idx++) {
            bytes = ArrayUtils.addAll( bytes, 
                            anyCodec.encode(
                            	ctx.getEncContextFactory().createEncContext(
                        				Array.get(array, idx), compomentClass, null) ) );
        }
        return  bytes;
    }

    public FieldCodecCategory getCategory() {
        return FieldCodecCategory.ARRAY;
    }

	public DecContextFactory getDecContextFactory() {
		return null;	//decContextFactory;
	}

	public void setDecContextFactory(DecContextFactory decContextFactory) {
//		this.decContextFactory = decContextFactory;
	}

	public EncContextFactory getEncContextFactory() {
		return null;	//encContextFactory;
	}

	public void setEncContextFactory(EncContextFactory encContextFactory) {
//		this.encContextFactory = encContextFactory;
	}
}
