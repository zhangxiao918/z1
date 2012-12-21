/**
 * 
 */
package com.skymobi.android.bean.kv;

import java.lang.reflect.Field;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skymobi.android.bean.kv.annotation.KeyValueAttribute;
import com.skymobi.android.util.FieldUtils;
import com.skymobi.android.util.SimpleCache;

/**
 * @author hp
 *
 */
public class KVUtils {
	
    @SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(KVUtils.class);
    
	private static SimpleCache<Class<?>, Field[]>	kvFieldsCache = 
		new SimpleCache<Class<?>, Field[]>();
		
	public static Field[]	getKVFieldsOf(final Class<?> kvType) {
		return kvFieldsCache.get(kvType, new Callable<Field[]>() {

				public Field[] call() throws Exception {
					return FieldUtils.getAnnotationFieldsOf(kvType, KeyValueAttribute.class);
			}});
	}
}
