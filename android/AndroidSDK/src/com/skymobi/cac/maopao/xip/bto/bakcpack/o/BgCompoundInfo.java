/**
 * 
 */

package com.skymobi.cac.maopao.xip.bto.bakcpack.o;

import com.skymobi.bean.bytebean.ByteBean;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;

public class BgCompoundInfo implements ByteBean {

    @ByteField(index = 0, bytes = 4)
    private int compoundId;

    @ByteField(index = 1, bytes = 4)
    private int compoundNameLength;

    @ByteField(index = 2, length = 1)
    private String compoundName;

    @ByteField(index = 3, bytes = 1)
    private int compoundType;

    @ByteField(index = 4, bytes = 4)
    private int picAddrLength;

    @ByteField(index = 5, length = 4)
    private String picAddr;

    @ByteField(index = 6, bytes = 1)
    private int defaultOrder;

    @ByteField(index = 7, bytes = 1)
    private int divOrder;

    @ByteField(index = 8, bytes = 4)
    private int compoundNum;

    public int getDefaultOrder() {
        return defaultOrder;
    }

    public void setDefaultOrder(int defaultOrder) {
        this.defaultOrder = defaultOrder;
    }

    public int getDivOrder() {
        return divOrder;
    }

    public void setDivOrder(int divOrder) {
        this.divOrder = divOrder;
    }

    public int getPicAddrLength() {
        return picAddrLength;
    }

    public void setPicAddrLength(int picAddrLength) {
        this.picAddrLength = picAddrLength;
    }

    public String getPicAddr() {
        return picAddr;
    }

    public void setPicAddr(String picAddr) {
        this.picAddr = picAddr;
        picAddrLength = this.picAddr == null ? 0 : this.picAddr.length() * 2;
    }

    public int getCompoundId() {
        return compoundId;
    }

    public void setCompoundId(int compoundId) {
        this.compoundId = compoundId;
    }

    public int getCompoundNameLength() {
        return compoundNameLength;
    }

    public void setCompoundNameLength(int compoundNameLength) {
        this.compoundNameLength = compoundNameLength;
    }

    public String getCompoundName() {
        return compoundName;
    }

    public void setCompoundName(String compoundName) {
        this.compoundName = compoundName;
        compoundNameLength = this.compoundName == null ? 0 : this.compoundName.length() * 2;
    }

    public int getCompoundType() {
        return compoundType;
    }

    public void setCompoundType(int compoundType) {
        this.compoundType = compoundType;
    }

    /**
     * @return the compoundNum
     */
    public int getCompoundNum() {
        return compoundNum;
    }

    /**
     * @param compoundNum the compoundNum to set
     */
    public void setCompoundNum(int compoundNum) {
        this.compoundNum = compoundNum;
    }

}
