/**
 * Dec 18, 2011 4:23:52 PM
 * com.skymobi.cac.card.bto.xip.CardChargeReq.java
 * xiaolei Wan
 */

package com.skymobi.cac.maopao.xip.bto.payment;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

/**
 * @author xiaolei Wan
 */
@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_CARD_PAY_REQ)
public class CardChargeReq extends CardPayReq {

    @ByteField(index = 14, bytes = 2, description = "游戏ID")
    private short gameId;

    @ByteField(index = 15, bytes = 4, description = "入口ID")
    private int enterId;

    @ByteField(index = 16, bytes = 4)
    private int payEntryID;

    @ByteField(index = 17, bytes = 4)
    private int androidGameVersion;

    public int getAndroidGameVersion() {
        return androidGameVersion;
    }

    public void setAndroidGameVersion(int androidGameVersion) {
        this.androidGameVersion = androidGameVersion;
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

    public int getPayEntryID() {
        return payEntryID;
    }

    public void setPayEntryID(int payEntryID) {
        this.payEntryID = payEntryID;
    }

}
