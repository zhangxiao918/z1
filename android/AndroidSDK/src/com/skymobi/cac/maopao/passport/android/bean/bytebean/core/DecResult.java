/**
 * 
 */
package com.skymobi.cac.maopao.passport.android.bean.bytebean.core;

/**
 * @author isdom
 *
 */
public class DecResult {
    private Object  value;
    private byte[]  bytes;
    
    public DecResult(Object value, byte[] bytes) {
        this.value = value;
        this.bytes = bytes;
    }
    
    public  Object  getValue() {
        return  value;
    }
    
    public  byte[]  getRemainBytes() {
        return  bytes;
    }
}
