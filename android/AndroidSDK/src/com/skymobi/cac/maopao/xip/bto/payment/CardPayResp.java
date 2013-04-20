/*******************************************************************************
 * CopyRight  (c) 2005-2010 SKY-MOBI Ltd. All rights reserved.
 * Filename:  CardChargeResp.java
 * Creator:   xiaolei Wan
 * Version:   1.0
 * Date: 	  Jun 2, 2010 1:57:27 PM
 * Description: 
 *******************************************************************************/

package com.skymobi.cac.maopao.xip.bto.payment;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipResp;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

/**
 * @author xiaolei Wan
 */
@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_CARD_PAY_RESP)
public class CardPayResp extends XipResp {

    @ByteField(index = 3, bytes = 1, description = "商户号长度")
    private int merchantIdLen;

    @ByteField(index = 4, length = 3, description = "商品号(由斯卡分配)")
    private String merchantId;

    @ByteField(index = 5, bytes = 1, description = "商户号密码长度")
    private int merchantSignatureLen;

    @ByteField(index = 6, length = 5, description = "商户号密码")
    private String merchantSignature;

    @ByteField(index = 7, bytes = 2, description = "卡密服务端实例号")
    private int instanceId;

    @ByteField(index = 8, bytes = 1, description = "订单号长度")
    private int orderIdLen;

    @ByteField(index = 9, length = 9, description = "订单号(只能为数字)")
    private String orderId;

    @ByteField(index = 10, bytes = 4)
    private long appId;

    @ByteField(index = 11, bytes = 1)
    private int appNameLen;

    @ByteField(index = 12, length = 11)
    private String appName;

    @ByteField(index = 13, bytes = 4)
    private long exchangeRate;

    @ByteField(index = 14, bytes = 1)
    private int reserved1Len;

    @ByteField(index = 15, length = 14)
    private String reserved1;

    @ByteField(index = 16, bytes = 1)
    private int reserved2Len;

    @ByteField(index = 17, length = 16)
    private String reserved2;

    @ByteField(index = 18, bytes = 1)
    private int reserved3Len;

    @ByteField(index = 19, length = 18)
    private String reserved3;

    /**
     * @return the merchantIdLen
     */
    public int getMerchantIdLen() {
        return merchantIdLen;
    }

    /**
     * @param merchantIdLen the merchantIdLen to set
     */
    public void setMerchantIdLen(int merchantIdLen) {
        this.merchantIdLen = merchantIdLen;
    }

    /**
     * @return the merchantId
     */
    public String getMerchantId() {
        return merchantId;
    }

    /**
     * @param merchantId the merchantId to set
     */
    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
        this.merchantIdLen = (this.merchantId == null ? 0 : this.merchantId.length() * 2);
    }

    /**
     * @return the instanceId
     */
    public int getInstanceId() {
        return instanceId;
    }

    /**
     * @param instanceId the instanceId to set
     */
    public void setInstanceId(int instanceId) {
        this.instanceId = instanceId;
    }

    /**
     * @return the orderIdLen
     */
    public int getOrderIdLen() {
        return orderIdLen;
    }

    /**
     * @param orderIdLen the orderIdLen to set
     */
    public void setOrderIdLen(int orderIdLen) {
        this.orderIdLen = orderIdLen;
    }

    /**
     * @return the orderId
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * @param orderId the orderId to set
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
        this.orderIdLen = (this.orderId == null ? 0 : this.orderId.length() * 2);
    }

    public long getAppId() {
        return appId;
    }

    public void setAppId(long appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
        this.appNameLen = (this.appName == null ? 0 : this.appName.length() * 2);
    }

    public long getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(long exchangeRate) {
        this.exchangeRate = exchangeRate;
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

    public int getReserved1Len() {
        return reserved1Len;
    }

    public int getReserved2Len() {
        return reserved2Len;
    }

    public int getReserved3Len() {
        return reserved3Len;
    }

    public String getMerchantSignature() {
        return merchantSignature;
    }

    public void setMerchantSignature(String merchantSignature) {
        this.merchantSignature = merchantSignature;
        this.merchantSignatureLen = (this.merchantSignature == null ? 0 : this.merchantSignature
                .length() * 2);
    }

    public int getMerchantSignatureLen() {
        return merchantSignatureLen;
    }

}
