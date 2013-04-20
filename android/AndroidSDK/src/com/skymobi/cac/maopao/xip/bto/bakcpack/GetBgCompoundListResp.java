
package com.skymobi.cac.maopao.xip.bto.bakcpack;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipResp;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;
import com.skymobi.cac.maopao.xip.bto.bakcpack.o.BgCompoundInfo;

@XipSignal(messageCode = BasisMessageConstants.MSGCODE_BP_COMPOUND_LIST_RESQ)
public class GetBgCompoundListResp extends XipResp {

    @ByteField(index = 3, bytes = 1)
    private int bgCompoundLength;

    @ByteField(index = 4, length = 3)
    private BgCompoundInfo[] bgCompounds;

    public int getBgCompoundLength() {
        return bgCompoundLength;
    }

    public void setBgCompoundLength(int bgCompoundLength) {
        this.bgCompoundLength = bgCompoundLength;
    }

    public BgCompoundInfo[] getBgCompounds() {
        return bgCompounds;
    }

    public void setBgCompounds(BgCompoundInfo[] bgCompounds) {
        this.bgCompounds = bgCompounds;
        bgCompoundLength = bgCompounds.length;
    }
}
