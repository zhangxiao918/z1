/**
 * 
 */
package com.skymobi.android.bean.pf.core;

import java.lang.reflect.Field;

import com.skymobi.android.bean.bytebean.core.ByteFieldCodec;
import com.skymobi.android.bean.bytebean.core.ByteFieldDesc;
import com.skymobi.android.bean.bytebean.core.DefaultFieldDesc;
import com.skymobi.android.bean.bytebean.core.Field2Desc;
import com.skymobi.android.bean.pf.annotation.PFField;

/**
 * 
 * @author Shawn.Du
 *
 */
public class PFFieldToDesc implements Field2Desc {

	/* (non-Javadoc)
	 * @see com.skymobi.bean.bytebean.core.Field2Desc#genDesc(java.lang.reflect.Field)
	 */
	public ByteFieldDesc genDesc(Field field) {
		PFField   byteField = field.getAnnotation(PFField.class);
        Class<?>    clazz = field.getDeclaringClass();
        if ( null != byteField ) {
            try {
                DefaultFieldDesc desc 
                = new DefaultFieldDesc()
                    .setField(field)
                    .setIndex( byteField.index() )
                    .setByteSize( byteField.bytes() )
                    .setCharset( byteField.charset() )
                    .setLengthField(
                        byteField.length().equals("") 
                        ? null 
                        : clazz.getDeclaredField( byteField.length() ) )
                	.setByteOrder(byteField.byteOrder())
                	.setCustomCodec(
                        byteField.customCodec().equals( ByteFieldCodec.class )                             
                        ? null
                        : byteField.customCodec() );
                return  desc;
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
        }
        return  null;
	}

}
