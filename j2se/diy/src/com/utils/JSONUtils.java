package com.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wangqi
 */
public class JSONUtils {

	private static Logger logger = LoggerFactory.getLogger(JSONUtils.class);

	/**
	 * 将POJO转换成JSON
	 * 
	 * @param object
	 * @return
	 */
	public static String bean2Json(Object object) {
		JSONObject jsonObject = JSONObject.fromObject(object);
		return jsonObject.toString();
	}

	/**
	 * 将JSON转换成<code>java.lang.String</code>
	 * 
	 * @param json
	 * @return
	 */
	public static String json2String(String json) {
		return (String) json2Object(json, String.class);
	}

	/**
	 * 将数组转换成JSON
	 * 
	 * @param object
	 * @return
	 */
	public static String array2Json(Object object) {
		JSONArray jsonArray = JSONArray.fromObject(object);
		return jsonArray.toString();
	}

	/**
	 * 将JSON转换成Collection,其中valueClz为Collection具体子类的Class,
	 * 
	 * @param json
	 * @param valueClz
	 *            存放的对象的Class
	 * @return
	 */
	public static Collection<?> json2Collection(String json, Class<?> valueClz) {
		JSONArray jsonArray = JSONArray.fromObject(json);
		return JSONArray.toCollection(jsonArray, valueClz);
	}

	/**
	 * 将Collection转换成JSON
	 * 
	 * @param object
	 * @return
	 */
	public static String collection2Json(Object object) {
		JSONArray jsonArray = JSONArray.fromObject(object);
		return jsonArray.toString();
	}

	/**
	 * 将JSON转换成数组,其中valueClz为数组中存放的对象的Class
	 * 
	 * @param json
	 * @param valueClz
	 * @return
	 */
	public static Object json2Array(String json, Class<?> valueClz) {
		JSONArray jsonArray = JSONArray.fromObject(json);
		return JSONArray.toArray(jsonArray, valueClz);
	}

	/**
	 * 
	 * @param json
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Collection json2Collection(String json) {
		JSONArray array = JSONArray.fromObject(json, getJsonConfig());
		return JSONArray.toCollection(array, getJsonConfig());
	}

	public static JsonConfig getJsonConfig() {
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setIgnoreDefaultExcludes(false);
		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		jsonConfig.registerJsonValueProcessor(Date.class,
				new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
		return jsonConfig;
	}

	/**
	 * 
	 * 将Json格式的字符串转换成指定的对象返回
	 * 
	 * 
	 * 
	 * @param jsonString
	 * 
	 *            Json格式的字符串
	 * 
	 * @param pojoCalss
	 * 
	 *            转换后的对象类型
	 * 
	 * @return 转换后的对象
	 */

	@SuppressWarnings("unchecked")
	public static Object json2Object(String jsonString, Class pojoCalss) {

		Object pojo;

		JSONObject jsonObject = JSONObject.fromObject(jsonString);

		pojo = JSONObject.toBean(jsonObject, pojoCalss);

		return pojo;

	}

	/**
	 * 
	 * 将Json格式的字符串转换成Map<String,Object>对象返回
	 * 
	 * 
	 * 
	 * @param jsonString
	 * 
	 *            需要进行转换的Json格式字符串
	 * 
	 * @return 转换后的Map<String,Object>对象
	 */

	@SuppressWarnings("unchecked")
	public static Map<String, Object> json2Map(String jsonString) {

		JSONObject jsonObject = JSONObject.fromObject(jsonString);

		Iterator keyIter = jsonObject.keys();

		String key;

		Object value;

		Map<String, Object> valueMap = new HashMap<String, Object>();

		while (keyIter.hasNext()) {

			key = (String) keyIter.next();

			value = jsonObject.get(key);

			valueMap.put(key, value);

		}

		return valueMap;

	}

	/**
	 * 
	 * 将Json格式的字符串转换成对象数组返回
	 * 
	 * 
	 * 
	 * @param jsonString
	 * 
	 *            需要进行转换的Json格式字符串
	 * 
	 * @return 转换后的对象数组
	 */

	public static Object[] json2ObjectArray(String jsonString) {

		JSONArray jsonArray = JSONArray.fromObject(jsonString);

		return jsonArray.toArray();

	}

	/**
	 * 
	 * 将Json格式的字符串转换成指定对象组成的List返回
	 * 
	 * 
	 * 
	 * @param jsonString
	 * 
	 *            Json格式的字符串
	 * 
	 * @param pojoClass
	 * 
	 *            转换后的List中对象类型
	 * 
	 * @return 转换后的List对象
	 */

	@SuppressWarnings("unchecked")
	public static <T extends Object> List<T> json2List(String jsonString,
			Class<T> pojoClass) {

		JSONArray jsonArray = JSONArray.fromObject(jsonString);

		JSONObject jsonObject;

		T pojoValue;

		List<T> list = new ArrayList<T>();

		for (int i = 0; i < jsonArray.size(); i++) {

			jsonObject = jsonArray.getJSONObject(i);

			pojoValue = (T) JSONObject.toBean(jsonObject, pojoClass);

			list.add(pojoValue);

		}

		return list;

	}

	
	/**
	 * 
	 * 将Json格式的字符串转换成指定对象组成的List返回
	 * 
	 * 
	 * 
	 * @param jsonString
	 * 
	 *            Json格式的字符串
	 * 
	 * @param pojoClass
	 * 
	 *            转换后的List中对象类型
	 * 
	 * @return 转换后的List对象
	 */

	@SuppressWarnings("unchecked")
	public static <T extends Object> List<T> json2List(JSONArray jsonArray,
			Class<T> pojoClass) {

		JSONObject jsonObject;

		T pojoValue;

		List<T> list = new ArrayList<T>();

		for (int i = 0; i < jsonArray.size(); i++) {

			jsonObject = jsonArray.getJSONObject(i);

			pojoValue = (T) JSONObject.toBean(jsonObject, pojoClass);

			list.add(pojoValue);

		}

		return list;

	}
	
	/**
	 * 
	 * 将Json格式的字符串转换成字符串数组返回
	 * 
	 * 
	 * 
	 * @param jsonString
	 * 
	 *            需要进行转换的Json格式字符串
	 * 
	 * @return 转换后的字符串数组
	 */

	public static String[] json2StringArray(String jsonString) {

		JSONArray jsonArray = JSONArray.fromObject(jsonString);

		String[] stringArray = new String[jsonArray.size()];

		for (int i = 0; i < jsonArray.size(); i++) {

			stringArray[i] = jsonArray.getString(i);

		}

		return stringArray;

	}

	/**
	 * 
	 * 将Json格式的字符串转换成Long数组返回
	 * 
	 * 
	 * 
	 * @param jsonString
	 * 
	 *            需要进行转换的Json格式字符串
	 * 
	 * @return 转换后的Long数组
	 */

	public static Long[] json2LongArray(String jsonString) {

		JSONArray jsonArray = JSONArray.fromObject(jsonString);

		Long[] longArray = new Long[jsonArray.size()];

		for (int i = 0; i < jsonArray.size(); i++) {

			longArray[i] = jsonArray.getLong(i);

		}

		return longArray;

	}

	/**
	 * 
	 * 将Json格式的字符串转换成Integer数组返回
	 * 
	 * 
	 * 
	 * @param jsonString
	 * 
	 *            需要进行转换的Json格式字符串
	 * 
	 * @return 转换后的Integer数组
	 */

	public static Integer[] json2IntegerArray(String jsonString) {

		JSONArray jsonArray = JSONArray.fromObject(jsonString);

		Integer[] integerArray = new Integer[jsonArray.size()];

		for (int i = 0; i < jsonArray.size(); i++) {

			integerArray[i] = jsonArray.getInt(i);

		}

		return integerArray;

	}

	/**
	 * 
	 * 将Json格式的字符串转换成日期数组返回
	 * 
	 * 
	 * 
	 * @param jsonString
	 * 
	 *            需要进行转换的Json格式字符串
	 * 
	 * @param DataFormat
	 * 
	 *            返回的日期格式
	 * 
	 * @return 转换后的日期数组
	 */

	public static Date[] json2DateArray(String jsonString, String DataFormat) {

		JSONArray jsonArray = JSONArray.fromObject(jsonString);

		Date[] dateArray = new Date[jsonArray.size()];

		String dateString;

		Date date;

		for (int i = 0; i < jsonArray.size(); i++) {

			dateString = jsonArray.getString(i);

			date = DateUtils.parseDate(dateString, DataFormat);

			dateArray[i] = date;

		}

		return dateArray;

	}

	/**
	 * 
	 * 将Json格式的字符串转换成Double数组返回
	 * 
	 * 
	 * 
	 * @param jsonString
	 * 
	 *            需要进行转换的Json格式字符串
	 * 
	 * @return 转换后的Double数组
	 */

	public static Double[] json2DoubleArray(String jsonString) {

		JSONArray jsonArray = JSONArray.fromObject(jsonString);

		Double[] doubleArray = new Double[jsonArray.size()];

		for (int i = 0; i < jsonArray.size(); i++) {

			doubleArray[i] = jsonArray.getDouble(i);

		}

		return doubleArray;

	}

}
