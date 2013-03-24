/**
 * 
 */
package com.skymobi.cac.maopao.passport.android.bean.tlv.decode.decoders;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.apache.commons.lang.ArrayUtils;

import com.skymobi.cac.maopao.passport.android.bean.tlv.annotation.TLVAttribute;
import com.skymobi.cac.maopao.passport.android.bean.tlv.decode.TLVDecodeContext;
import com.skymobi.cac.maopao.passport.android.bean.tlv.decode.TLVDecodeContextFactory;
import com.skymobi.cac.maopao.passport.android.bean.tlv.decode.TLVDecoder;
import com.skymobi.cac.maopao.passport.android.bean.tlv.decode.TLVDecoderOfBean;
import com.skymobi.cac.maopao.passport.android.bean.tlv.meta.TLVFieldMetainfo;
import com.skymobi.cac.maopao.passport.android.bean.util.meta.Int2TypeMetainfo;
import com.skymobi.cac.maopao.passport.factory.DecodeContextFactory;



/**
 * @author hp
 *
 */
public class BeanTLVDecoder implements TLVDecoderOfBean {

   // private static final Logger logger = 
    //	LoggerFactory.getLogger(BeanTLVDecoder.class);
    
  //  private	TLVDecodeContextFactory	decodeContextFactory;
    

	/* (non-Javadoc)
	 * @see com.skymobi.bean.tlv.decode.decoders.TLVDecoderOfBean#getDecodeContextFactory()
	 */
	public TLVDecodeContextFactory getDecodeContextFactory() {
		return DecodeContextFactory.getInstance();
	}

	//public void setDecodeContextFactory(TLVDecodeContextFactory decodeContextFactory) {
	//	this.decodeContextFactory = decodeContextFactory;
	//}

	/* (non-Javadoc)
	 * @see com.skymobi.bean.tlv.TLVDecoder#decode(int, byte[], com.skymobi.bean.tlv.TLVDecodeContext)
	 */
	/* (non-Javadoc)
	 * @see com.skymobi.bean.tlv.decode.decoders.TLVDecoderOfBean#decode(int, byte[], com.skymobi.bean.tlv.decode.TLVDecodeContext)
	 */
	@SuppressWarnings("unchecked")
	public Object decode(int tlvLength, byte[] tlvValue, TLVDecodeContext ctx) {
		
		if ( tlvValue.length < tlvLength ) {
			throw new RuntimeException("BeanTLVDecoder, too few bytes.");
		}
		
		Class<?> valueClazz = ctx.getValueType();
		if ( null == valueClazz ) {
			throw new RuntimeException("BeanTLVDecoder, invalid value type.");
		}
		
		Object target = null;
		try {
			target = valueClazz.newInstance();
		} catch (InstantiationException e) {
			//logger.error("BeanTLVDecoder:", e);
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		if (null == target) {
			throw new RuntimeException("BeanTLVDecoder, can not create instance.");
		}
		
		int offset = 0;
		while ( offset + 8 <= tlvLength ) {
			
			byte[] tagBytes = ArrayUtils.subarray(tlvValue, offset, offset + 4);
			int tag = ctx.getNumberCodec().bytes2Int(tagBytes, 4);
			
			offset += 4;
			byte[] lenBytes = ArrayUtils.subarray(tlvValue, offset, offset + 4);
			int len = ctx.getNumberCodec().bytes2Int(lenBytes, 4);
			offset += 4;
			
			Int2TypeMetainfo typeMetainfo = ctx.getTypeMetainfo();
			Class<?> type = typeMetainfo.find(tag);
			if ( null == type ) {
				// unknow tag, just ignore
				//logger.info("unknow tag:"+tag+", just ignore.");
				offset += len;
				continue;
			}
			
			TLVDecoder decoder = ctx.getDecoderRepository().getDecoderOf(type);
			if ( null == decoder ) {
				// unknow tag, just ignore
				//logger.info("unknow decoder for [tag]:"+tag+",[type]:"+type+" just ignore.");
				offset += len;
				continue;
			}
			
			byte[] valueBytes = ArrayUtils.subarray(tlvValue, offset, offset + len);
			offset += len;
			
			TLVFieldMetainfo fieldMetainfo = ctx.getFieldMetainfo();
			Field field = fieldMetainfo.get(tag);
			Object bean = null;
            try {
                bean = decoder.decode(len, valueBytes,
                		getDecodeContextFactory().createDecodeContext(type, field));
            }catch (RuntimeException e){
                //logger.error("Decode tag {}({}) error!",new String(Hex.encodeHex(tagBytes)).toUpperCase(),tag);
                throw e;
            }

            TLVAttribute param  = field.getAnnotation(TLVAttribute.class);
            Class<?>	fieldType = param.type();
            if ( TLVAttribute.class.equals(fieldType)) {
            	fieldType = field.getType();
            }
			if ( fieldType.equals(ArrayList.class)) {
				try {
					ArrayList<Object> list = (ArrayList<Object>)field.get(target);
					if ( null == list ) {
						list = new ArrayList<Object>();
						field.set(target, list);
					}
					list.add(bean);
				} catch (IllegalArgumentException e) {
					//logger.error("BeanTLVDecoder:", e);
				} catch (IllegalAccessException e) {
				//	logger.error("BeanTLVDecoder:", e);
				}
			}
			else {
				try {
					field.set(target, bean);
				} catch (IllegalArgumentException e) {
					//logger.error("BeanTLVDecoder:", e);
				} catch (IllegalAccessException e) {
					//logger.error("BeanTLVDecoder:", e);
				}
			}
		}
		
		return target;
	}

}
