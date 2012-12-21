/**
 * 
 */
package com.skymobi.android.bean.pf.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.skymobi.android.bean.bytebean.core.ByteFieldCodec;
import com.skymobi.android.util.ByteOrder;


/**
 * @author isdom
 *
 */
@Retention(RetentionPolicy.RUNTIME) 
public @interface PFField {
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
    
    public abstract String length() default "";
    
    public abstract String charset() default "UTF-8";
    
    public abstract ByteOrder byteOrder() default ByteOrder.None;
    
    public abstract String description() default "";
    
    public abstract Class<ByteFieldCodec> customCodec() default ByteFieldCodec.class;
}
