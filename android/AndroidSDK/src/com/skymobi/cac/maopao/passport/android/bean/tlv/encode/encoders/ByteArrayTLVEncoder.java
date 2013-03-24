/**
 * 
 */
package com.skymobi.cac.maopao.passport.android.bean.tlv.encode.encoders;

import java.util.Arrays;
import java.util.List;

import com.skymobi.cac.maopao.passport.android.bean.tlv.encode.TLVEncodeContext;
import com.skymobi.cac.maopao.passport.android.bean.tlv.encode.TLVEncoder;



/**
 * @author hp
 *
 */
public class ByteArrayTLVEncoder implements TLVEncoder {

    /* (non-Javadoc)
     * @see com.skymobi.util.Transformer#transform(java.lang.Object)
     */
	public List<byte[]> encode(Object src, TLVEncodeContext ctx) {
        return Arrays.asList( (byte[])src );
	}

}
