
package com.skymobi.cac.maopao.passport.android.bean.tlv.decode.decoders;

import com.skymobi.cac.maopao.passport.android.bean.tlv.decode.TLVDecodeContext;
import com.skymobi.cac.maopao.passport.android.bean.tlv.decode.TLVDecoder;

public class BooleanTLVDecoder implements TLVDecoder {
	
    public Object decode(int tlvLength, byte[] tlvValue, TLVDecodeContext ctx) {
        return new Boolean(0 != tlvValue[0]);
    }
}
