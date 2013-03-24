/**
 * 
 */
package com.skymobi.cac.maopao.passport.android.bean.tlv.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author hp
 *
 */
@Retention(RetentionPolicy.RUNTIME) 
public @interface TLVAttribute {
    public abstract int tag();
    public abstract Class<?> type() default TLVAttribute.class;
    public abstract String charset() default "";
    public abstract String description() default "";
    
    /**
     * 
     * @return
     */
    public abstract int bytes() default -1;
}
