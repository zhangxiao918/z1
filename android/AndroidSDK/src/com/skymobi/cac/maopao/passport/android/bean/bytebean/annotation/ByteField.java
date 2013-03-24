/**
 * 
 */
package com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.skymobi.cac.maopao.passport.android.util.ByteOrder;



/**
 * @author isdom
 *
 */
@Retention(RetentionPolicy.RUNTIME) 
public @interface ByteField {
    /**
     * 
     * @return
     */
    public abstract int index();

    /**
     * 
     * @return
     */
    public abstract int bytes() default -1;
    
    public abstract int length() default -1;
    
    public abstract String customType() default "";
    
    public abstract String charset() default "UTF-16";
    
    public abstract ByteOrder byteOrder() default ByteOrder.None;
    
    public abstract int fixedLength() default -1;
    
    public abstract String description() default "";
}
