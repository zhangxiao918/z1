package com.skymobi.android.bean.pf.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME) 
public @interface PFSignal {
    public abstract int messageCode();
}
