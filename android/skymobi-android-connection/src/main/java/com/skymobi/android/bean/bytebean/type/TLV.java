/**
 * 
 */
package com.skymobi.android.bean.bytebean.type;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.skymobi.android.bean.bytebean.ByteBean;
import com.skymobi.android.bean.bytebean.annotation.ByteField;

/**
 * @author isdom
 *
 */
public class TLV implements ByteBean {
    @ByteField(index = 0)
    private int tag;
    
    @ByteField(index = 1)
    private int length;
    
    @ByteField(index = 2, length="length")
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
    
    /* (non-Javadoc)
     * @see com.sky.applist20.xip.core.AbstractXipBean#toString()
     */
    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE);
        return  builder.append("tag", tag)
                .append("length", length)
                .append("value.length", (null != value ? value.length : 0) )
                .toString();
    }
}
