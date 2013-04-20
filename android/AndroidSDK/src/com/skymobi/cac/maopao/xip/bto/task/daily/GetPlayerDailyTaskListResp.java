
package com.skymobi.cac.maopao.xip.bto.task.daily;

import com.skymobi.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipResp;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_GET_PLAYER_DAILY_TASK_RESP)
public class GetPlayerDailyTaskListResp extends XipResp {

    @ByteField(index = 3, bytes = 1, description = "日常任务数组长度")
    private int dailyTaskListLen;

    @ByteField(index = 4, length = "dailyTaskListLen", description = "日常任务数组")
    private PlayerDailyTaskInfo[] playerDailyTaskInfoList;

    public int getDailyTaskListLen() {
        return dailyTaskListLen;
    }

    public PlayerDailyTaskInfo[] getPlayerDailyTaskInfoList() {
        return playerDailyTaskInfoList;
    }

    public void setPlayerDailyTaskInfoList(PlayerDailyTaskInfo[] playerDailyTaskInfoList) {
        this.playerDailyTaskInfoList = playerDailyTaskInfoList;
        this.dailyTaskListLen = (null == this.playerDailyTaskInfoList ? 0
                : this.playerDailyTaskInfoList.length);
    }

}
