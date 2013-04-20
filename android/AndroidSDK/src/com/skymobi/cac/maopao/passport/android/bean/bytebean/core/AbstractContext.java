/**
 * 
 */
package com.skymobi.cac.maopao.passport.android.bean.bytebean.core;

import java.lang.reflect.Field;

import com.skymobi.cac.maopao.passport.android.util.ByteOrder;
import com.skymobi.cac.maopao.passport.android.util.DefaultNumberCodecs;
import com.skymobi.cac.maopao.passport.android.util.NumberCodec;



/**
 * @author isdom
 *
 */
public class AbstractContext extends ByteBeanUtil implements FieldCodecContext {
    protected FieldCodecProvider    codecProvider = null;
    
    protected ByteFieldDesc   fieldDesc;
    protected NumberCodec     numberCodec;
    protected Class<?>        targetType;
    
    /* (non-Javadoc)
     * @see com.sky.applist20.bytebean.codec.FieldCodecContext#getByteLength(int)
     */
    public int getByteSize() {
        int ret = -1;
        if ( null != fieldDesc ) {
            ret = fieldDesc.getByteSize();
        }
        else if ( null != targetType ) {
            ret = super.type2DefaultByteSize(targetType);
        }
        return  ret;
    }

    /* (non-Javadoc)
     * @see com.sky.applist20.bytebean.codec.FieldCodecContext#getCodecOf(com.sky.applist20.bytebean.codec.FieldCodecType)
     */
    public ByteFieldCodec getCodecOf(FieldCodecCategory type) {
        if ( null != codecProvider ) {
            return  codecProvider.getCodecOf(type);
        }
        else {
            return null;
        }
    }

    public ByteFieldCodec getCodecOf(Class<?> clazz) {
        if ( null != codecProvider ) {
            return  codecProvider.getCodecOf(clazz);
        }
        else {
            return null;
        }
    }
    
    /* (non-Javadoc)
     * @see com.sky.applist20.bytebean.codec.FieldCodecContext#getFieldDesc()
     */
    public ByteFieldDesc getFieldDesc() {
        return fieldDesc;
    }

    /* (non-Javadoc)
     * @see com.sky.applist20.bytebean.codec.FieldCodecContext#getNumberCodec()
     */
    public NumberCodec getNumberCodec() {
        if ( null != fieldDesc 
            && ( ByteOrder.BigEndian == fieldDesc.getByteOrder() ||
                ByteOrder.LittleEndian == fieldDesc.getByteOrder() ) ) {
            return  DefaultNumberCodecs.byteOrder2NumberCodec(fieldDesc.getByteOrder());
        }
        return numberCodec;
    }

    public Field getField() {
        if ( null != this.fieldDesc ) {
            return  this.fieldDesc.getField();
        }
        else {
            return null;
        }
    }
}
