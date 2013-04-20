
package com.skymobi.cac.maopao.xip.bto.pet;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

/**
 * 通信协议处理类 用户任务女宠技能查看请求
 * 
 * @author shangguo.wu
 * @date 2013-03-05
 */
@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_DDZ_PET_SKILL_REQ)
public class PlayerPetSkillReq extends XipBody {
    @ByteField(index = 0, bytes = 4, description = "玩家ID")
    private long skyId = 0;

    @ByteField(index = 1, bytes = 4, description = "玩家女宠ID,不传则取出战女宠ID")
    private long playerPetId = 0;

    @ByteField(index = 2, bytes = 1, description = "请求显示类型：0-初始  ，1.打牌时技能展示, 2.单纯查看该女宠技能")
    private int showType = 0;

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

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }
}
