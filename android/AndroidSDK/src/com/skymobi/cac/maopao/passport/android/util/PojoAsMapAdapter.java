/**
 * 
 */
package com.skymobi.cac.maopao.passport.android.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Marvin.Ma
 *
 */
public class PojoAsMapAdapter implements Map<String, Object> {

    //private static final Logger logger = 
    //	LoggerFactory.getLogger(PojoAsMapAdapter.class);
	
	private	Object pojo;
	private Map<String, Field> fields = new HashMap<String, Field>();
	private	boolean readonly;
	
	public PojoAsMapAdapter(Object pojo, boolean readonly) {
		if ( null == pojo ) {
			throw new RuntimeException("pojo can't be null.");
		}
		
		this.pojo = pojo;
		this.readonly = readonly;
		
		Field[] fieldList = pojo.getClass().getDeclaredFields();
		for ( Field field : fieldList ) {
			field.setAccessible(true);
			fields.put(field.getName(), field);
		}
	}
	
	public void clear() {
		if ( readonly ) {
			throw new RuntimeException("can't invoke clear method when readonly mode");
		}
		
		for ( Field field : fields.values() ) {
			try {
				field.set(pojo, null);
			} catch (Exception e) {
				//logger.error("failed to invoke set null for field [{}] bcs of {}", field, e);
			} 
		}
	}

	public boolean containsKey(Object key) {
		return fields.containsKey(key);
	}

	public boolean containsValue(Object value) {
		for ( Field field : fields.values() ) {
			try {
				Object obj = field.get(pojo);
				if ( value == null ) {
					if ( obj == null ) {
						return	true;
					}
				}
				else {
					if ( value.equals(obj)) {
						return	true;
					}
				}
			} catch (Exception e) {
				//logger.error("failed to invoke get for field [{}] bcs of {}", field, e);
			}
		}
		
		return	false;
	}

	public Set<Map.Entry<String, Object>> entrySet() {
		Set<Map.Entry<String, Object>> ret = new HashSet<Map.Entry<String, Object>>();
		
		for ( String key : fields.keySet() ) {
			final String entryKey = key;
			ret.add(new Map.Entry<String, Object>() {

				public String getKey() {
					return entryKey;
				}

				public Object getValue() {
					return get(entryKey);
				}

				public Object setValue(Object value) {
					return put(entryKey, value);
				}} );
		}

		return	ret;
	}

	public Object get(Object key) {
		Field field = fields.get(key);
		if ( null != field ) {
			try {
				return	field.get(pojo);
			}
			catch (Exception e) {
			//	logger.error("failed to invoke get for field [{}] bcs of {}", field, e);
			}
		}
		else {
			//logger.warn("can't found field named [{}]", key);
		}
		
		return null;
	}

	public boolean isEmpty() {
		return fields.isEmpty();
	}

	public Set<String> keySet() {
		return fields.keySet();
	}

	public Object put(String key, Object value) {
		if ( readonly ) {
			throw new RuntimeException("can't invoke put method when readonly mode");
		}
		
		Field field = fields.get(key);
		if ( null != field ) {
			try {
				Object oldv = field.get(pojo);
				field.set(pojo, value);
				return	oldv;
			}
			catch (Exception e) {
				//logger.error("failed to invoke get/set for field [{}] bcs of {}", field, e);
			}
		}
		else {
			//logger.warn("can't found field named [{}]", key);
		}
		
		return null;
	}

	public void putAll(Map<? extends String, ? extends Object> m) {
		throw new UnsupportedOperationException();
	}

	public Object remove(Object key) {
		throw new UnsupportedOperationException();
	}

	public int size() {
		return fields.size();
	}

	public Collection<Object> values() {
		List<Object> ret = new ArrayList<Object>();
		Collection<Field> fieldList = fields.values();
		for ( Field field : fieldList ) {
			try {
				ret.add(field.get(pojo));
			} catch (Exception e) {
				//logger.error("failed to invoke get for field [{}] bcs of {}", field, e);
			}
		}
		return	ret;
	}

}
