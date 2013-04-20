
package com.skymobi.cac.maopao.xip.bto.bakcpack;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipResp;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;
import com.skymobi.cac.maopao.xip.bto.bakcpack.o.BgPropInfo;

@XipSignal(messageCode = BasisMessageConstants.MSGCODE_BP_PROP_LIST_RESQ)
public class GetBgPropListResp extends XipResp {
    @ByteField(index = 3, bytes = 1)
    private int bgPropsLength;

    @ByteField(index = 4, length = 3)
    private BgPropInfo[] bgProps;

    public int getBgPropsLength() {
        return bgPropsLength;
    }

    public void setBgPropsLength(int bgPropsLength) {
        this.bgPropsLength = bgPropsLength;
    }

    public BgPropInfo[] getBgProps() {
        return bgProps;
    }

    public void setBgProps(BgPropInfo[] bgProps) {
        this.bgProps = bgProps;
        bgPropsLength = bgProps.length;
    }
}
