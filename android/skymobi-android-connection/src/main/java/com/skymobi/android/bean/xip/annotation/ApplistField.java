/**
 * 
 */
package com.skymobi.android.bean.xip.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author isdom
 *
 */
@Retention(RetentionPolicy.RUNTIME) 
public @interface ApplistField {
    /**
     * 娑堟伅浣撲腑鐨勭储寮曚綅缃�
     * @return
     */
    public abstract int index();

    /**
     * 鍦ㄦ秷鎭綋涓殑瀛楄妭闀垮害锛屼负-1鏃讹紝鍙栧瓧娈电被鍨嬬殑闀垮害
     * @return
     */
    public abstract int bytes() default -1;
    
    public abstract String customType() default "";
    
    public abstract String description() default "";
}
