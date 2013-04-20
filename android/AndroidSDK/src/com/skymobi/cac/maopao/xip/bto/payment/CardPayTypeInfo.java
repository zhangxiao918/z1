/**
 * 
 */

package com.skymobi.cac.maopao.xip.bto.payment;

import android.os.Parcel;
import android.os.Parcelable;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.ByteBean;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.Comparator;

/**
 * 支付类型详情
 * 
 * @author bluestome.zhang
 */
public class CardPayTypeInfo implements ByteBean, Parcelable {

    @ByteField(index = 0, bytes = 1, description = "卡密支付名称长度")
    private byte cardPayNameLen;

    @ByteField(index = 1, length = 0, description = "卡密支付名称")
    private String cardPayName;

    @ByteField(index = 2, bytes = 1, description = "卡密类型,支付时使用该值作为支付的支付参数之一")
    private int cardType;

    @ByteField(index = 3, bytes = 1, description = "图片URL长度")
    private byte urlLen;

    @ByteField(index = 4, length = 3, description = "图片URL")
    private String url;

    private int index;// 显示的顺序

    // 图片资源ID,主要是体现在部分支付图标保留在本地。
    private int resId;

    public static final Comparator<CardPayTypeInfo> comparator = new Comparator<CardPayTypeInfo>() {

        @Override
        public int compare(CardPayTypeInfo o1, CardPayTypeInfo o2) {
            return o1.index - o2.index;
        }

    };

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public byte getUrlLen() {
        return urlLen;
    }

    public void setUrlLen(byte urlLen) {
        this.urlLen = urlLen;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        this.urlLen = (null == url ? (byte) 0 : (byte) (url.length() * 2));
    }

    public byte getCardPayNameLen() {
        return cardPayNameLen;
    }

    public void setCardPayNameLen(byte cardPayNameLen) {
        this.cardPayNameLen = cardPayNameLen;
    }

    public String getCardPayName() {
        return cardPayName;
    }

    public void setCardPayName(String cardPayName) {
        this.cardPayName = cardPayName;
        this.cardPayNameLen = (null == cardPayName ? (byte) 0 : (byte) (cardPayName.length() * 2));
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(cardPayNameLen);
        dest.writeString(cardPayName);
        dest.writeInt(cardType);
        dest.writeByte(urlLen);
        dest.writeString(url);
    }

    public void readFromParcel(Parcel in) {
        cardPayNameLen = in.readByte();
        cardPayName = in.readString();
        cardType = in.readInt();
        urlLen = in.readByte();
        url = in.readString();
    }

    /**
     * @return the resId
     */
    public int getResId() {
        return resId;
    }

    /**
     * @param resId the resId to set
     */
    public void setResId(int resId) {
        this.resId = resId;
    }

}
