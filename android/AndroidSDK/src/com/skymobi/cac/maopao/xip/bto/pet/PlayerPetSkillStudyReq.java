
package com.skymobi.cac.maopao.xip.bto.pet;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

/**
 * 通信协议处理类 用户任务女宠技能学习请求
 * 
 * @author shangguo.wu
 * @date 2013-03-05
 */
@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_DDZ_PET_SKILL_STUDY_REQ)
public class PlayerPetSkillStudyReq extends XipBody {
    @ByteField(index = 0, bytes = 4, description = "玩家ID")
    private long skyId = 0;
    @ByteField(index = 1, bytes = 4, description = "玩家女宠ID")
    private long playerPetId = 0;
    @ByteField(index = 2, bytes = 4, description = "玩家要学习的女宠技能ID")
    private long propId = 0;
    @ByteField(index = 3, bytes = 1, description = "是否使用祝福糕  0-否  1-是")
    private int isZfg = 0;
    @ByteField(index = 4, bytes = 1, description = "使用祝福糕的位置 1,2,3,如第2个没有技能，直接学第3个会导致程序bug")
    private int zfgArea = 0;

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

    public int getIsZfg() {
        return isZfg;
    }

    public void setIsZfg(int isZfg) {
        this.isZfg = isZfg;
    }

    public int getZfgArea() {
        return zfgArea;
    }

    public void setZfgArea(int zfgArea) {
        this.zfgArea = zfgArea;
    }
}
