/**
 * 
 */
package com.skymobi.cac.maopao.passport.android.bean.bytebean.core;

import com.skymobi.cac.maopao.passport.android.util.NumberCodec;

/**
 * @author hp
 *
 */
public class DefaultEncContextFactory implements EncContextFactory {

	private	FieldCodecProvider	codecProvider;
	private	NumberCodec			numberCodec;
	
	/* (non-Javadoc)
	 * @see com.skymobi.bean.bytebean.core.EncContextFactory#createEncContext(java.lang.Object, java.lang.Class, com.skymobi.bean.bytebean.core.ByteFieldDesc)
	 */
	public EncContext createEncContext(Object encObject, Class<?> type,
			ByteFieldDesc desc) {
		return new DefaultEncContext()
				.setCodecProvider(codecProvider)
				.setEncClass(type)
				.setEncObject(encObject)
				.setNumberCodec(numberCodec)
				.setFieldDesc(desc)
				.setEncContextFactory(this);
	}

	public FieldCodecProvider getCodecProvider() {
		return codecProvider;
	}

	public void setCodecProvider(FieldCodecProvider codecProvider) {
		this.codecProvider = codecProvider;
	}

	public NumberCodec getNumberCodec() {
		return numberCodec;
	}

	public void setNumberCodec(NumberCodec numberCodec) {
		this.numberCodec = numberCodec;
	}

}
