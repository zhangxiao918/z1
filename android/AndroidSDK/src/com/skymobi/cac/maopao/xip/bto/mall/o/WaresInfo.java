/**
 * 
 */

package com.skymobi.cac.maopao.xip.bto.mall.o;

import com.skymobi.bean.bytebean.ByteBean;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;

import org.apache.commons.lang.StringUtils;

public class WaresInfo implements ByteBean {

    @ByteField(index = 0, bytes = 4)
    private int propId;

    @ByteField(index = 1, bytes = 4)
    private int propNameLength;

    @ByteField(index = 2, length = 1)
    private String propName;

    @ByteField(index = 3, bytes = 1)
    private int propType;

    @ByteField(index = 4, bytes = 4)
    private int propDescLength;

    @ByteField(index = 5, length = 4)
    private String propDesc;

    @ByteField(index = 6, bytes = 4)
    private int price;

    @ByteField(index = 7, bytes = 4)
    private int propPicLength;

    @ByteField(index = 8, length = 7)
    private String propPic;

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

    public int getPropDescLength() {
        return propDescLength;
    }

    public void setPropDescLength(int propDescLength) {
        this.propDescLength = propDescLength;
    }

    public String getPropDesc() {
        return propDesc;
    }

    public void setPropDesc(String propDesc) {
        this.propDesc = propDesc;
        propDescLength = this.propDesc == null ? 0 : this.propDesc.length() * 2;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPropPicLength() {
        return propPicLength;
    }

    public void setPropPicLength(int propPicLength) {
        this.propPicLength = propPicLength;
    }

    public String getPropPic() {
        return propPic;
    }

    public void setPropPic(String propPic) {
        this.propPic = propPic;
        this.propPicLength = (StringUtils.isBlank(propPic) ? 0 : propPic.length());
    }

}
