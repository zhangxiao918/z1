
package com.skymobi.cac.maopao.xip.bto.task.challenge;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipResp;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_GET_PLAYER_CURRENT_CHALLENGE_TASK_RESP)
public class GetPlayerChallengeTaskListResp extends XipResp {

    @ByteField(index = 3, bytes = 1, description = "挑战任务数组长度")
    private int taskListSize;

    @ByteField(index = 4, length = 3, description = "挑战任务数组")
    private PlayerChallengeTaskInfo[] taskList;

    public int getTaskListSize() {
        return taskListSize;
    }

    public PlayerChallengeTaskInfo[] getTaskList() {
        return taskList;
    }

    public void setTaskList(PlayerChallengeTaskInfo[] taskList) {
        this.taskList = taskList;
        this.taskListSize = (null == this.taskList ? 0 : this.taskList.length);
    }

}
