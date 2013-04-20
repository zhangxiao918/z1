/**
 * 
 */
package com.skymobi.cac.maopao.passport.android.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.commons.lang.ArrayUtils;

/**
 * @author hp
 *
 */
public class FieldUtils {
	
    //private static final Logger logger = LoggerFactory.getLogger(FieldUtils.class);
    
    static public Class<?> getComponentClass(Field field) {
        if ( null == field ) {
            String errmsg = "FieldUtils: field is null, can't get compoment class.";
           // logger.error(errmsg);
            throw new RuntimeException(errmsg);
        }
        Type type = field.getGenericType();
        
        if ( null == type || !(type instanceof ParameterizedType) ) {
            String errmsg = "FieldUtils: getGenericType invalid, can't get compoment class."
                +"/ cause field is [" + field + "]";
            //logger.error(errmsg);
            throw new RuntimeException(errmsg);
        }
        ParameterizedType parameterizedType  = (ParameterizedType)type;
        Class<?> clazz = (Class<?>)parameterizedType.getActualTypeArguments()[0];
        return  clazz;
    }
    
    static public Field[] getAllFieldsOfClass(Class<?> cls) {
        Field[] fields = new Field[0];
        
        Class<?> itr = cls;
        while ( (null != itr) && !itr.equals(Object.class)) {
            fields = (Field[]) ArrayUtils.addAll(itr.getDeclaredFields(), fields);
            itr = itr.getSuperclass();
        }
        
        return	fields;
    }
    
	static public Field[]	getAnnotationFieldsOf(Class<?> cls, 
			Class<? extends Annotation> annotationClass) {
        Field[] fields = new Field[0];
        
        Class<?> itr = cls;
        while ( (null != itr) && !itr.equals(Object.class)) {
            fields = (Field[]) ArrayUtils.addAll(itr.getDeclaredFields(), fields);
            itr = itr.getSuperclass();
        }

        int idx = 0;
        for ( Field field : fields ) {
            if ( null != field.getAnnotation(annotationClass) ) {
            	idx++;
            }
        }
        
        Field[] ret = new Field[idx];
        idx = 0;
        for ( Field field : fields ) {
            field.setAccessible(true);
            if ( null != field.getAnnotation(annotationClass) ) {
            	ret[idx++] = field;
            }
        }
        
        return	ret;
	}
}
