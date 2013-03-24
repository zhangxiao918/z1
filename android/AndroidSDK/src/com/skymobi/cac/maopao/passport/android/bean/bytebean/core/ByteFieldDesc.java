/**
 * 
 */
package com.skymobi.cac.maopao.passport.android.bean.bytebean.core;

import java.lang.reflect.Field;
import java.util.Comparator;

import com.skymobi.cac.maopao.passport.android.util.ByteOrder;



/**
 * @author isdom
 *
 */
public interface ByteFieldDesc {

    public int getIndex();
    
    public int getByteSize();

    public Field getField();
    
    public Class<?> getFieldType();
    
    public boolean hasLength();
    
    public int getLength(Object owner);
    
    public int getStringLengthInBytes(Object owner);
    
    public void setLength(Object owner);
    
    public Class<?> getCustomType(Object owner);

    public String getCharset();
    
    public String getDescription();
    
    public ByteOrder    getByteOrder();

    public int  getFixedLength();
    
    public int getLengthFieldIndex();
    
    public Class<? extends ByteFieldCodec> getCustomCodec();
    
    public static final Comparator<ByteFieldDesc> comparator = new Comparator<ByteFieldDesc>() {
        public int compare(ByteFieldDesc desc1, ByteFieldDesc desc2) {
            int ret = desc1.getIndex() - desc2.getIndex();
            if ( 0 == ret ) {
                throw   new RuntimeException( "field1:" + desc1.getField() + "/field2:" + desc2.getField()
                        + " has the same index value, internal error.");
            }
            return  ret;
        }
    };
}
