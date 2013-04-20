package com.skymobi.cac.maopao.xip.bto.game.skill;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;


@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_DDZ_HALF_NOTIFY)
public class HalfNotify extends XipBody {

    @ByteField(index = 0, bytes = 1)
    private long seatId;
    
    public long getSeatId() {
    	return seatId;
    }

}
