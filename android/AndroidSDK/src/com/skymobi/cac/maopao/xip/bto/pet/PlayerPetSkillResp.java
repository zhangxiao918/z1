
package com.skymobi.cac.maopao.xip.bto.pet;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipResp;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;
import com.skymobi.cac.maopao.xip.bto.pet.o.PlayerPetSkill;

/**
 * 通信协议处理类 用户任务女宠技能查看请求返回结果列表
 * 
 * @author shangguo.wu
 * @date 2013-03-05
 */
@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_DDZ_PET_SKILL_RESP)
public class PlayerPetSkillResp extends XipResp {
    @ByteField(index = 3, bytes = 1, description = "女宠技能查看请求返回结果. 1.成功 ，2.女宠ID不存在，且无出战女宠 ,-1. 失败 ,0.初始")
    private int result = 0;

    @ByteField(index = 4, bytes = 1, description = "女宠已学技能长度")
    private byte playerPetSkillNum;

    @ByteField(index = 5, length = 4, description = "女宠已学技能数组")
    private PlayerPetSkill[] playerPetSkill;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public byte getPlayerPetSkillNum() {
        return playerPetSkillNum;
    }

    public void setPlayerPetSkillNum(byte playerPetSkillNum) {
        this.playerPetSkillNum = playerPetSkillNum;
    }

    public PlayerPetSkill[] getPlayerPetSkill() {
        return playerPetSkill;
    }

    public void setPlayerPetSkill(PlayerPetSkill[] playerPetSkill) {
        this.playerPetSkillNum = (byte) (playerPetSkill == null ? 0 : playerPetSkill.length);
        this.playerPetSkill = playerPetSkill;
    }
}
