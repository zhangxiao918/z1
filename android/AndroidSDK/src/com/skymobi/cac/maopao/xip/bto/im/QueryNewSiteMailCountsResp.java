
package com.skymobi.cac.maopao.xip.bto.im;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipResp;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_SITEMAIL_NEW_COUNTS_RESP)
public class QueryNewSiteMailCountsResp extends XipResp {

    @ByteField(index = 3, bytes = 1, description = "新站内信数量")
    private int counts;

    @ByteField(index = 4, bytes = 1)
    private int publicMailCounts;

    public int getPublicMailCounts() {
        return publicMailCounts;
    }

    public void setPublicMailCounts(int publicMailCounts) {
        this.publicMailCounts = publicMailCounts;
    }

    public int getCounts() {
        return counts;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }

}
