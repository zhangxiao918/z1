package com.skymobi.android.bean.xip.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME) 
public @interface SsipSignal {
    public abstract int messageCode();
}
