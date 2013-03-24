/**
 * 
 */
package com.skymobi.cac.maopao.passport.android.bean.bytebean.core;

import java.lang.reflect.Field;

import com.skymobi.cac.maopao.passport.android.util.NumberCodec;



/**
 * @author isdom
 *
 */
public interface FieldCodecContext extends FieldCodecProvider {
    public  ByteFieldDesc   getFieldDesc();
    public  Field           getField();
    public  NumberCodec     getNumberCodec();
    public  int             getByteSize();
}
