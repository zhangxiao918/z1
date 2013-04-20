
package com.skymobi.cac.maopao.xip.bto.task.accept;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

/**
 * 获取已接任务列表
 */
@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_GET_PLAYER_TASK_REQ)
public class GetPlayerTaskListReq extends XipBody {

    @ByteField(index = 0, bytes = 2, description = "游戏ID")
    private int gameId;

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

}
