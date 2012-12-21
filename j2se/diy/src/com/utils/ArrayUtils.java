/**
 * 
 */
package com.utils;

import java.lang.reflect.Array;

/**
 * @author wangqi
 * 
 */
public class ArrayUtils extends org.apache.commons.lang.ArrayUtils {

	public static int count(final String[] args, final String valueToFind) {
		if (args == null || args.length == 0) {
			return 0;
		}

		int ret = 0;
		for (int i = 0; i < args.length; i++) {
			if (args[i] != null && args[i].equals(valueToFind)) {
				ret++;
			}
		}
		return ret;
	}

	public static Long[] toLong(final String[] args) {
		if (args == null || args.length == 0) {
			return new Long[0];
		}

		Long[] ret = new Long[args.length];
		for (int i = 0; i < args.length; i++) {
			if (args[i] == null) {
				ret[i] = null;
			} else {
				ret[i] = Long.parseLong(args[i]);
			}
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	public static String toString(Object... args) {
		if (args == null || args.length == 0) {
			return "";
		}
		StringBuffer retVal = new StringBuffer("'" + args[0] + "'");
		for (int i = 1; i < args.length; i++) {
			retVal.append(",").append("'" + args[i] + "'");
		}
		return retVal.toString();
	}

	public static Object[] clone(final Object[] array) {
		if (array == null) {
			return null;
		}
		return (Object[]) array.clone();
	}

	public static Object[] addAll(final Object[] array1, final Object[] array2) {
		if (array1 == null) {
			return clone(array2);
		} else if (array2 == null) {
			return clone(array1);
		} else {
			Object[] joinedArray = (Object[]) Array.newInstance(array1
					.getClass().getComponentType(), array1.length
					+ array2.length);
			System.arraycopy(array1, 0, joinedArray, 0, array1.length);
			System.arraycopy(array2, 0, joinedArray, array1.length,
					array2.length);
			return joinedArray;
		}
	}

	/**
	 * 判断指定对象是否存在于指定数组中。
	 * 
	 * <p>
	 * 如果数组为<code>null</code>则返回<code>false</code>。
	 * </p>
	 * 
	 * @param array
	 *            要扫描的数组
	 * @param byteToFind
	 *            要查找的元素
	 * 
	 * @return 如果找到则返回<code>true</code>
	 */
	public static boolean contains(byte[] array, byte byteToFind) {
		return indexOf(array, byteToFind) != -1;
	}

	/**
	 * 在数组中查找一个元素序列。
	 * 
	 * <p>
	 * 如果未找到或数组为<code>null</code>则返回<code>-1</code>。
	 * </p>
	 * 
	 * @param array
	 *            要扫描的数组
	 * @param byteToFind
	 *            要查找的元素
	 * 
	 * @return 该元素序列在数组中的序号，如果数组为<code>null</code>或未找到，则返回<code>-1</code>。
	 */
	public static int indexOf(byte[] array, byte byteToFind) {
		return indexOf(array, byteToFind, 0);
	}

	/**
	 * 在数组中查找一个元素。
	 * 
	 * <p>
	 * 如果未找到或数组为<code>null</code>则返回<code>-1</code>。
	 * </p>
	 * 
	 * <p>
	 * 起始索引小于<code>0</code>则看作<code>0</code>，超出数组长度的起始索引则返回<code>-1</code>。
	 * </p>
	 * 
	 * @param array
	 *            要扫描的数组
	 * @param byteToFind
	 *            要查找的元素
	 * @param startIndex
	 *            起始索引
	 * 
	 * @return 该元素在数组中的序号，如果数组为<code>null</code>或未找到，则返回<code>-1</code>。
	 */
	public static int indexOf(byte[] array, byte byteToFind, int startIndex) {
		if (array == null) {
			return -1;
		}

		if (startIndex < 0) {
			startIndex = 0;
		}

		for (int i = startIndex; i < array.length; i++) {
			if (byteToFind == array[i]) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * 判断指定对象是否存在于指定数组中。
	 * 
	 * <p>
	 * 如果数组为<code>null</code>则返回<code>false</code>。
	 * </p>
	 * 
	 * @param array
	 *            要扫描的数组
	 * @param objectToFind
	 *            要查找的元素
	 * 
	 * @return 如果找到则返回<code>true</code>
	 */
	public static boolean contains(Object[] array, Object objectToFind) {
		return indexOf(array, objectToFind) != -1;
	}

	/**
	 * 在数组中查找一个元素。
	 * 
	 * <p>
	 * 如果未找到或数组为<code>null</code>则返回<code>-1</code>。
	 * </p>
	 * 
	 * @param array
	 *            要扫描的数组
	 * @param objectToFind
	 *            要查找的元素
	 * 
	 * @return 该元素在数组中的序号，如果数组为<code>null</code>或未找到，则返回<code>-1</code>。
	 */
	public static int indexOf(Object[] array, Object objectToFind) {
		return indexOf(array, objectToFind, 0);
	}

	/**
	 * 在数组中查找一个元素。
	 * 
	 * <p>
	 * 如果未找到或数组为<code>null</code>则返回<code>-1</code>。
	 * </p>
	 * 
	 * <p>
	 * 起始索引小于<code>0</code>则看作<code>0</code>，超出数组长度的起始索引则返回<code>-1</code>。
	 * </p>
	 * 
	 * @param array
	 *            要扫描的数组
	 * @param objectToFind
	 *            要查找的元素
	 * @param startIndex
	 *            起始索引
	 * 
	 * @return 该元素在数组中的序号，如果数组为<code>null</code>或未找到，则返回<code>-1</code>。
	 */
	public static int indexOf(Object[] array, Object objectToFind,
			int startIndex) {
		if (array == null) {
			return -1;
		}

		if (startIndex < 0) {
			startIndex = 0;
		}

		if (objectToFind == null) {
			for (int i = startIndex; i < array.length; i++) {
				if (array[i] == null) {
					return i;
				}
			}
		} else {
			for (int i = startIndex; i < array.length; i++) {
				if (objectToFind.equals(array[i])) {
					return i;
				}
			}
		}

		return -1;
	}
}
