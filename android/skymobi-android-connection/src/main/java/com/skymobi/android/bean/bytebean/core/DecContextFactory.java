/**
 * 
 */
package com.skymobi.android.bean.bytebean.core;

/**
 * @author hp
 *
 */
public interface DecContextFactory {
	public DecContext	createDecContext(
			byte[] decBytes, Class<?> type, Object parent, ByteFieldDesc desc);
}
