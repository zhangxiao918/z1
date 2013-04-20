
package com.skymobi.cac.maopao.xip.bto.pet.o;

import com.skymobi.bean.bytebean.ByteBean;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;

/**
 * 斗地主宠物表情配置 数据传输模型
 * 
 * @author shangguo.wu
 * @date 2013-03-27
 */
public class PlayerPetExpression implements ByteBean {
    @ByteField(index = 0, bytes = 8, description = "女宠表情ID")
    private long petExpressionId;

    public PlayerPetExpression() {
        super();
    }

    public PlayerPetExpression(long petExpressionId) {
        super();
        this.petExpressionId = petExpressionId;
    }

    public long getPetExpressionId() {
        return petExpressionId;
    }

    public void setPetExpressionId(long petExpressionId) {
        this.petExpressionId = petExpressionId;
    }

}
