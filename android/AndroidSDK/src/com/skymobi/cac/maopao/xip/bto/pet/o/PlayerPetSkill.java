
package com.skymobi.cac.maopao.xip.bto.pet.o;

import com.skymobi.bean.bytebean.ByteBean;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;

/**
 * 斗地主宠物技能配置 数据传输模型
 * 
 * @author shangguo.wu
 * @date 2013-02-19
 */
public class PlayerPetSkill implements ByteBean {
    @ByteField(index = 0, bytes = 4, description = "女宠技能ID")
    private long petSkillId;

    @ByteField(index = 1, bytes = 1, description = "女宠技能顺序")
    private long orderId;

    public PlayerPetSkill() {
        super();
        // TODO Auto-generated constructor stub
    }

    public PlayerPetSkill(long petSkillId, long orderId) {
        super();
        this.petSkillId = petSkillId;
        this.orderId = orderId;
    }

    public long getPetSkillId() {
        return petSkillId;
    }

    public void setPetSkillId(long petSkillId) {
        this.petSkillId = petSkillId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }
}
