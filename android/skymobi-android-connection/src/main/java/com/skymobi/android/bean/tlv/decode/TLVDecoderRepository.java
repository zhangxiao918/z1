/**
 * 
 */
package com.skymobi.android.bean.tlv.decode;


/**
 * @author hp
 *
 */
public interface TLVDecoderRepository {
    public 	TLVDecoder	getDecoderOf(Class<?> cls);
}
