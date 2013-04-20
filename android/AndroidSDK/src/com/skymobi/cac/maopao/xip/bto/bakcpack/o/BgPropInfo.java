/**
 * 
 */

package com.skymobi.cac.maopao.xip.bto.bakcpack.o;

import com.skymobi.bean.bytebean.ByteBean;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;

public class BgPropInfo implements ByteBean {

    @ByteField(index = 0, bytes = 4)
    private int propId;

    @ByteField(index = 1, bytes = 4)
    private int propNameLength;

    @ByteField(index = 2, length = 1)
    private String propName;

    @ByteField(index = 3, bytes = 1)
    private int propType;

    @ByteField(index = 4, bytes = 4)
    private int picAddrLength;

    @ByteField(index = 5, length = 4)
    private String picAddr;

    @ByteField(index = 6, bytes = 1)
    private int defaultOrder;

    @ByteField(index = 7, bytes = 1)
    private int divOrder;

    @ByteField(index = 8, bytes = 4)
    private int propNum;

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

    public int getPropId() {
        return propId;
    }

    public void setPropId(int propId) {
        this.propId = propId;
    }

    public int getPropNameLength() {
        return propNameLength;
    }

    public void setPropNameLength(int propNameLength) {
        this.propNameLength = propNameLength;
    }

    public String getPropName() {
        return propName;
    }

    public void setPropName(String propName) {
        this.propName = propName;
        propNameLength = this.propName == null ? 0 : this.propName.length() * 2;
    }

    public int getPropType() {
        return propType;
    }

    public void setPropType(int propType) {
        this.propType = propType;
    }

    /**
     * @return the propNum
     */
    public int getPropNum() {
        return propNum;
    }

    /**
     * @param propNum the propNum to set
     */
    public void setPropNum(int propNum) {
        this.propNum = propNum;
    }

}
