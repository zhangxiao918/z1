/**
 * 
 */
package com.skymobi.android.bean.tlv.decode;

import java.lang.reflect.Field;

import com.skymobi.android.bean.tlv.meta.TLVFieldMetainfo;
import com.skymobi.android.bean.util.meta.Int2TypeMetainfo;
import com.skymobi.android.util.NumberCodec;

/**
 * @author hp
 *
 */
public interface TLVDecodeContext {
	public	Class<?>			getValueType();
	public	Field				getValueField();
	public 	Int2TypeMetainfo	getTypeMetainfo();
	public	TLVFieldMetainfo	getFieldMetainfo();
    public  NumberCodec			getNumberCodec();
    public 	TLVDecoderRepository	getDecoderRepository();
}
