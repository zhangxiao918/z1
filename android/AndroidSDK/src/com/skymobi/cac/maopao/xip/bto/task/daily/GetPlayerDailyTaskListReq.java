
package com.skymobi.cac.maopao.xip.bto.task.daily;

import com.skymobi.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_GET_PLAYER_DAILY_TASK_REQ)
public class GetPlayerDailyTaskListReq extends XipBody {

    @ByteField(index = 0, bytes = 2, description = "游戏ID")
    private int gameId;

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

}
