/**
 * 
 */
package com.skymobi.android.bean.bytebean.core;


/**
 * @author isdom
 *
 */
public interface BeanFieldCodec extends ByteFieldCodec {
    public int getStaticByteSize(Class<?> clazz);
	public DecContextFactory getDecContextFactory();
	public EncContextFactory getEncContextFactory();
}
