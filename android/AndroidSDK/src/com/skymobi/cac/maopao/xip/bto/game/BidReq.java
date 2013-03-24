package com.skymobi.cac.maopao.xip.bto.game;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;


@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_DDZ_BID_REQ)
public class BidReq extends XipBody {

    @ByteField(index = 0, bytes = 1, description = "叫的分值")
    private int bidVal;

    public int getBidVal() {
        return bidVal;
    }

    public void setBidVal(int bidVal) {
        this.bidVal = bidVal;
    }

}
