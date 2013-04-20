/**
 * 
 */
package com.skymobi.cac.maopao.passport.android.bean.tlv.meta;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;

import org.apache.commons.collections.Transformer;

import com.skymobi.cac.maopao.passport.android.bean.tlv.annotation.TLVAttribute;
import com.skymobi.cac.maopao.passport.android.bean.util.meta.BeanMetainfoUtils;
import com.skymobi.cac.maopao.passport.android.bean.util.meta.DefaultInt2TypeMetainfo;
import com.skymobi.cac.maopao.passport.android.bean.util.meta.Int2TypeMetainfo;
import com.skymobi.cac.maopao.passport.android.util.FieldUtils;
import com.skymobi.cac.maopao.passport.android.util.SimpleCache;



/**
 * @author hp
 *
 */
public class TLVCodecUtils {
   // @SuppressWarnings("unused")
	//private static final Logger logger = LoggerFactory
    //	.getLogger(TLVCodecUtils.class);
    
    private static final Transformer CLS2INT = new Transformer(){

		public Object transform(Object input) {
			Class<?> cls = (Class<?>)input;
			TLVAttribute attr = cls.getAnnotation(TLVAttribute.class);
			return null != attr ? attr.tag() : null;
		}};
		
	private static SimpleCache<Class<?>, Field[]>	tlvFieldsCache = 
		new SimpleCache<Class<?>, Field[]>();
	
	static public Field[]	getTLVFieldsOf(final Class<?> tlvType) {
		return tlvFieldsCache.get(tlvType, new Callable<Field[]>() {

				public Field[] call() throws Exception {
					return FieldUtils.getAnnotationFieldsOf(tlvType, TLVAttribute.class);
			}});
	}
	
	static public TLVFieldMetainfo createFieldMetainfo(Class<?> tlvType) {
		DefaultFieldMetainfo fieldMetainfo = new DefaultFieldMetainfo();
		
        Field[] fields = getTLVFieldsOf(tlvType);
        
        for ( Field field : fields ) {
            TLVAttribute param  = field.getAnnotation(TLVAttribute.class);
            fieldMetainfo.add(param.tag(), field);
        }
        
        return	fieldMetainfo;
	}
	
	static public TLVFieldMetainfo chainFieldMetainfo(
			final TLVFieldMetainfo first, final TLVFieldMetainfo second) {
        return	new TLVFieldMetainfo() {

			public Field get(int tag) {
				Field field = first.get(tag);
				if ( null != field ) {
					return	field;
				}
				
				return second.get(tag);
			}};
	}
	
	static public Int2TypeMetainfo createTypeMetainfo(Class<?> tlvType) {
		DefaultInt2TypeMetainfo typeMetainfo = new DefaultInt2TypeMetainfo();
		
        Field[] fields = getTLVFieldsOf(tlvType);
        
        for ( Field field : fields ) {
            TLVAttribute param  = field.getAnnotation(TLVAttribute.class);
            
            Class<?> type = param.type();
            if ( type.equals(TLVAttribute.class) ) {
            	type = field.getType();
            	if ( type.equals(ArrayList.class) ) {
            		type = FieldUtils.getComponentClass(field);
            	}
            }
            typeMetainfo.add(param.tag(), type);
        }
        
        return	typeMetainfo;
	}
	
//	static public TLVTypeMetainfo chainTypeMetainfo(
//			final TLVTypeMetainfo first, final TLVTypeMetainfo second) {
//        return	new TLVTypeMetainfo() {
//
//			public Class<?> find(int tag) {
//				Class<?> cls = first.find(tag);
//				if ( null != cls ) {
//					return	cls;
//				}
//				
//				return second.find(tag);
//			}};
//	}
	
	static public Int2TypeMetainfo createTopmostTypeMetainfo(Collection<String> packages) {
    	return	BeanMetainfoUtils.createTypeMetainfo(packages, CLS2INT );
	}
	
	static public Int2TypeMetainfo createTopmostTypeMetainfoByClasses(Collection<Class<?>> clazzes) {
    	return	BeanMetainfoUtils.createTypeMetainfoByClasses(clazzes, CLS2INT );
	}
}
