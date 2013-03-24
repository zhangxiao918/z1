/**
 * 
 */
package com.skymobi.cac.maopao.passport.android.bean.bytebean.core;

/**
 * @author isdom
 *
 */
public interface FieldCodecProvider {
    public  ByteFieldCodec  getCodecOf(FieldCodecCategory type);
    public  ByteFieldCodec  getCodecOf(Class<?> clazz);
}
