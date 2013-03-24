/**
 * 
 */
package com.skymobi.cac.maopao.passport.android.bean.bytebean.type;

import java.io.UnsupportedEncodingException;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.ByteBean;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;



/**
 * @author isdom
 *
 */
public class TLV implements ByteBean {
    @ByteField(index = 0)
    private int tag;
    
    @ByteField(index = 1)
    private int length;
    
    @ByteField(index = 2, length= 1)
    private byte[] value;

    /**
     * @return the tag
     */
    public int getTag() {
        return tag;
    }

    /**
     * @param tag the tag to set
     */
    public void setTag(int tag) {
        this.tag = tag;
    }

    /**
     * @return the length
     */
    public int getLength() {
        return length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * @return the value
     */
    public byte[] getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(byte[] value) {
        this.value = value;
    }
    
    public String  genString(String charset) {
        try {
            if ( null != value ) {
                return  new String(value, charset);
            }
            else {
                return  "";
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    

}
