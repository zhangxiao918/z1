package com.skymobi.cac.maopao.xip.bto.user;


import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.XipResp;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

// 第三方登陆请求
@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_THIRD_LOGIN_REQ)
public class ThirdLoginReq extends XipBody {

    @ByteField(index = 0, bytes = 1)
    private int openPlatformType;

    @ByteField(index = 1, bytes = 1)
    private int openIdLength;

    @ByteField(index = 2, length = 1)
    private String openId;

    @ByteField(index = 3, bytes = 1)
    private int nickNameLength;

    @ByteField(index = 4, length = 3)
    private String nickName;

    public int getOpenPlatformType() {
        return openPlatformType;
    }

    public void setOpenPlatformType(int openPlatformType) {
        this.openPlatformType = openPlatformType;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
        this.openIdLength = (null == this.openId ? 0 : this.openId.length() * 2);
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
        this.nickNameLength = (null == this.nickName ? 0 : this.nickName.length() * 2);
    }

    public int getOpenIdLength() {
        return openIdLength;
    }

    public int getNickNameLength() {
        return nickNameLength;
    }
}
