
package com.skymobi.cac.maopao.xip.bto.im;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_SITEMAIL_CONTENT_REQ)
public class GetSiteMailContentReq extends XipBody {

    @ByteField(index = 0, bytes = 4)
    private long siteMailId;

    @ByteField(index = 1, bytes = 1)
    private int mailType;

    public int getMailType() {
        return mailType;
    }

    public void setMailType(int mailType) {
        this.mailType = mailType;
    }

    public long getSiteMailId() {
        return siteMailId;
    }

    public void setSiteMailId(long siteMailId) {
        this.siteMailId = siteMailId;
    }

}
