
package com.skymobi.cac.maopao.xip.bto.task.accept;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipResp;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_GET_PLAYER_TASK_RESP)
public class GetPlayerTaskListResp extends XipResp {

    @ByteField(index = 3, bytes = 1, description = "新手任务数组长度")
    private int guideTaskListSize;

    @ByteField(index = 4, length = 3, description = "新手任务数组")
    private PlayerTaskInfo[] guideTaskList;

    public int getGuideTaskListSize() {
        return guideTaskListSize;
    }

    public PlayerTaskInfo[] getGuideTaskList() {
        return guideTaskList;
    }

    public void setGuideTaskList(PlayerTaskInfo[] guideTaskList) {
        this.guideTaskList = guideTaskList;
        this.guideTaskListSize = (null == this.guideTaskList ? 0 : this.guideTaskList.length);
    }

}
