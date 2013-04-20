
package com.skymobi.cac.maopao.xip.bto.pet;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.passport.android.util.StringUtils;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

/**
 * 通信协议处理类 用户任务获得女宠请求
 * 
 * @author shangguo.wu
 * @date 2013-03-05
 */
@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_DDZ_PET_ADD_REQ)
public class PlayerPetAddReq extends XipBody {
    @ByteField(index = 0, bytes = 8, description = "女宠拥有者ID")
    private long skyId = 0;
    @ByteField(index = 1, bytes = 1, description = "女宠拥有者昵称长度")
    private int skyNicknameLen;
    @ByteField(index = 2, length = 1, description = "女宠拥有者昵称")
    private String skyNickname;
    @ByteField(index = 3, bytes = 4, description = "女宠获得类型 1.新手任务")
    private int addType = 0;
    @ByteField(index = 4, bytes = 4, description = "女宠类型 1.宫女 2.美人  3.郡主")
    private int petType = 1;

    public long getSkyId() {
        return skyId;
    }

    public void setSkyId(long skyId) {
        this.skyId = skyId;
    }

    public int getSkyNicknameLen() {
        return skyNicknameLen;
    }

    public void setSkyNicknameLen(int skyNicknameLen) {
        this.skyNicknameLen = skyNicknameLen;
    }

    public String getSkyNickname() {
        return skyNickname;
    }

    public void setSkyNickname(String skyNickname) {
        this.skyNickname = skyNickname;
        this.skyNicknameLen = (StringUtils.isBlank(this.skyNickname) ? 0 : this.skyNickname
                .length() * 2);
    }

    public int getAddType() {
        return addType;
    }

    public void setAddType(int addType) {
        this.addType = addType;
    }

    public int getPetType() {
        return petType;
    }

    public void setPetType(int petType) {
        this.petType = petType;
    }

}
