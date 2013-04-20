
package com.skymobi.cac.maopao.xip.bto.pet;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipResp;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;
import com.skymobi.cac.maopao.xip.bto.pet.o.PlayerPet;

/**
 * 通信协议处理类 用户查看女宠信息的返回结果
 * 
 * @author shangguo.wu
 * @date 2013-03-05
 */
@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_DDZ_PET_SHOW_RESP)
public class PlayerPetShowResp extends XipResp {
    @ByteField(index = 3, bytes = 1, description = "女宠查看请求返回结果. 1.成功 ,-1. 失败 ,0.初始, 2.无宠物")
    private int showResult = 0;

    @ByteField(index = 4, bytes = 1, description = "除查询女宠以外，该玩家拥有的别的女宠基础信息的数组长度")
    private byte otherPlayerPetArrayLength;
    @ByteField(index = 5, length = 4, description = "除查询女宠以外，该玩家拥有的别的女宠基础信息,女宠编号#女宠昵称")
    private PlayerPet[] otherPlayerPetArray;

    @ByteField(index = 6, description = "女宠VO类")
    private PlayerPet playerPet;

    public int getShowResult() {
        return showResult;
    }

    public void setShowResult(int showResult) {
        this.showResult = showResult;
    }

    public PlayerPet getPlayerPet() {
        return playerPet;
    }

    public void setPlayerPet(PlayerPet playerPet) {
        this.playerPet = playerPet;
    }

    public byte getOtherPlayerPetArrayLength() {
        return otherPlayerPetArrayLength;
    }

    public void setOtherPlayerPetArrayLength(byte otherPlayerPetArrayLength) {
        this.otherPlayerPetArrayLength = otherPlayerPetArrayLength;
    }

    public PlayerPet[] getOtherPlayerPetArray() {
        return otherPlayerPetArray;
    }

    public void setOtherPlayerPetArray(PlayerPet[] otherPlayerPetArray) {
        this.otherPlayerPetArrayLength = (byte) (otherPlayerPetArray == null ? 0
                : otherPlayerPetArray.length);
        this.otherPlayerPetArray = otherPlayerPetArray;
    }
}
