
package com.skymobi.cac.maopao.xip.bto.user;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_MODIFY_PASS_WORD_REQ)
public class ModifyPasswordReq extends XipBody {

    @ByteField(index = 0, bytes = 1, description = "账户名称长度")
    private int accountNameLen;

    @ByteField(index = 1, length = 0, description = "账户名称")
    private String accountName;

    @ByteField(index = 2, bytes = 1, description = "原始密码长度")
    private int oldPasswordLen;

    @ByteField(index = 3, length = 2, description = "原始密码")
    private String oldPassword;

    @ByteField(index = 4, bytes = 1, description = "新密码长度")
    private int newPasswordLength;

    @ByteField(index = 5, length = 4, description = "新密码")
    private String newPassword;

    @ByteField(index = 6, bytes = 1, description = "重复新密码长度")
    private int newPasswordDuplicateLength;

    @ByteField(index = 7, length = 6, description = "重复新密码")
    private String newPasswordDuplicate;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
        this.accountNameLen = (null == this.accountName ? 0 : this.accountName.length() * 2);
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
        this.oldPasswordLen = (null == this.oldPassword ? 0 : this.oldPassword.length() * 2);
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
        this.newPasswordLength = (null == this.newPassword ? 0 : this.newPassword.length() * 2);
    }

    public String getNewPasswordDuplicate() {
        return newPasswordDuplicate;
    }

    public void setNewPasswordDuplicate(String newPasswordDuplicate) {
        this.newPasswordDuplicate = newPasswordDuplicate;
        this.newPasswordDuplicateLength = (null == this.newPasswordDuplicate ? 0
                : this.newPasswordDuplicate.length() * 2);
    }

    public int getAccountNameLen() {
        return accountNameLen;
    }

    public int getOldPasswordLen() {
        return oldPasswordLen;
    }

    public int getNewPasswordLength() {
        return newPasswordLength;
    }

    public int getNewPasswordDuplicateLength() {
        return newPasswordDuplicateLength;
    }

}
