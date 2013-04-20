
package com.skymobi.cac.maopao.xip.bto.sys;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipResp;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_GET_FILE_SERVER_ADDR_RESP)
public class GetFileServerAddrResp extends XipResp {

    @ByteField(index = 3, bytes = 1)
    private int addrLen;

    @ByteField(index = 4, length = 3)
    private String addr;

    public int getAddrLen() {
        return addrLen;
    }

    public void setAddrLen(int addrLen) {
        this.addrLen = addrLen;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
        this.addrLen = (addr == null) ? 0 : (addr.length() * 2);
    }

}
