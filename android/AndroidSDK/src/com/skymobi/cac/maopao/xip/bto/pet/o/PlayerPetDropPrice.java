
package com.skymobi.cac.maopao.xip.bto.pet.o;

import com.skymobi.bean.bytebean.ByteBean;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;

/**
 * 斗地主宠物掉落物品玩家定价 数据传输模型
 * 
 * @author shangguo.wu
 * @date 2013-03-27
 */
public class PlayerPetDropPrice implements ByteBean {
    @ByteField(index = 0, bytes = 4, description = "掉落物品ID")
    private long propId;

    @ByteField(index = 1, bytes = 4, description = "掉落物品玩家配置价格")
    private long propUnitPrice;

    public PlayerPetDropPrice() {
        super();
        // TODO Auto-generated constructor stub
    }

    public PlayerPetDropPrice(long propId, long propUnitPrice) {
        super();
        this.propId = propId;
        this.propUnitPrice = propUnitPrice;
    }

    public long getPropId() {
        return propId;
    }

    public void setPropId(long propId) {
        this.propId = propId;
    }

    public long getPropUnitPrice() {
        return propUnitPrice;
    }

    public void setPropUnitPrice(long propUnitPrice) {
        this.propUnitPrice = propUnitPrice;
    }

}
