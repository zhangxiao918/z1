/**
 * 
 */
package com.skymobi.android.bean.esb.core;

import java.lang.reflect.Field;

import com.skymobi.android.bean.bytebean.core.ByteFieldCodec;
import com.skymobi.android.bean.bytebean.core.ByteFieldDesc;
import com.skymobi.android.bean.bytebean.core.Field2Desc;
import com.skymobi.android.bean.esb.annotation.EsbField;

/**
 * @author hp
 *
 */
public class EsbFieldToDescForTLV implements Field2Desc {

	/* (non-Javadoc)
	 * @see com.skymobi.bean.bytebean.core.Field2Desc#genDesc(java.lang.reflect.Field)
	 * ignore esb's tlv field
	 */
	public ByteFieldDesc genDesc(Field field) {
        EsbField   byteField = field.getAnnotation(EsbField.class);
        Class<?>    clazz = field.getDeclaringClass();
        if ( (null != byteField) && ( -1 != byteField.tag() ) ) {
            try {
                EsbFieldDescImpl desc 
                = (EsbFieldDescImpl)new EsbFieldDescImpl()
                	.setTag( byteField.tag() )
                    .setField(field)
                    .setIndex( byteField.index() )
                    .setByteSize( byteField.bytes() )
                    .setCharset( byteField.charset() )
                    .setLengthField(  
                        byteField.length().equals("") 
                        ? null 
                        : clazz.getDeclaredField( byteField.length() ) )
                    .setCustomTypeMethod(
                        byteField.customType().equals("")                                
                        ? null
                        : clazz.getDeclaredMethod( byteField.customType() ) )
                	.setByteOrder(byteField.byteOrder())
                	.setCustomCodec(
                        byteField.customCodec().equals( ByteFieldCodec.class )                             
                        ? null
                        : byteField.customCodec() )
                	.setFixedLength(byteField.fixedLength());
                return  desc;
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
        }
        return  null;
	}

}
