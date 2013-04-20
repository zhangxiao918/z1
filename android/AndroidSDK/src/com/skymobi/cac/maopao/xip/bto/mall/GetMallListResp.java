
package com.skymobi.cac.maopao.xip.bto.mall;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipResp;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;
import com.skymobi.cac.maopao.xip.bto.mall.o.WaresInfo;

@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_MALL_LIST_RESP)
public class GetMallListResp extends XipResp {

    @ByteField(index = 3, bytes = 1)
    private int waresLength;

    @ByteField(index = 4, length = 3)
    private WaresInfo[] wares;

    public int getWaresLength() {
        return waresLength;
    }

    public void setWaresLength(int waresLength) {
        this.waresLength = waresLength;
    }

    public WaresInfo[] getWares() {
        if (wares == null) {
            wares = new WaresInfo[0];
        }
        return wares;
    }

    public void setWares(WaresInfo[] wares) {
        this.waresLength = getWares().length;
        this.wares = wares;
    }

}
