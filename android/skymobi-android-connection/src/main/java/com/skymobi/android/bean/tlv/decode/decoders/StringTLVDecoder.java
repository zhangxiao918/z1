/**
 * 
 */
package com.skymobi.android.bean.tlv.decode.decoders;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.skymobi.android.bean.tlv.annotation.TLVAttribute;
import com.skymobi.android.bean.tlv.decode.TLVDecodeContext;
import com.skymobi.android.bean.tlv.decode.TLVDecodeContextFactory;
import com.skymobi.android.bean.tlv.decode.TLVDecoder;
import com.skymobi.android.bean.tlv.decode.TLVDecoderOfBean;
import com.skymobi.android.bean.tlv.meta.TLVFieldMetainfo;
import com.skymobi.android.bean.util.meta.Int2TypeMetainfo;


/**
 * @author hp
 *
 */
public class StringTLVDecoder implements TLVDecoder {

    private static final Logger logger = LoggerFactory.getLogger(StringTLVDecoder.class);
	/* (non-Javadoc)
	 * @see com.skymobi.bean.tlv.TLVDecoder#decode(int, byte[], com.skymobi.bean.tlv.decode.TLVDecodeContext)
	 */
	public Object decode(int tlvLength, byte[] tlvValue, TLVDecodeContext ctx) {
        String charset = "UTF-8";
        Field field = ctx.getValueField();
        if ( null != field ) {
	        TLVAttribute attr = field.getAnnotation(TLVAttribute.class);
	        if ( null != attr ) {
	        	if ( !"".equals( attr.charset() ) ) {
	        		charset = attr.charset();
	        	}
	        }
        }
		try {
			return new String(tlvValue, 0, tlvLength, charset);
		} catch (UnsupportedEncodingException e) {
        	logger.error("StringTLVDecoder:", e);
		}
		
		return	null;
	}

}
