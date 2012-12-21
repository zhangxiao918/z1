/**
 * 
 */
package com.skymobi.android.bean.tlv.decode;

import java.lang.reflect.Field;



/**
 * @author hp
 *
 */
public interface TLVDecodeContextFactory {
	public TLVDecodeContext	createDecodeContext(Class<?> type, Field field);
}
