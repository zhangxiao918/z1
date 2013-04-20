
package com.skymobi.cac.maopao.xip.bto.pet;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipResp;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

/**
 * 通信协议处理类 用户任务获得女宠出战闺房调整请求的返回结果
 * 
 * @author shangguo.wu
 * @date 2013-03-05
 */
@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_DDZ_PET_BATTLE_BOUDOIR_CHANGE_RESP)
public class PlayerPetBattleBoudoirResp extends XipResp {
    @ByteField(index = 3, bytes = 1, description = "女宠出战闺房调整请求返回结果. 1.成功 ,-1. 失败 ,0.初始")
    private byte changeResult = 0;

    public byte getChangeResult() {
        return changeResult;
    }

    public void setChangeResult(byte changeResult) {
        this.changeResult = changeResult;
    }
}
