
package com.skymobi.cac.maopao.xip.bto.user;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipResp;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_FIND_PASSWORD_RESP)
public class FindPasswordResp extends XipResp {

    @ByteField(index = 3, bytes = 1)
    private int passwordLen;

    @ByteField(index = 4, length = 3, description = "新密码")
    private String password;

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
}
