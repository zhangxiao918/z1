/**
 * 
 */
package com.skymobi.cac.maopao.passport.android.bean.bytebean;

import java.lang.reflect.Field;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.core.ByteFieldDesc;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.core.DefaultFieldDesc;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.core.Field2Desc;



/**
 * @author hp
 *
 */
public class ByteFieldToDesc implements Field2Desc {

	/* (non-Javadoc)
	 * @see com.skymobi.bean.bytebean.core.Field2Desc#genDesc(java.lang.reflect.Field)
	 */
	public ByteFieldDesc genDesc(Field field) {
        ByteField   byteField = field.getAnnotation(ByteField.class);
        Class<?>    clazz = field.getDeclaringClass();
        if ( null != byteField ) {
        	try {
                DefaultFieldDesc desc 
                = new DefaultFieldDesc()
                    .setField(field)
                    .setIndex( byteField.index() )
                    .setByteSize( byteField.bytes() )
                    .setCharset( byteField.charset() )
                    .setLengthFieldIndex( byteField.length())
                    .setCustomTypeMethod(
                        byteField.customType().equals("")                                
                        ? null
                        : clazz.getDeclaredMethod( byteField.customType() ) )
                     .setByteOrder(byteField.byteOrder())
                     .setFixedLength(byteField.fixedLength());
                return  desc;
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return  null;
    }
}
