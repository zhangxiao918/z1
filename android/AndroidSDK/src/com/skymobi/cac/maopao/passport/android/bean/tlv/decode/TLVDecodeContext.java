/**
 * 
 */
package com.skymobi.cac.maopao.passport.android.bean.tlv.decode;

import java.lang.reflect.Field;

import com.skymobi.cac.maopao.passport.android.bean.tlv.meta.TLVFieldMetainfo;
import com.skymobi.cac.maopao.passport.android.bean.util.meta.Int2TypeMetainfo;
import com.skymobi.cac.maopao.passport.android.util.NumberCodec;



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
