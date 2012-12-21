/**
 * 
 */
package com.skymobi.android.bean.pf.util;

import java.util.Collection;

import org.apache.commons.collections.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skymobi.android.bean.pf.annotation.PFSignal;
import com.skymobi.android.bean.util.meta.BeanMetainfoUtils;
import com.skymobi.android.bean.util.meta.Int2TypeMetainfo;

/**
 * @author hp
 *
 */
public class MetainfoUtils {
	
    @SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(MetainfoUtils.class);
    
    private static final Transformer CLS2INT = new Transformer(){

		public Object transform(Object input) {
			Class<?> cls = (Class<?>)input;
			PFSignal attr = cls.getAnnotation(PFSignal.class);
			return null != attr ? attr.messageCode() : null;
		}};
		
    static public Int2TypeMetainfo createPfTypeMetainfo(Collection<String> packages) {
    	return	BeanMetainfoUtils.createTypeMetainfo(packages, CLS2INT );
	}
    
    static public Int2TypeMetainfo createPfTypeMetainfoByClasses(Collection<Class<?>> clazzes) {
    	return	BeanMetainfoUtils.createTypeMetainfoByClasses(clazzes, CLS2INT );
	}

}
