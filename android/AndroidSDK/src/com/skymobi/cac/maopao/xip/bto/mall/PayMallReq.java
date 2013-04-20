
package com.skymobi.cac.maopao.xip.bto.mall;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_MALL_PAY_REQ)
public class PayMallReq extends XipBody {

    @ByteField(index = 0, bytes = 4, description = "道具ID")
    private int propId;

    @ByteField(index = 1, bytes = 2, description = "道具数量")
    private int num;

    public int getPropId() {
        return propId;
    }

    public void setPropId(int propId) {
        this.propId = propId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

}
