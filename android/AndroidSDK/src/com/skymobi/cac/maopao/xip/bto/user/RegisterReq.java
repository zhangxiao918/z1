package com.skymobi.cac.maopao.xip.bto.user;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.XipResp;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;


@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_REGISTER_REQ)
public class RegisterReq extends XipBody {

    @ByteField(index = 0, bytes = 1)
    private int accountNameLen;

    @ByteField(index = 1, length = 0)
    private String accountName;

    @ByteField(index = 2, bytes = 1, description = "密码长度")
    private int pwdLen;

    @ByteField(index = 3, length = 2, description = "密码")
    private String pwd;

    @ByteField(index = 4, bytes = 1, description = "确认密码长度")
    private int confirmPwdLen;

    @ByteField(index = 5, length = 4, description = "确认密码")
    private String confirmPwd;

    @ByteField(index = 6, bytes = 1)
    private int nickNameLen;

    @ByteField(index = 7, length = 6)
    private String nickName;

    @ByteField(index = 8, bytes = 1)
    private int gender;

    public int getConfirmPwdLen() {
        return confirmPwdLen;
    }

    public String getConfirmPwd() {
        return confirmPwd;
    }

    public void setConfirmPwd(String confirmPwd) {
        this.confirmPwd = confirmPwd;
        this.confirmPwdLen = (null == this.confirmPwd ? 0 : this.confirmPwd.length() * 2);
    }

    public int getPwdLen() {
        return pwdLen;
    }

    public void setPwdLen(int pwdLen) {
        this.pwdLen = pwdLen;
    }

    public int getAccountNameLen() {
        return accountNameLen;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName.trim();
        this.accountNameLen = (null == this.accountName ? 0 : this.accountName.length() * 2);
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
        this.pwdLen = (null == this.pwd ? 0 : this.pwd.length() * 2);
    }

    public int getNickNameLen() {
        return nickNameLen;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
        this.nickNameLen = (null == this.nickName ? 0 : this.nickName.length() * 2);
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

}
