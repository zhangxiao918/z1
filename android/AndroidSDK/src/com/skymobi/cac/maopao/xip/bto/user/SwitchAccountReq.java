
package com.skymobi.cac.maopao.xip.bto.user;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_SWITCH_ACCOUNT_REQ)
public class SwitchAccountReq extends XipBody {

    @ByteField(index = 0, bytes = 1)
    private int accountNameLen;

    @ByteField(index = 1, length = 0, description = "账户名称")
    private String accountName;

    @ByteField(index = 2, bytes = 1)
    private int pwdLen;

    @ByteField(index = 3, length = 2, description = "DES加密后的密码")
    private String pwd;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
        this.accountNameLen = (null == this.accountName ? 0 : this.accountName.length() * 2);
    }

    public int getPwdLen() {
        return pwdLen;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
        this.pwdLen = (null == this.pwd ? 0 : this.pwd.length() * 2);
    }

    public int getAccountNameLen() {
        return accountNameLen;
    }

}
