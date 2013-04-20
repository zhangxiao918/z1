
package com.skymobi.cac.maopao.xip.bto.pet.o;

import com.skymobi.bean.bytebean.ByteBean;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.passport.android.util.StringUtils;

/**
 * 斗地主宠物信息 数据传输模型
 * 
 * @author shangguo.wu
 * @date 2013-03-27
 */
public class PlayerPet implements ByteBean {
    @ByteField(index = 0, bytes = 4, description = "玩家宠物ID")
    private long playerPetId;

    @ByteField(index = 1, bytes = 4, description = "斯凯ID")
    private long skyId;

    @ByteField(index = 2, bytes = 4, description = "女宠类型ID")
    private long petTypeId;

    @ByteField(index = 3, bytes = 2, description = "女宠昵称长度")
    private int petNickNameLength;
    @ByteField(index = 4, length = 3, description = "女宠昵称")
    private String petNickName;

    @ByteField(index = 5, bytes = 1, description = "女宠等级")
    private int petLevel;

    @ByteField(index = 6, bytes = 4, description = "女宠经验")
    private long petExp;

    @ByteField(index = 7, bytes = 4, description = "女宠当前体力")
    private int staminaValueRemain;

    @ByteField(index = 8, bytes = 4, description = "女宠该等级最大体力值上限")
    private int staminaValueBase;

    @ByteField(index = 9, bytes = 1, description = "女宠是否在闺房")
    private int isBoudoir;

    @ByteField(index = 10, bytes = 1, description = "女宠是否出战")
    private int isBattle;

    @ByteField(index = 11, bytes = 1, description = "女宠缠绵价格是否官方价格")
    private int isOfficialPrice;

    @ByteField(index = 12, bytes = 2, description = "玩家昵称长度")
    private int skyNicknameLength;

    @ByteField(index = 13, length = 12, description = "玩家昵称")
    private String skyNickname;

    @ByteField(index = 14, bytes = 1, description = "女宠已学技能数组长度")
    private byte playerPetSkillNum;

    @ByteField(index = 15, length = 14, description = "女宠已学技能数组")
    private PlayerPetSkill[] playerPetSkill;

    @ByteField(index = 16, bytes = 1, description = "女宠时装数组长度")
    private byte playerPetClothesNum;
    @ByteField(index = 17, length = 16, description = "女宠时装数组")
    private PlayerPetClothes[] playerPetClothes;

    @ByteField(index = 18, bytes = 1, description = "女宠物品掉落价格数组长度")
    private byte playerPetDropPriceNum;
    @ByteField(index = 19, length = 18, description = "女宠物品掉落价格数组长度")
    private PlayerPetDropPrice[] playerPetDropPrice;

    @ByteField(index = 20, bytes = 4, description = "女宠该等级所需经验")
    private long petExpLevel;

    @ByteField(index = 21, bytes = 2, description = "女宠图像长度")
    private int petImgFileNameLength;

    @ByteField(index = 22, length = 21, description = "女宠图像")
    private String petImgFileName;

    public PlayerPet() {
        super();
        // TODO Auto-generated constructor stub
    }

    public PlayerPet(long playerPetId, long skyId, long petTypeId, int petNickNameLength,
            String petNickName,
            int petLevel, long petExp, int staminaValueRemain, int staminaValueBase, int isBoudoir,
            int isBattle,
            int isOfficialPrice, int skyNicknameLength, String skyNickname, byte playerPetSkillNum,
            PlayerPetSkill[] playerPetSkill) {
        super();
        this.playerPetId = playerPetId;
        this.skyId = skyId;
        this.petTypeId = petTypeId;
        this.petNickNameLength = petNickNameLength;
        this.petNickName = petNickName;
        this.petLevel = petLevel;
        this.petExp = petExp;
        this.staminaValueRemain = staminaValueRemain;
        this.staminaValueBase = staminaValueBase;
        this.isBoudoir = isBoudoir;
        this.isBattle = isBattle;
        this.isOfficialPrice = isOfficialPrice;
        this.skyNicknameLength = skyNicknameLength;
        this.skyNickname = skyNickname;
        this.playerPetSkillNum = playerPetSkillNum;
        this.playerPetSkill = playerPetSkill;
    }

    public long getPlayerPetId() {
        return playerPetId;
    }

    public void setPlayerPetId(long playerPetId) {
        this.playerPetId = playerPetId;
    }

    public long getSkyId() {
        return skyId;
    }

    public void setSkyId(long skyId) {
        this.skyId = skyId;
    }

    public long getPetTypeId() {
        return petTypeId;
    }

    public void setPetTypeId(long petTypeId) {
        this.petTypeId = petTypeId;
    }

    public String getPetNickName() {
        return petNickName;
    }

    public void setPetNickName(String petNickName) {
        this.petNickNameLength = (StringUtils.isBlank(petNickName) ? 0 : petNickName.length() * 2);
        this.petNickName = petNickName;
    }

    public int getPetLevel() {
        return petLevel;
    }

    public void setPetLevel(int petLevel) {
        this.petLevel = petLevel;
    }

    public long getPetExp() {
        return petExp;
    }

    public void setPetExp(long petExp) {
        this.petExp = petExp;
    }

    public int getStaminaValueRemain() {
        return staminaValueRemain;
    }

    public void setStaminaValueRemain(int staminaValueRemain) {
        this.staminaValueRemain = staminaValueRemain;
    }

    public int getStaminaValueBase() {
        return staminaValueBase;
    }

    public void setStaminaValueBase(int staminaValueBase) {
        this.staminaValueBase = staminaValueBase;
    }

    public int getIsBoudoir() {
        return isBoudoir;
    }

    public void setIsBoudoir(int isBoudoir) {
        this.isBoudoir = isBoudoir;
    }

    public int getIsBattle() {
        return isBattle;
    }

    public void setIsBattle(int isBattle) {
        this.isBattle = isBattle;
    }

    public int getIsOfficialPrice() {
        return isOfficialPrice;
    }

    public void setIsOfficialPrice(int isOfficialPrice) {
        this.isOfficialPrice = isOfficialPrice;
    }

    public String getSkyNickname() {
        return skyNickname;
    }

    public int getPetNickNameLength() {
        return petNickNameLength;
    }

    public void setPetNickNameLength(int petNickNameLength) {
        this.petNickNameLength = petNickNameLength;
    }

    public int getSkyNicknameLength() {
        return skyNicknameLength;
    }

    public void setSkyNicknameLength(int skyNicknameLength) {
        this.skyNicknameLength = skyNicknameLength;
    }

    public void setSkyNickname(String skyNickname) {
        this.skyNicknameLength = (StringUtils.isBlank(skyNickname) ? 0 : skyNickname.length() * 2);
        this.skyNickname = skyNickname;
    }

    public byte getPlayerPetSkillNum() {
        return playerPetSkillNum;
    }

    public void setPlayerPetSkillNum(byte playerPetSkillNum) {
        this.playerPetSkillNum = playerPetSkillNum;
    }

    public PlayerPetSkill[] getPlayerPetSkill() {
        return playerPetSkill;
    }

    public void setPlayerPetSkill(PlayerPetSkill[] playerPetSkill) {
        this.playerPetSkillNum = (byte) (playerPetSkill == null ? 0 : playerPetSkill.length);
        this.playerPetSkill = playerPetSkill;
    }

    public byte getPlayerPetClothesNum() {
        return playerPetClothesNum;
    }

    public void setPlayerPetClothesNum(byte playerPetClothesNum) {
        this.playerPetClothesNum = playerPetClothesNum;
    }

    public PlayerPetClothes[] getPlayerPetClothes() {
        return playerPetClothes;
    }

    public void setPlayerPetClothes(PlayerPetClothes[] playerPetClothes) {
        this.playerPetClothesNum = (byte) (playerPetClothes == null ? 0 : playerPetClothes.length);
        this.playerPetClothes = playerPetClothes;
    }

    public byte getPlayerPetDropPriceNum() {
        return playerPetDropPriceNum;
    }

    public void setPlayerPetDropPriceNum(byte playerPetDropPriceNum) {
        this.playerPetDropPriceNum = playerPetDropPriceNum;
    }

    public PlayerPetDropPrice[] getPlayerPetDropPrice() {
        return playerPetDropPrice;
    }

    public void setPlayerPetDropPrice(PlayerPetDropPrice[] playerPetDropPrice) {
        this.playerPetDropPriceNum = (byte) (playerPetDropPrice == null ? 0
                : playerPetDropPrice.length);
        this.playerPetDropPrice = playerPetDropPrice;
    }

    /**
     * @return the petImgFileNameLength
     */
    public int getPetImgFileNameLength() {
        return petImgFileNameLength;
    }

    /**
     * @param petImgFileNameLength the petImgFileNameLength to set
     */
    public void setPetImgFileNameLength(int petImgFileNameLength) {
        this.petImgFileNameLength = petImgFileNameLength;
    }

    /**
     * @return the petImgFileName
     */
    public String getPetImgFileName() {
        return petImgFileName;
    }

    /**
     * @param petImgFileName the petImgFileName to set
     */
    public void setPetImgFileName(String petImgFileName) {
        this.petImgFileName = petImgFileName;
        this.petImgFileNameLength = (StringUtils.isBlank(petImgFileName) ? 0
                : petImgFileName.length() * 2);
    }

    /**
     * @return the petExpLevel
     */
    public long getPetExpLevel() {
        return petExpLevel;
    }

    /**
     * @param petExpLevel the petExpLevel to set
     */
    public void setPetExpLevel(long petExpLevel) {
        this.petExpLevel = petExpLevel;
    }

}
