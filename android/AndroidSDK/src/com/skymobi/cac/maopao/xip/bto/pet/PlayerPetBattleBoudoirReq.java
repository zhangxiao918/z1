
package com.skymobi.cac.maopao.xip.bto.pet;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

/**
 * 通信协议处理类 用户任务女宠出战闺房调整请求
 * 
 * @author shangguo.wu
 * @date 2013-03-05
 */
@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_DDZ_PET_BATTLE_BOUDOIR_CHANGE_REQ)
public class PlayerPetBattleBoudoirReq extends XipBody {
    @ByteField(index = 0, bytes = 4, description = "玩家ID")
    private long skyId = 0;
    @ByteField(index = 1, bytes = 4, description = "玩家女宠ID")
    private long playerPetId = 0;
    @ByteField(index = 2, bytes = 1, description = "操作类型  1-出战   2-闺房")
    private int operType = 0;
    @ByteField(index = 3, bytes = 1, description = "请求操作  1-出战，在闺房   0-不出战，不在闺房")
    private int oper = 0;

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

    public int getOperType() {
        return operType;
    }

    public void setOperType(int operType) {
        this.operType = operType;
    }

    public int getOper() {
        return oper;
    }

    public void setOper(int oper) {
        this.oper = oper;
    }
}
