
package com.skymobi.cac.maopao.xip.bto.user;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_BIND_PHONE_REQ)
public class BindPhoneReq extends XipBody {

    @ByteField(index = 0, bytes = 1, description = "账户名称长度")
    private int accountNameLen;

    @ByteField(index = 1, length = 0, description = "账户名称")
    private String accountName;

    @ByteField(index = 2, bytes = 1, description = "密码长度")
    private int passwordLen;

    @ByteField(index = 3, length = 2, description = "密码DES加密")
    private String password;

    @ByteField(index = 4, bytes = 1, description = "手机号码长度")
    private int phoneNumberLength;

    @ByteField(index = 5, length = 4, description = "手机号码DES加密")
    private String phoneNumber;

    @ByteField(index = 6, bytes = 1, description = "手机号码长度")
    private int phoneNumberDuplicateLength;

    @ByteField(index = 7, length = 6, description = "手机号码DES加密")
    private String phoneNumberDuplicate;

    public int getAccountNameLen() {
        return accountNameLen;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
        this.accountNameLen = (null == this.accountName ? 0 : accountName.length() * 2);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        this.passwordLen = (null == this.password ? 0 : this.password.length() * 2);
    }

    public int getPasswordLen() {
        return passwordLen;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        this.phoneNumberLength = (null == this.phoneNumber ? 0 : this.phoneNumber.length() * 2);
    }

    public String getPhoneNumberDuplicate() {
        return phoneNumberDuplicate;
    }

    public void setPhoneNumberDuplicate(String phoneNumberDuplicate) {
        this.phoneNumberDuplicate = phoneNumberDuplicate;
        this.phoneNumberDuplicateLength = (null == this.phoneNumberDuplicate ? 0
                : this.phoneNumberDuplicate.length() * 2);
    }

    public int getPhoneNumberLength() {
        return phoneNumberLength;
    }

    public int getPhoneNumberDuplicateLength() {
        return phoneNumberDuplicateLength;
    }

}
