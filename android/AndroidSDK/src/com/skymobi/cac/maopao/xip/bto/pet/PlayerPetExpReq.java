
package com.skymobi.cac.maopao.xip.bto.pet;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.XipBody;

/**
 * 通信协议处理类 女宠经验值增加请求
 * 
 * @author shangguo.wu
 * @date 2013-03-05
 */
public class PlayerPetExpReq extends XipBody {
    @ByteField(index = 0, bytes = 8, description = "玩家ID")
    private long skyId = 0;
    @ByteField(index = 1, bytes = 8, description = "玩家女宠ID")
    private long playerPetId = 0;
    @ByteField(index = 2, bytes = 8, description = "玩家女宠ID")
    private long addExp;

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

    public long getAddExp() {
        return addExp;
    }

    public void setAddExp(long addExp) {
        this.addExp = addExp;
    }
}
