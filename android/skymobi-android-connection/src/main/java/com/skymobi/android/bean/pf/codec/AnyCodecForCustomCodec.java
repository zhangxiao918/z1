/**
 * 
 */
package com.skymobi.android.bean.pf.codec;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skymobi.android.bean.bytebean.codec.AbstractCategoryCodec;
import com.skymobi.android.bean.bytebean.core.ByteFieldCodec;
import com.skymobi.android.bean.bytebean.core.ByteFieldDesc;
import com.skymobi.android.bean.bytebean.core.DecContext;
import com.skymobi.android.bean.bytebean.core.DecResult;
import com.skymobi.android.bean.bytebean.core.EncContext;
import com.skymobi.android.bean.bytebean.core.FieldCodecCategory;
import com.skymobi.android.util.SimpleCache;

/**
 * @author isdom
 *
 */
public class AnyCodecForCustomCodec extends AbstractCategoryCodec 
	implements ByteFieldCodec {

	private SimpleCache<Class<? extends ByteFieldCodec>, ByteFieldCodec>	codecCache = 
		new SimpleCache<Class<? extends ByteFieldCodec>, ByteFieldCodec>();
	
	private static final Logger logger = 
		LoggerFactory.getLogger(AnyCodecForCustomCodec.class);
	
    public  AnyCodecForCustomCodec() {
    }
    
    private ByteFieldCodec getCustomCodec(ByteFieldDesc fieldDesc) {
    	
    	if ( null != fieldDesc ) {
	        final Class<? extends ByteFieldCodec> codecCls = fieldDesc.getCustomCodec();
	        
	        if ( null != codecCls ) {
	        	return	codecCache.get(codecCls, new Callable<ByteFieldCodec>(){
	
					public ByteFieldCodec call() throws Exception {
						return codecCls.newInstance();
					}});
	        }
    	}
        
        return	null;
    }
    
    public DecResult decode(DecContext ctx) {
        Class<?>  clazz = ctx.getDecClass();

        ByteFieldCodec codec = getCustomCodec(ctx.getFieldDesc() );
        
        if ( null == codec ) {
        	codec = ctx.getCodecOf(clazz);
        }
        
        if ( null == codec ) {
            if ( clazz.isArray() ) {
                codec = ctx.getCodecOf(FieldCodecCategory.ARRAY);
            }
            else {
//            	if ( beanClass.isAssignableFrom( clazz ) ) {
                codec = ctx.getCodecOf(FieldCodecCategory.BEAN);
//              if (logger.isDebugEnabled()) {
//                  logger.debug( "bytes2Any: is ByteBean's subclass, using bytes2Compound");
//              }
            }
        }
        
        if ( null != codec) {
            return  codec.decode( ctx );
        }
        else {
        	logger.error("decode : can nout found matched codec for field [" + ctx.getField() + "].");
        }
        return  new DecResult( null, ctx.getDecBytes());
    }

    public byte[] encode(EncContext ctx) {
        Class<?>  clazz = ctx.getEncClass();

        ByteFieldCodec codec = getCustomCodec(ctx.getFieldDesc() );
        
        if ( null == codec ) {
        	codec = ctx.getCodecOf(clazz);
        }
        
        if ( null == codec ) {
            if ( clazz.isArray() ) {
                codec = ctx.getCodecOf(FieldCodecCategory.ARRAY);
            }
            else {
//            	if ( beanClass.isAssignableFrom( clazz ) ) {
                //  clazz is derive from ByteBean
                codec = ctx.getCodecOf( FieldCodecCategory.BEAN);
//                if ( logger.isDebugEnabled() ) {
//                    logger.debug( "any2Bytes: is ByteBean's subclass, using compound2Bytes");
//                }
            }
        }
        
        if ( null != codec ) {
            return  codec.encode(ctx );
        }
        else {
        	logger.error("encode : can nout found matched codec for field [" + ctx.getField() + "].");
        }
        
        return  new byte[0];
    }

    public FieldCodecCategory getCategory() {
        return FieldCodecCategory.ANY;
    }
}
