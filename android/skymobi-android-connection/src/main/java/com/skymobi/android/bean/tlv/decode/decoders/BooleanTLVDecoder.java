
package com.skymobi.android.bean.tlv.decode.decoders;

import com.skymobi.android.bean.tlv.annotation.TLVAttribute;
import com.skymobi.android.bean.tlv.decode.TLVDecodeContext;
import com.skymobi.android.bean.tlv.decode.TLVDecodeContextFactory;
import com.skymobi.android.bean.tlv.decode.TLVDecoder;
import com.skymobi.android.bean.tlv.decode.TLVDecoderOfBean;
import com.skymobi.android.bean.tlv.meta.TLVFieldMetainfo;
import com.skymobi.android.bean.util.meta.Int2TypeMetainfo;

public class BooleanTLVDecoder implements TLVDecoder {
	
    public Object decode(int tlvLength, byte[] tlvValue, TLVDecodeContext ctx) {
        return new Boolean(0 != tlvValue[0]);
    }
}
