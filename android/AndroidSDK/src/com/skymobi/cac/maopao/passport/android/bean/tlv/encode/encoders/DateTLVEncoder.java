/**
 * 
 */
package com.skymobi.cac.maopao.passport.android.bean.tlv.encode.encoders;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.skymobi.cac.maopao.passport.android.bean.tlv.encode.TLVEncodeContext;
import com.skymobi.cac.maopao.passport.android.bean.tlv.encode.TLVEncoder;



/**
 * @author hp
 *
 */
public class DateTLVEncoder implements TLVEncoder {

    private static final int BYTE_SIZE = 8;
    
    /* (non-Javadoc)
     * @see com.skymobi.util.Transformer#transform(java.lang.Object)
     */
	public List<byte[]> encode(Object src, TLVEncodeContext ctx) {
		byte[] ret = null;
		
		if ( src instanceof Date ) {
			ret = ctx.getNumberCodec().long2Bytes( ((Date)src).getTime(), BYTE_SIZE);
		}
		else {
			throw new RuntimeException("DateTLVEncoder: wrong source type. [" + src.getClass() + "]");
		}
		
		return	Arrays.asList( ret );
	}

}
