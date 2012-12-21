/**
 * 
 */
package com.skymobi.android.bean.kv.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author hp
 *
 */
@Retention(RetentionPolicy.RUNTIME) 
public @interface KeyValueAttribute {
    public abstract String key();
    public abstract String desc() default "";
}
