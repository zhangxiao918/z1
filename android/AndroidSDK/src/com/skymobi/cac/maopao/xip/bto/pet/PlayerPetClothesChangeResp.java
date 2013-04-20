
package com.skymobi.cac.maopao.xip.bto.pet;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipResp;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

/**
 * 通信协议处理类 用户任务女宠换装请求的返回结果
 * 
 * @author shangguo.wu
 * @date 2013-03-05
 */
@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_DDZ_PET_CLOTHES_CHANGE_RESP)
public class PlayerPetClothesChangeResp extends XipResp {
    @ByteField(index = 3, bytes = 4, description = "女宠换装请求返回结果. 1.成功 ,-1. 失败 ,0.初始")
    private int changeResult = 0;

    public int getChangeResult() {
        return changeResult;
    }

    public void setChangeResult(int changeResult) {
        this.changeResult = changeResult;
    }
}
