/**
 * 
 */
package com.skymobi.cac.maopao.passport.android.bean.tlv.encode.encoders;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.skymobi.cac.maopao.passport.android.bean.tlv.annotation.TLVAttribute;
import com.skymobi.cac.maopao.passport.android.bean.tlv.encode.TLVEncodeContext;
import com.skymobi.cac.maopao.passport.android.bean.tlv.encode.TLVEncodeContextFactory;
import com.skymobi.cac.maopao.passport.android.bean.tlv.encode.TLVEncoder;
import com.skymobi.cac.maopao.passport.android.bean.tlv.encode.TLVEncoderOfBean;
import com.skymobi.cac.maopao.passport.android.bean.tlv.meta.TLVCodecUtils;
import com.skymobi.cac.maopao.passport.android.util.ByteUtils;
import com.skymobi.cac.maopao.passport.android.util.FieldUtils;
import com.skymobi.cac.maopao.passport.factory.EncodeContextFactory;



/**
 * @author hp
 *
 */
public class BeanTLVEncoder implements TLVEncoderOfBean {

   // private static final Logger logger = 
    //	LoggerFactory.getLogger(BeanTLVEncoder.class);
    
    //private	TLVEncodeContextFactory	encodeContextFactory;
    
	/* (non-Javadoc)
	 * @see com.skymobi.bean.tlv.encode.encoders.TLVEncoderOfBean#encode(java.lang.Object, com.skymobi.bean.tlv.encode.TLVEncodeContext)
	 */
	@SuppressWarnings("unchecked")
	public List<byte[]> encode(Object tlvBean, TLVEncodeContext ctx) {
        if ( null == tlvBean ) {
            throw new RuntimeException("null == tlvBean.");
        }

        List<byte[]> ret = new ArrayList<byte[]>();
        Field[] fields = TLVCodecUtils.getTLVFieldsOf(tlvBean.getClass());
        
        for ( Field field : fields ) {
            TLVAttribute param  = field.getAnnotation(TLVAttribute.class);
            
            if ( null == param ) {
                //  not TLVAttribute
                continue;
            }
            
            field.setAccessible(true);
            Object src = null;
            try {
                src = field.get(tlvBean);
            } catch (IllegalArgumentException e) {
            	//logger.error("transform:", e);
            } catch (IllegalAccessException e) {
            	//logger.error("transform:", e);
            }
            if ( null == src ) {
                continue;
            }
            
            Class<?>	type = param.type();
            if ( TLVAttribute.class.equals(type)) {
            	type = src.getClass();
            }
            
            if ( type.equals(ArrayList.class)) {
            	Class<?> componentType = FieldUtils.getComponentClass(field);
                TLVEncoder encoder = ctx.getEncoderRepository().getEncoderOf(componentType);
                if ( null == encoder ) {
                	//logger.error("field[" + field + "]/"+componentType.getSimpleName()+ " can not found encoder, ignore");
                    continue;
                }
                ArrayList<Object> list = (ArrayList<Object>)src;
                for ( Object component : list ) {
                    List<byte[]> dest = encoder.encode(component, 
                    		getEncodeContextFactory().createEncodeContext(componentType, field) );
                    //	tag
                    ret.add( ctx.getNumberCodec().int2Bytes(param.tag(), 4) );
                    
                    //	len
                    ret.add( ctx.getNumberCodec().int2Bytes(ByteUtils.totalByteSizeOf(dest), 4) );
                    
                    ret.addAll(dest);
                }
            }
            else {
	            TLVEncoder encoder = ctx.getEncoderRepository().getEncoderOf(type);
	            if ( null == encoder ) {
	            	//logger.error("field[" + field + "] can not found encoder, ignore");
	                continue;
	            }
	            //System.out.println("encodeContextFactory is null:"+(encodeContextFactory== null));
	            List<byte[]> dest = encoder.encode(src, 
	            		getEncodeContextFactory().createEncodeContext(type, field) );
	
	            //	tag
	            ret.add( ctx.getNumberCodec().int2Bytes(param.tag(), 4) );
	            
	            //	len
	            ret.add( ctx.getNumberCodec().int2Bytes(ByteUtils.totalByteSizeOf(dest), 4) );
	            
	            ret.addAll(dest);
            }
        }
        
        return ret;
	}

	/* (non-Javadoc)
	 * @see com.skymobi.bean.tlv.encode.encoders.TLVEncoderOfBean#getEncodeContextFactory()
	 */
	public TLVEncodeContextFactory getEncodeContextFactory() {
		return EncodeContextFactory.getInstance();
	}



}
