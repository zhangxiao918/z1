package org.bluestome.pcs.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MapUtils {
	private static Log logger = LogFactory.getLog(MapUtils.class);

	private MapUtils() {
	}

	/**
	 * fieldName 作为Map的一个key，getFieldName()的返回结果作为Map的值
	 * 
	 * @param object
	 *            参数对象
	 * @return Map 返回经过解析的参数对象
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> objectToMap(Object object) {
		if (object == null) {
			return new HashMap<String, Object>();
		}

		if (object instanceof Map) {
			return (Map) object;
		}

		Map<String, Object> map = new HashMap<String, Object>();

		/**
		 * IBatis的默认对象参数名
		 */
		map.put("value", object);

		Field[] fields = object.getClass().getDeclaredFields();
		for (Field field : fields) {
			String fieldName = field.getName();
			if (PropertyUtils.isReadable(object, fieldName)) {
				try {
					map.put(fieldName, PropertyUtils.getProperty(object,
							fieldName));
				} catch (Exception e) {
					logger.error("Object:" + object + "  ,field:" + fieldName
							+ "(). invoke error!", e);
				}
			}
		}

		return map;
	}

	/**
	 * 
	 * @param bean
	 * @param parameters
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static void populate(Object bean, Map parameters) throws Exception {
		for (Iterator i = parameters.entrySet().iterator(); i.hasNext();) {
			Map.Entry entry = (Map.Entry) i.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
			String strKey;

			if (key != null) {
				if (!(key instanceof String)) {
					strKey = key.toString();
				} else {
					strKey = (String) key;
				}

				if (PropertyUtils.isWriteable(bean, strKey)) {
					try {
						BeanUtils.setProperty(bean, strKey, value);
					} catch (Exception e) {
						logger.error("Object:" + bean + "  ,field:" + strKey
								+ "(). Writer error!", e);
					}
				}
			}
		}
	}
}
