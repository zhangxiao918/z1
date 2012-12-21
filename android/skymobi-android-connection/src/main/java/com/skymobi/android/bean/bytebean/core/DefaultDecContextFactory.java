/**
 * 
 */
package com.skymobi.android.bean.bytebean.core;

import com.skymobi.android.util.NumberCodec;

/**
 * @author hp
 *
 */
public class DefaultDecContextFactory implements DecContextFactory {

	private	FieldCodecProvider	codecProvider;
	private	NumberCodec			numberCodec;
	
	/* (non-Javadoc)
	 * @see com.skymobi.bean.bytebean.core.DecContextFactory#createDecContext(byte[], java.lang.Class, java.lang.Object, com.skymobi.bean.bytebean.core.ByteFieldDesc)
	 */
	public DecContext createDecContext(byte[] decBytes, Class<?> type,
			Object parent, ByteFieldDesc desc) {
		return new DefaultDecContext()
				.setCodecProvider(codecProvider)
				.setDecBytes(decBytes)
				.setDecClass(type)
				.setDecOwner(parent)
				.setNumberCodec(numberCodec)
				.setFieldDesc(desc)
				.setDecContextFactory(this);
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
