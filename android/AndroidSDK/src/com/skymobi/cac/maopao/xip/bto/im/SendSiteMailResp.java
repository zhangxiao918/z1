
package com.skymobi.cac.maopao.xip.bto.im;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipResp;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_SITEMAIL_SEND_RESP)
public class SendSiteMailResp extends XipResp {

    @ByteField(index = 3, bytes = 4)
    private long sendMailId;

    @ByteField(index = 4, bytes = 4)
    private long mailId;

    @ByteField(index = 5, fixedLength = 38)
    private String createTime;

    public long getSendMailId() {
        return sendMailId;
    }

    public void setSendMailId(long sendMailId) {
        this.sendMailId = sendMailId;
    }

    public long getMailId() {
        return mailId;
    }

    public void setMailId(long mailId) {
        this.mailId = mailId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

}
