package com.skymobi.cac.maopao.xip.bto.user;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_USER_LOGIN_REQ)
public class LoginReq extends XipBody {

    @ByteField(index = 1, bytes = 1)
    private int accountNameLen;

    @ByteField(index = 2, length = 1, description = "账户名称")
    private String accountName;

    @ByteField(index = 3, bytes = 1)
    private int pwdLen;

    @ByteField(index = 4, length = 3, description = "密码")
    private String pwd;

    @ByteField(index = 5, bytes = 1, description = "账号类型,0:平台账户")
    private int accountType;

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
        this.accountNameLen = (null == accountName ? 0 : accountName.length() * 2);
    }

    public int getPwdLen() {
        return pwdLen;
    }

    public String getPwd() {
        return pwd;

    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
        this.pwdLen = (null == pwd ? 0 : pwd.length() * 2);
    }

    public int getAccountNameLen() {
        return accountNameLen;
    }

}
