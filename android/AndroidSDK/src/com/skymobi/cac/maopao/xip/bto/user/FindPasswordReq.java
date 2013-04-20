
package com.skymobi.cac.maopao.xip.bto.user;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_FIND_PASSWORD_REQ)
// 0xDA13
public class FindPasswordReq extends XipBody {

    @ByteField(index = 0, bytes = 1, description = "账户名称长度")
    private int accountNameLen;

    @ByteField(index = 1, length = 0, description = "账户名称")
    private String accountName;

    @ByteField(index = 4, bytes = 1, description = "绑定的手机号码长度")
    private int phoneNumberLength;

    @ByteField(index = 5, length = 4, description = "绑定的手机号码DES加密")
    private String phoneNumber;

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        this.phoneNumberLength = (null == this.phoneNumber ? 0 : this.phoneNumber.length() * 2);
    }

    public int getPhoneNumberLength() {
        return phoneNumberLength;
    }

}
