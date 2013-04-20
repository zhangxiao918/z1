/**
 * Dec 26, 2011 10:36:46 AM
 * com.skymobi.cac.sms.bto.SMSAccessPayInfo.java
 * xiaolei Wan
 */

package com.skymobi.cac.maopao.xip.bto.payment;

import com.skymobi.bean.bytebean.ByteBean;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;

/**
 * @author xiaolei Wan
 */
public class SMSAccessPayInfo implements ByteBean {

    @ByteField(index = 0, bytes = 4, description = "应用入口400001社区,1008套壳")
    private int portalId;

    @ByteField(index = 1, bytes = 4, description = "0:注册, 1:道具, 2:积分，3:充值")
    private int payType;

    @ByteField(index = 2, bytes = 4, description = "请求直付金额，单位为分")
    private int price;

    @ByteField(index = 3, bytes = 1, description = "付费原因长度")
    private int payTypeReasonLen;

    @ByteField(index = 4, length = 3, description = "付费原因")
    private String payTypeReason;

    @ByteField(index = 5, bytes = 2, description = "应用版本号")
    private int version;

    @ByteField(index = 6, bytes = 4, description = "安卓[2/1002]")
    private int terminalType;

    @ByteField(index = 7, bytes = 2, description = "游戏ID：斗地主[1]")
    private short gameId;

    @ByteField(index = 8, bytes = 4, description = "渠道号")
    private int enterId;

    @ByteField(index = 9, bytes = 4, description = "暂无意义，可不填")
    private int payEntryID;

    @ByteField(index = 10, bytes = 4, description = "上传安卓游戏版本号")
    private int androidGameVersion;

    public int getAndroidGameVersion() {
        return androidGameVersion;
    }

    public void setAndroidGameVersion(int androidGameVersion) {
        this.androidGameVersion = androidGameVersion;
    }

    public int getPayEntryID() {
        return payEntryID;
    }

    public void setPayEntryID(int payEntryID) {
        this.payEntryID = payEntryID;
    }

    public short getGameId() {
        return gameId;
    }

    public void setGameId(short gameId) {
        this.gameId = gameId;
    }

    public int getEnterId() {
        return enterId;
    }

    public void setEnterId(int enterId) {
        this.enterId = enterId;
    }

    public int getPortalId() {
        return portalId;
    }

    public void setPortalId(int portalId) {
        this.portalId = portalId;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPayTypeReason() {
        return payTypeReason;
    }

    public void setPayTypeReason(String payTypeReason) {
        this.payTypeReason = payTypeReason;
        this.payTypeReasonLen = (this.payTypeReason == null ? 0 : this.payTypeReason.length() * 2);
    }

    public int getPayTypeReasonLen() {
        return payTypeReasonLen;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(int terminalType) {
        this.terminalType = terminalType;
    }

    @Override
    public String toString() {
        return "SMSAccessPayInfo [payType=" + payType + ", payTypeReason=" + payTypeReason
                + ", payTypeReasonLen=" + payTypeReasonLen + ", portalId="
                + portalId + ", price=" + price + ", terminalType=" + terminalType + ", version="
                + version + "]";
    }

}
