/**
 * 
 */
package com.skymobi.android.bean.esb.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.skymobi.android.bean.bytebean.core.ByteFieldCodec;
import com.skymobi.android.util.ByteOrder;

/**
 * @author isdom
 *
 */
@Retention(RetentionPolicy.RUNTIME) 
public @interface EsbField {
    public abstract int index();

    //	for esb's tlv field
    public abstract int tag() default -1;
    
    public abstract int bytes() default -1;
    
    public abstract String length() default "";
    
    public abstract String customType() default "";
    
    public abstract String charset() default "UTF-8";
    
    public abstract ByteOrder byteOrder() default ByteOrder.None;
    
    public abstract String description() default "";
    
    public abstract Class<? extends ByteFieldCodec> customCodec() default ByteFieldCodec.class;
    
    public abstract int fixedLength() default -1;
}
