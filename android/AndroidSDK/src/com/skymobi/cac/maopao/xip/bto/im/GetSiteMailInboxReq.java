
package com.skymobi.cac.maopao.xip.bto.im;

import com.skymobi.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_SITEMAIL_INBOX_REQ)
public class GetSiteMailInboxReq extends XipBody {

    @ByteField(index = 0, bytes = 1)
    private int mailType;

    public int getMailType() {
        return mailType;
    }

    public void setMailType(int mailType) {
        this.mailType = mailType;
    }

}
