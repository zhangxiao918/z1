/**
 * 
 */
package com.skymobi.cac.maopao.passport.android.bean.tlv.decode.decoders;

import org.apache.commons.lang.ArrayUtils;

import com.skymobi.cac.maopao.passport.android.bean.tlv.decode.TLVDecodeContext;
import com.skymobi.cac.maopao.passport.android.bean.tlv.decode.TLVDecoder;



/**
 * @author hp
 *
 */
public class ByteArrayTLVDecoder implements TLVDecoder {

   // @SuppressWarnings("unused")
	//private static final Logger logger = LoggerFactory.getLogger(ByteArrayTLVDecoder.class);
    
	/* (non-Javadoc)
	 * @see com.skymobi.bean.tlv.TLVDecoder#decode(int, byte[], com.skymobi.bean.tlv.TLVDecodeContext)
	 */
	public Object decode(int tlvLength, byte[] tlvValue, TLVDecodeContext ctx) {
		if ( tlvLength == tlvValue.length ) {
			return tlvValue;
		}
		else {
			return	ArrayUtils.subarray(tlvValue, 0, tlvLength);
		}
	}

}
