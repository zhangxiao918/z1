/**
 * 
 */
package com.skymobi.android.bean.tlv.encode;


/**
 * @author hp
 *
 */
public interface TLVEncoderRepository {
    public 	TLVEncoder	getEncoderOf(Class<?> cls);
}
