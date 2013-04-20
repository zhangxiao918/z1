
package com.skymobi.cac.maopao.xip.bto.pet;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

/**
 * 通信协议处理类 用户任务女宠换装请求
 * 
 * @author shangguo.wu
 * @date 2013-03-05
 */
@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_DDZ_PET_CLOTHES_CHANGE_REQ)
public class PlayerPetClothesChangeReq extends XipBody {
    @ByteField(index = 0, bytes = 8, description = "玩家ID")
    private long skyId = 0;
    @ByteField(index = 1, bytes = 8, description = "玩家女宠ID")
    private long playerPetId = 0;
    @ByteField(index = 2, bytes = 8, description = "女宠时装ID")
    private long propId = 0;

    public long getSkyId() {
        return skyId;
    }

    public void setSkyId(long skyId) {
        this.skyId = skyId;
    }

    public long getPlayerPetId() {
        return playerPetId;
    }

    public void setPlayerPetId(long playerPetId) {
        this.playerPetId = playerPetId;
    }

    public long getPropId() {
        return propId;
    }

    public void setPropId(long propId) {
        this.propId = propId;
    }

}
