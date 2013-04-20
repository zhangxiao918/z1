
package com.skymobi.cac.maopao.xip.bto.im;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipResp;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_SITEMAIL_CONTENT_RESP)
public class GetSiteMailContentResp extends XipResp {

    @ByteField(index = 3, bytes = 1)
    private int mailType;

    @ByteField(index = 4, bytes = 4)
    private long mailId;

    @ByteField(index = 5, bytes = 2)
    private int mailContentLength;

    @ByteField(index = 6, length = 5)
    private String mailContent;

    public int getMailType() {
        return mailType;
    }

    public void setMailType(int mailType) {
        this.mailType = mailType;
    }

    public int getMailContentLength() {
        return mailContentLength;
    }

    public void setMailContentLength(int mailContentLength) {
        this.mailContentLength = mailContentLength;
    }

    public long getMailId() {
        return mailId;
    }

    public void setMailId(long mailId) {
        this.mailId = mailId;
    }

    public String getMailContent() {
        return mailContent;
    }

    public void setMailContent(String mailContent) {
        this.mailContent = mailContent;
        this.mailContentLength = (mailContent == null) ? 0 : (mailContent.length() * 2);
    }

}
