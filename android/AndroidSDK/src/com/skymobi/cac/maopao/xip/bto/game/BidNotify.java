package com.skymobi.cac.maopao.xip.bto.game;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;



@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_DDZ_BID_NOTIFY)
public class BidNotify extends XipBody {

    @ByteField(index = 1, bytes = 1, description = "座位号")
    private int seatId;

    @ByteField(index = 2, bytes = 1, description = "叫的分值")
    private int bidVal;

    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    public int getBidVal() {
        return bidVal;
    }

    public void setBidVal(int bidVal) {
        this.bidVal = bidVal;
    }

}
