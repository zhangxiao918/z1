/**
 * Dec 24, 2011 6:08:14 PM
 * com.skymobi.cac.sms.bto.SMSOrderInfo.java
 * xiaolei Wan
 *
 *
 */

package com.skymobi.cac.maopao.xip.bto.payment;

import com.skymobi.bean.bytebean.ByteBean;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author xiaolei Wan
 */
public class SMSOrderInfo implements ByteBean {
    @ByteField(index = 3, bytes = 1)
    private int appNameLen;

    @ByteField(index = 4, length = 3)
    private String appName;

    @ByteField(index = 5, bytes = 1)
    private int merchantIdLen;

    @ByteField(index = 6, length = 5)
    private String merchantId;

    @ByteField(index = 7, bytes = 1)
    private int merchantSignatureLen;

    @ByteField(index = 8, length = 7)
    private String merchantSignature;

    @ByteField(index = 9, bytes = 4)
    private int appId;

    @ByteField(index = 10, bytes = 4)
    private int portalId;

    @ByteField(index = 11, bytes = 2)
    private int smsInstanceId;

    @ByteField(index = 12, bytes = 1)
    private int smsOrderIdLen;

    @ByteField(index = 13, length = 12)
    private String smsOrderId;

    @ByteField(index = 14, bytes = 1)
    private int orderDescLen;

    @ByteField(index = 15, length = 14)
    private String orderDesc;

    @ByteField(index = 16, bytes = 1)
    private int titleLen;

    @ByteField(index = 17, length = 16)
    private String title;

    @ByteField(index = 18, bytes = 2)
    private int contentLen;

    @ByteField(index = 19, length = 18)
    private String content;

    @ByteField(index = 20, bytes = 1)
    private int reserved1Len;

    @ByteField(index = 21, length = 20, description = "保留字段1")
    private String reserved1;

    @ByteField(index = 22, bytes = 1)
    private int reserved2Len;

    @ByteField(index = 23, length = 22, description = "保留字段2")
    private String reserved2;

    @ByteField(index = 24, bytes = 1)
    private int reserved3Len;

    @ByteField(index = 25, length = 24, description = "保留字段3")
    private String reserved3;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
        this.appNameLen = (this.appName == null ? 0 : this.appName.length() * 2);
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
        this.merchantIdLen = (this.merchantId == null ? 0 : this.merchantId.length() * 2);
    }

    public String getMerchantSignature() {
        return merchantSignature;
    }

    public void setMerchantSignature(String merchantSignature) {
        this.merchantSignature = merchantSignature;
        this.merchantSignatureLen = (this.merchantSignature == null ? 0 : this.merchantSignature
                .length() * 2);
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public int getPortalId() {
        return portalId;
    }

    public void setPortalId(int portalId) {
        this.portalId = portalId;
    }

    public int getSmsInstanceId() {
        return smsInstanceId;
    }

    public void setSmsInstanceId(int smsInstanceId) {
        this.smsInstanceId = smsInstanceId;
    }

    public String getSmsOrderId() {
        return smsOrderId;
    }

    public void setSmsOrderId(String smsOrderId) {
        this.smsOrderId = smsOrderId;
        this.smsOrderIdLen = (this.smsOrderId == null ? 0 : this.smsOrderId.length() * 2);
    }

    public int getSmsOrderIdLen() {
        return smsOrderIdLen;
    }

    public String getOrderDesc() {
        return orderDesc;
    }

    public void setOrderDesc(String orderDesc) {
        this.orderDesc = orderDesc;
        this.orderDescLen = (this.orderDesc == null ? 0 : this.orderDesc.length() * 2);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        this.titleLen = (this.title == null ? 0 : this.title.length() * 2);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        this.contentLen = (this.content == null ? 0 : this.content.length() * 2);
    }

    public String getReserved1() {
        return reserved1;
    }

    public void setReserved1(String reserved1) {
        this.reserved1 = reserved1;
        this.reserved1Len = (this.reserved1 == null ? 0 : this.reserved1.length() * 2);
    }

    public String getReserved2() {
        return reserved2;
    }

    public void setReserved2(String reserved2) {
        this.reserved2 = reserved2;
        this.reserved2Len = (this.reserved2 == null ? 0 : this.reserved2.length() * 2);
    }

    public String getReserved3() {
        return reserved3;
    }

    public void setReserved3(String reserved3) {
        this.reserved3 = reserved3;
        this.reserved3Len = (this.reserved3 == null ? 0 : this.reserved3.length() * 2);
    }

    public int getAppNameLen() {
        return appNameLen;
    }

    public int getMerchantIdLen() {
        return merchantIdLen;
    }

    public int getMerchantSignatureLen() {
        return merchantSignatureLen;
    }

    public int getOrderDescLen() {
        return orderDescLen;
    }

    public int getTitleLen() {
        return titleLen;
    }

    public int getContentLen() {
        return contentLen;
    }

    public int getReserved1Len() {
        return reserved1Len;
    }

    public int getReserved2Len() {
        return reserved2Len;
    }

    public int getReserved3Len() {
        return reserved3Len;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.MULTI_LINE_STYLE);
    }
}
