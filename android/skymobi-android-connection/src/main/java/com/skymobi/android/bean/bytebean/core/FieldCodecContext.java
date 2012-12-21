/**
 * 
 */
package com.skymobi.android.bean.bytebean.core;

import java.lang.reflect.Field;

import com.skymobi.android.util.NumberCodec;

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
