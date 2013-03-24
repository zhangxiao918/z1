/**
 * 
 */
package com.skymobi.cac.maopao.passport.android.bean;

import java.lang.reflect.Field;
import java.util.concurrent.Callable;

import com.skymobi.cac.maopao.passport.android.bean.annotation.BeanAttribute;
import com.skymobi.cac.maopao.passport.android.util.FieldUtils;
import com.skymobi.cac.maopao.passport.android.util.SimpleCache;



/**
 * @author hp
 *
 */
public class BeanUtils {
	
  //  private static final Logger logger = LoggerFactory.getLogger(BeanUtils.class);
    
	private static SimpleCache<Class<?>, Field[]>	beanFieldsCache = 
		new SimpleCache<Class<?>, Field[]>();
		
	public static Field[]	getBeanFieldsOf(final Class<?> beanType) {
		return beanFieldsCache.get(beanType, new Callable<Field[]>() {

				public Field[] call() throws Exception {
					return FieldUtils.getAnnotationFieldsOf(beanType, BeanAttribute.class);
			}});
	}
	
	public static void checkRequired(Object bean) {
		if ( null == bean ) {
			return;
		}
		
		Field[] fields = getBeanFieldsOf(bean.getClass());
		for ( Field field : fields ) {
			BeanAttribute attr = 
				field.getAnnotation(BeanAttribute.class);
			
			field.setAccessible(true);
			
			if ( null != attr && attr.required() && !field.getType().isPrimitive() ) {
				Object value = null;
				try {
					value = field.get(bean);
				} catch (IllegalArgumentException e) {
					//logger.error("checkRequired:", e);
				} catch (IllegalAccessException e) {
					//logger.error("checkRequired:", e);
				}
				if ( null == value ) {
					throw new RuntimeException("required field missing [" + field + "]");
				}
			}
		}
	}
}
