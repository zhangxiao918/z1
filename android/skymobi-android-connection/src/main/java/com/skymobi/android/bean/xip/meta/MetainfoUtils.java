/**
 * 
 */
package com.skymobi.android.bean.xip.meta;

import java.util.Collection;

import org.apache.commons.collections.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skymobi.android.bean.util.meta.BeanMetainfoUtils;
import com.skymobi.android.bean.util.meta.Int2TypeMetainfo;
import com.skymobi.android.bean.xip.annotation.SaipSignalAnnotation;
import com.skymobi.android.bean.xip.annotation.SsipSignal;

/**
 * @author hp
 *
 */
public class MetainfoUtils {
	
    @SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(MetainfoUtils.class);
    
    private static final Transformer SSIP_CLS2INT = new Transformer(){

		public Object transform(Object input) {
			Class<?> cls = (Class<?>)input;
			SsipSignal attr = cls.getAnnotation(SsipSignal.class);
			return null != attr ? attr.messageCode() : null;
		}};
		
    private static final Transformer SAIP_CLS2INT = new Transformer(){

		public Object transform(Object input) {
			Class<?> cls = (Class<?>)input;
			SaipSignalAnnotation attr = cls.getAnnotation(SaipSignalAnnotation.class);
			return null != attr ? attr.messageCode() : null;
		}};
			
    static public Int2TypeMetainfo createSsipTypeMetainfo(Collection<String> packages) {
    	return	BeanMetainfoUtils.createTypeMetainfo(packages, SSIP_CLS2INT );
	}
    
    static public Int2TypeMetainfo createSsipTypeMetainfoByClasses(Collection<Class<?>> clazzes) {
    	return	BeanMetainfoUtils.createTypeMetainfoByClasses(clazzes, SSIP_CLS2INT );
	}

    static public Int2TypeMetainfo createSaipTypeMetainfo(Collection<String> packages) {
    	return	BeanMetainfoUtils.createTypeMetainfo(packages, SAIP_CLS2INT );
	}
}
