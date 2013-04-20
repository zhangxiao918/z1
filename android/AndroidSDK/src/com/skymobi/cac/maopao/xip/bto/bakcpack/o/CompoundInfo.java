/**
 * 
 */

package com.skymobi.cac.maopao.xip.bto.bakcpack.o;

import com.skymobi.bean.bytebean.ByteBean;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;

import org.apache.commons.lang.StringUtils;

public class CompoundInfo implements ByteBean {

    @ByteField(index = 0, bytes = 4)
    private int compoundId;

    @ByteField(index = 1, bytes = 4)
    private int compoundNameLength;

    @ByteField(index = 2, length = 1)
    private String compoundName;

    @ByteField(index = 3, bytes = 1)
    private int compoundType;

    @ByteField(index = 4, bytes = 4)
    private int compoundDescLength;

    @ByteField(index = 5, length = 5)
    private String compoundDesc;

    @ByteField(index = 6, bytes = 4)
    private int price;

    @ByteField(index = 7, bytes = 4)
    private int picAddrLength;

    @ByteField(index = 8, length = 7)
    private String picAddr;

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
        this.picAddrLength = (StringUtils.isBlank(picAddr) ? 0 : picAddr.length() * 2);
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
        this.compoundNameLength = (StringUtils.isBlank(compoundName) ? 0
                : compoundName.length() * 2);
    }

    public int getCompoundType() {
        return compoundType;
    }

    public void setCompoundType(int compoundType) {
        this.compoundType = compoundType;
    }

    public int getCompoundDescLength() {
        return compoundDescLength;
    }

    public void setCompoundDescLength(int compoundDescLength) {
        this.compoundDescLength = compoundDescLength;
    }

    public String getCompoundDesc() {
        return compoundDesc;
    }

    public void setCompoundDesc(String compoundDesc) {
        this.compoundDesc = compoundDesc;
        this.compoundDescLength = (StringUtils.isBlank(compoundDesc) ? 0
                : compoundDesc.length() * 2);
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

}
