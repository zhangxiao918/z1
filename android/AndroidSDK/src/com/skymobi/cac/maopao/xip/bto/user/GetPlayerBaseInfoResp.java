/*******************************************************************************
 * CopyRight (c) 2005-2010 SKY-MOBI Ltd. All rights reserved.
 * Filename: GetPlayerBaseInfoReq.java
 * Creator: Qi Wang
 * Version: 1.0
 * Date: 2010-5-26 下午04:39:46
 * Description:
 *******************************************************************************/

package com.skymobi.cac.maopao.xip.bto.user;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipResp;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_GET_PLAYER_BASE_INFO_RESP)
// DA10
public class GetPlayerBaseInfoResp extends XipResp {

    @ByteField(index = 3, bytes = 1)
    private int accountNameLen;

    @ByteField(index = 4, length = 3)
    private String accountName;

    @ByteField(index = 5, bytes = 1)
    private int nickNameLen;

    @ByteField(index = 6, length = 5)
    private String nickName;

    @ByteField(index = 7, bytes = 2)
    private int portraitId;

    @ByteField(index = 8, bytes = 1)
    private int gender;

    @ByteField(index = 9, bytes = 1)
    private int age;

    @ByteField(index = 10, bytes = 4)
    private long gold;// 金豆

    @ByteField(index = 11, bytes = 4)
    private long yb;// 元宝

    public int getAccountNameLen() {
        return accountNameLen;
    }

    public void setAccountNameLen(int accountNameLen) {
        this.accountNameLen = accountNameLen;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
        this.accountNameLen = (accountName == null) ? 0 : accountName.length() * 2;
    }

    public int getNickNameLen() {
        return nickNameLen;
    }

    public void setNickNameLen(int nickNameLen) {
        this.nickNameLen = nickNameLen;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
        this.nickNameLen = (nickName == null) ? 0 : nickName.length() * 2;
    }

    public int getPortraitId() {
        return portraitId;
    }

    public void setPortraitId(int portraitId) {
        this.portraitId = portraitId;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long getGold() {
        return gold;
    }

    public void setGold(long gold) {
        this.gold = gold;
    }

    public long getYb() {
        return yb;
    }

    public void setYb(long yb) {
        this.yb = yb;
    }

}
