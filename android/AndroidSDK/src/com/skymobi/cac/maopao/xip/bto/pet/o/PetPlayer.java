
package com.skymobi.cac.maopao.xip.bto.pet.o;

import com.skymobi.bean.bytebean.ByteBean;
import com.skymobi.bean.bytebean.annotation.ByteField;

/**
 * 斗地主玩家信息 数据传输模型
 * 
 * @author shangguo.wu
 * @date 2013-04-09
 */
public class PetPlayer implements ByteBean {
    @ByteField(index = 0, bytes = 4, description = "玩家宠物ID")
    private long skyId;

    @ByteField(index = 1, bytes = 1, description = "女宠数组长度")
    private byte playerPetsNum;

    @ByteField(index = 2, length = "playerPetsNum", description = "女宠数组")
    private PlayerPet[] playerPets;

    public long getSkyId() {
        return skyId;
    }

    public void setSkyId(long skyId) {
        this.skyId = skyId;
    }

    public byte getPlayerPetsNum() {
        return playerPetsNum;
    }

    public void setPlayerPetsNum(byte playerPetsNum) {
        this.playerPetsNum = playerPetsNum;
    }

    public PlayerPet[] getPlayerPets() {
        return playerPets;
    }

    public void setPlayerPets(PlayerPet[] playerPets) {
        this.playerPetsNum = (byte) (playerPets == null ? 0 : playerPets.length);
        this.playerPets = playerPets;
    }
}
