/*******************************************************************************
 * CopyRight (c) 2005-2010 SKY-MOBI Ltd. All rights reserved.
 * Filename: CardChargeReq.java
 * Creator: xiaolei Wan
 * Version: 1.0
 * Date: Jun 2, 2010 1:56:59 PM
 * Description:
 *******************************************************************************/

package com.skymobi.cac.maopao.xip.bto.payment;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.XipBody;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author xiaolei Wan
 */
public class CardPayReq extends XipBody {

    @ByteField(index = 0, bytes = 4, description = "斯凯ID")
    private long skyId;

    @ByteField(index = 1, bytes = 1, description = "商品号长度")
    private int productIdLen;

    @ByteField(index = 2, length = 1, description = "商品号(只能为数字，默认为0)")
    private String productId;

    @ByteField(index = 3, bytes = 1, description = "商品号名称长度")
    private int productNameLen;

    @ByteField(index = 4, length = 3, description = "商品名称（utf16-be编码）(英文gold)")
    private String productName;

    @ByteField(index = 5, bytes = 1, description = "卡号长度")
    private int cardNumLen;

    @ByteField(index = 6, length = 5, description = "卡号")
    private String cardNum;

    @ByteField(index = 7, bytes = 1, description = "卡密码长度")
    private int cardPassWordLen;

    @ByteField(index = 8, length = 7, description = "卡密码")
    private String cardPassWord;

    @ByteField(index = 9, bytes = 2, description = "充值类类别,10神州行、11盛大")
    private int cardType;

    @ByteField(index = 10, bytes = 2, description = "充值金额,整形数据，以分为单位")
    private int amount;

    @ByteField(index = 11, bytes = 4)
    private long portalId;

    @ByteField(index = 12, bytes = 2, description = "版本")
    private int version;

    @ByteField(index = 13, bytes = 4, description = "终端类型 1001:表示山寨, 1002:表示android, 1003:表示WP等")
    private int terminalType;

    public long getSkyId() {
        return skyId;
    }

    public void setSkyId(long skyId) {
        this.skyId = skyId;
    }

    public int getProductIdLen() {
        return productIdLen;
    }

    public void setProductIdLen(int productIdLen) {
        this.productIdLen = productIdLen;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
        this.productIdLen = (this.productId == null ? 0 : this.productId.length() * 2);
    }

    public int getProductNameLen() {
        return productNameLen;
    }

    public void setProductNameLen(int productNameLen) {
        this.productNameLen = productNameLen;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
        this.productNameLen = (this.productName == null ? 0 : this.productName.length() * 2);
    }

    public int getCardNumLen() {
        return cardNumLen;
    }

    public void setCardNumLen(int cardNumLen) {
        this.cardNumLen = cardNumLen;
    }

    /**
     * @return the cardNum
     */
    public String getCardNum() {
        return cardNum;
    }

    /**
     * @param cardNum the cardNum to set
     */
    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
        this.cardNumLen = (this.cardNum == null ? 0 : this.cardNum.length() * 2);
    }

    public int getCardPassWordLen() {
        return cardPassWordLen;
    }

    public void setCardPassWordLen(int cardPassWordLen) {
        this.cardPassWordLen = cardPassWordLen;
    }

    public String getCardPassWord() {
        return cardPassWord;
    }

    public void setCardPassWord(String cardPassWord) {
        this.cardPassWord = cardPassWord;
        this.cardPassWordLen = (this.cardPassWord == null ? 0 : this.cardPassWord.length() * 2);
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public long getPortalId() {
        return portalId;
    }

    public void setPortalId(long portalId) {
        this.portalId = portalId;
    }

    public int getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(int terminalType) {
        this.terminalType = terminalType;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
