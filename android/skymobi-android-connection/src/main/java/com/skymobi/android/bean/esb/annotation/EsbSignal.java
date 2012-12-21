package com.skymobi.android.bean.esb.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME) 
public @interface EsbSignal {
    public abstract int messageCode();
}
