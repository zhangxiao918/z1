
package com.skymobi.cac.maopao.xip.bto.pet.o;

import com.skymobi.bean.bytebean.ByteBean;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;

/**
 * 斗地主宠物时装信息 数据传输模型
 * 
 * @author shangguo.wu
 * @date 2013-03-27
 */
public class PlayerPetClothes implements ByteBean {
    @ByteField(index = 0, bytes = 4, description = "时装ID")
    private long propId;

    @ByteField(index = 1, bytes = 4, description = "时装类型")
    private int clothesType;

    public PlayerPetClothes() {
        super();
        // TODO Auto-generated constructor stub
    }

    public PlayerPetClothes(long propId, int clothesType) {
        super();
        this.propId = propId;
        this.clothesType = clothesType;
    }

    public long getPropId() {
        return propId;
    }

    public void setPropId(long propId) {
        this.propId = propId;
    }

    public long getClothesType() {
        return clothesType;
    }

    public void setClothesType(int clothesType) {
        this.clothesType = clothesType;
    }

}
