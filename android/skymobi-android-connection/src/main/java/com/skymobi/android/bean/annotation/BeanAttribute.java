/**
 * 
 */
package com.skymobi.android.bean.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author hp
 *
 */
@Retention(RetentionPolicy.RUNTIME) 
public @interface BeanAttribute {
    public abstract boolean required() default false;
    public abstract String desc() default "";
}
