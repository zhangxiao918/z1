
package com.skymobi.cac.maopao.xip.bto.pet;

import com.skymobi.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipResp;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

/**
 * 通信协议处理类 用户操作女宠信息的返回结果
 * 
 * @author shangguo.wu
 * @date 2013-03-05
 */
@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_DDZ_PET_OPER_RESP)
public class PlayerPetOperResp extends XipResp {
    @ByteField(index = 3, bytes = 4, description = "女宠操作返回结果 -1 失败  0 初始   1成功")
    private int operResult = 0;

    @ByteField(index = 9, bytes = 4, description = "女宠操作类型：1.缠绵  2-打牌结束体力经验增加   3-体力药水    ")
    private long operType = 0;

    public int getOperResult() {
        return operResult;
    }

    public void setOperResult(int operResult) {
        this.operResult = operResult;
    }

    public long getOperType() {
        return operType;
    }

    public void setOperType(long operType) {
        this.operType = operType;
    }

}
