
package com.skymobi.cac.maopao.xip.bto.pet;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipResp;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

/**
 * 通信协议处理类 用户任务获得女宠请求的返回结果
 * 
 * @author shangguo.wu
 * @date 2013-03-05
 */
@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_DDZ_PET_ADD_RESP)
public class PlayerPetAddResp extends XipResp {
    @ByteField(index = 3, bytes = 4, description = "女宠请求返回结果. 1.成功 ,-1. 失败 ,0.初始")
    private int addResult = 0;

    @ByteField(index = 4, bytes = 8, description = "女宠ID")
    private long petId;

    @ByteField(index = 5, bytes = 1, description = "女宠昵称长度")
    private int petNickNameLen;

    @ByteField(index = 6, length = 5, description = "女宠昵称")
    private String petNickName;

    public int getAddResult() {
        return addResult;
    }

    public void setAddResult(int addResult) {
        this.addResult = addResult;
    }

    public long getPetId() {
        return petId;
    }

    public void setPetId(long petId) {
        this.petId = petId;
    }

    public int getPetNickNameLen() {
        return petNickNameLen;
    }

    public void setPetNickNameLen(int petNickNameLen) {
        this.petNickNameLen = petNickNameLen;
    }

    public String getPetNickName() {
        return petNickName;
    }

    public void setPetNickName(String petNickName) {
        this.petNickName = petNickName;
        this.petNickNameLen = (this.petNickName == null ? 0 : this.petNickName.length() * 2);
    }
}
