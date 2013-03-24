package com.skymobi.cac.maopao.xip.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME) 
public @interface XipSignal {
    public abstract int messageCode();
}
