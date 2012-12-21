/**
 * 
 */
package com.skymobi.android.bean.tlv.decode;


/**
 * @author hp
 *
 */
public interface TLVDecoder {
	public	Object	decode(int tlvLength, byte[] tlvValue, TLVDecodeContext ctx);
}
