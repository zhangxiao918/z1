/**
 * 
 */
package com.skymobi.android.bean.xip.core;

import java.lang.reflect.Field;

import com.skymobi.android.bean.bytebean.core.ByteFieldDesc;
import com.skymobi.android.bean.bytebean.core.DefaultFieldDesc;
import com.skymobi.android.bean.bytebean.core.Field2Desc;
import com.skymobi.android.bean.xip.annotation.SsipField;

/**
 * @author hp
 *
 */
public class SsipToDesc implements Field2Desc {

	/* (non-Javadoc)
	 * @see com.skymobi.bean.bytebean.core.Field2Desc#genDesc(java.lang.reflect.Field)
	 */
	public ByteFieldDesc genDesc(Field field) {
        SsipField   byteField = field.getAnnotation(SsipField.class);
        Class<?>    clazz = field.getDeclaringClass();
        if ( null != byteField ) {
            try {
                DefaultFieldDesc desc 
                = new DefaultFieldDesc()
                    .setField(field)
                    .setIndex( byteField.index() )
                    .setByteSize( byteField.bytes() )
                    .setCustomTypeMethod(
                        byteField.customType().equals("")                                
                        ? null
                        : clazz.getDeclaredMethod( byteField.customType() ) );
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
