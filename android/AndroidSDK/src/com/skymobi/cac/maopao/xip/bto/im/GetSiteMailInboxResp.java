
package com.skymobi.cac.maopao.xip.bto.im;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipResp;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;
import com.skymobi.cac.maopao.xip.bto.im.o.SiteMailHeader;

@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_SITEMAIL_INBOX_RESP)
public class GetSiteMailInboxResp extends XipResp {

    @ByteField(index = 3, bytes = 1)
    private int mailType;

    @ByteField(index = 4, bytes = 2)
    private int refreshInterval;

    @ByteField(index = 5)
    private byte mailLength;

    @ByteField(index = 6, length = 5)
    private SiteMailHeader[] mailList;

    public int getMailType() {
        return mailType;
    }

    public void setMailType(int mailType) {
        this.mailType = mailType;
    }

    public byte getMailLength() {
        return mailLength;
    }

    public void setMailLength(byte mailLength) {
        this.mailLength = mailLength;
    }

    public SiteMailHeader[] getMailList() {
        return mailList;
    }

    public void setMailList(SiteMailHeader[] mailList) {
        this.mailList = mailList;
        this.mailLength = (byte) ((this.mailList == null) ? 0 : this.mailList.length);
    }

    public int getRefreshInterval() {
        return refreshInterval;
    }

    public void setRefreshInterval(int refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

}
