/**
 * 
 */
package com.skymobi.android.bean.xip.codec;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class LenListCodec implements ByteFieldCodec {
    
	private static final Logger logger = LoggerFactory.getLogger(LenListCodec.class);
    
//	private	DecContextFactory	decContextFactory;
//	private	EncContextFactory	encContextFactory;
	
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
	
    public Class<?> getCompomentClass(Field field) {
        if ( null == field ) {
            String errmsg = "LenListCodec: field is null, can't get compoment class.";
            logger.error(errmsg);
            throw new RuntimeException(errmsg);
        }
        Type type = field.getGenericType();
        
        if ( null == type || !(type instanceof ParameterizedType) ) {
            String errmsg = "LenListCodec: getGenericType invalid, can't get compoment class."
                +"/ cause field is [" + field + "]";
            logger.error(errmsg);
            throw new RuntimeException(errmsg);
        }
        ParameterizedType parameterizedType  = (ParameterizedType)type;
        Class<?> clazz = (Class<?>)parameterizedType.getActualTypeArguments()[0];
        return  clazz;
    }
    
    public DecResult decode(DecContext ctx) {
        DecResult ret = ctx.getCodecOf(short.class).decode(
        		ctx.getDecContextFactory().createDecContext(
        				ctx.getDecBytes(), short.class, ctx.getDecOwner(), null) );
        short   listLength = (Short)ret.getValue();
        byte[]  bytes = ret.getRemainBytes();
        Class<?>    compomentClass = getCompomentClass(ctx.getField());
        
        ArrayList<Object> list = null;
        if ( listLength > 0 ) {
//            if (logger.isDebugEnabled()) {
//                logger.debug( "bytes2Array: decode Array length [" +  arrayLength +"]" );
//            }
            list = new ArrayList<Object>(listLength);
            ByteFieldCodec  anyCodec = ctx.getCodecOf(FieldCodecCategory.ANY);
            
            for ( int idx = 0; idx < listLength; idx++ ) {
                ret = anyCodec.decode(
                		ctx.getDecContextFactory().createDecContext(
                				bytes, compomentClass, ctx.getDecOwner(), null) );
                list.add(ret.getValue());
                bytes = ret.getRemainBytes();
            }
        }
        
        return  new DecResult( list, bytes);
    }

    @SuppressWarnings("unchecked")
    public byte[] encode(EncContext ctx) {
        ArrayList<Object>   list       = (ArrayList<Object>)ctx.getEncObject();
        int         listLength = ( null != list ? list.size() : 0);
        Class<?>    compomentClass = getCompomentClass(ctx.getField());
        
        byte[] bytes = ctx.getCodecOf(short.class).encode(
        		ctx.getEncContextFactory().createEncContext(
        				(short)listLength, short.class, null) );

        if ( listLength > 0 ) {
            
            ByteFieldCodec  anyCodec = ctx.getCodecOf(FieldCodecCategory.ANY);
            
            for ( int idx = 0; idx < listLength; idx++) {
                bytes = ArrayUtils.addAll( bytes, 
                                anyCodec.encode(
                                	ctx.getEncContextFactory().createEncContext(
                            			list.get(idx), compomentClass, null) ) );
            }
        }
        return  bytes;
    }

    public FieldCodecCategory getCategory() {
        return null;
    }

    public Class<?>[] getFieldType() {
        return new Class<?>[]{ ArrayList.class };
    }
}
