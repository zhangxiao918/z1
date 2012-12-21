/**
 * 
 */
package com.skymobi.android.bean.tlv.annotation;

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
     * 在消息体中的字节长度，为-1时，取字段类型的长度
     * @return
     */
    public abstract int bytes() default -1;
}
