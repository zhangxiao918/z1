/**
 * 
 */
package com.skymobi.cac.maopao.passport.android.bean.bytebean.codec;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.core.ByteFieldCodec;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.core.DecContext;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.core.DecResult;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.core.EncContext;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.core.FieldCodecCategory;


/**
 * @author isdom
 *
 */
public class AnyCodec extends AbstractCategoryCodec implements ByteFieldCodec {

	//private static final Logger logger = LoggerFactory.getLogger(AnyCodec.class);
	
    @SuppressWarnings("unused")
	private Class<?>    beanClass;
    
    public  AnyCodec(Class<?> beanClass) {
        this.beanClass = beanClass;
    }
    
    public DecResult decode(DecContext ctx) {
        Class<?>  clazz = ctx.getDecClass();

        ByteFieldCodec codec = ctx.getCodecOf(clazz);
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
        	//logger.error("decode : can nout found matched codec for field [" + ctx.getField() + "].");
        }
        return  new DecResult( null, ctx.getDecBytes());
    }

    public byte[] encode(EncContext ctx) {
        Class<?>  clazz = ctx.getEncClass();

        ByteFieldCodec codec = ctx.getCodecOf( clazz );
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
        	//logger.error("encode : can nout found matched codec for field [" + ctx.getField() + "].");
        }
        
        return  new byte[0];
    }

    public FieldCodecCategory getCategory() {
        return FieldCodecCategory.ANY;
    }
}
