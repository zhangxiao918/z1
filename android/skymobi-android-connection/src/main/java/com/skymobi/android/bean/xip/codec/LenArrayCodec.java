/**
 * 
 */
package com.skymobi.android.bean.xip.codec;

import java.lang.reflect.Array;

import org.apache.commons.lang.ArrayUtils;

import com.skymobi.android.bean.bytebean.core.ByteFieldCodec;
import com.skymobi.android.bean.bytebean.core.DecContext;
import com.skymobi.android.bean.bytebean.core.DecContextFactory;
import com.skymobi.android.bean.bytebean.core.DecResult;
import com.skymobi.android.bean.bytebean.core.EncContext;
import com.skymobi.android.bean.bytebean.core.EncContextFactory;
import com.skymobi.android.bean.bytebean.core.FieldCodecCategory;

/**
 * @author isdom
 *
 */
public class LenArrayCodec implements ByteFieldCodec {

//	private	DecContextFactory	decContextFactory;
//	private	EncContextFactory	encContextFactory;
//	
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

	public DecResult decode(DecContext ctx) {
        DecResult ret = ctx.getCodecOf(short.class).decode(
        		ctx.getDecContextFactory().createDecContext(
        				ctx.getDecBytes(), short.class, ctx.getDecOwner(), null) );
        short arrayLength = (Short)ret.getValue();
        byte[] bytes = ret.getRemainBytes();
        
        
        Object array = null;
        if ( arrayLength > 0 ) {
//            if (logger.isDebugEnabled()) {
//                logger.debug( "bytes2Array: decode Array length [" +  arrayLength +"]" );
//            }
            Class<?> fieldClass = ctx.getDecClass();
            Class<?> compomentClass = fieldClass.getComponentType();
            
            array = Array.newInstance(compomentClass, arrayLength);
            ByteFieldCodec  anyCodec = ctx.getCodecOf(FieldCodecCategory.ANY);
            
            for ( int idx = 0; idx < arrayLength; idx++ ) {
                ret = anyCodec.decode(
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
        int arrayLength = ( null != array ? Array.getLength(array) : 0);
        
        byte[] bytes = ctx.getCodecOf(short.class).encode(
        		ctx.getEncContextFactory().createEncContext(
        				(short)arrayLength, short.class, null) );

        if ( arrayLength > 0 ) {
            Class<?> fieldClass = ctx.getEncClass();
            Class<?> compomentClass = fieldClass.getComponentType();
            
            ByteFieldCodec  anyCodec = ctx.getCodecOf(FieldCodecCategory.ANY);
            
            for ( int idx = 0; idx < arrayLength; idx++) {
                bytes = ArrayUtils.addAll( bytes, 
                                anyCodec.encode(
                                		ctx.getEncContextFactory().createEncContext(
                            				Array.get(array, idx), compomentClass, null) ) );
            }
        }
        return  bytes;
    }

    public FieldCodecCategory getCategory() {
        return FieldCodecCategory.ARRAY;
    }

    public Class<?>[] getFieldType() {
        return null;
    }
}
