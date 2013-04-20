package com.skymobi.cac.maopao.xip.bto.basis;


public enum SkillEnum {

    SHOW_BACK_CARD(1, "占卜技能"),
    SHOW_PLAYER_CARD(2, "奇袭技能"),
    ANTI_SHOW_PLAYER_CARD(3, "闪避技能"),
    SWITCH_PLAYER_CARD(4, "神偷技能"),
    ANTI_SWITCH_PLAYER_CARD(5, "护主技能"),
    DOUBLE_MAGNIFICATION(6, "绝杀技能"),
    HALF_MAGNIFICATION(7, "回春技能");
    
    private int value;
    private String desc;

    public static SkillEnum getTypeByValue(int value) {
        switch (value) {
        case 1:
            return SHOW_BACK_CARD;
        case 2:
            return SHOW_PLAYER_CARD;
        case 3:
            return ANTI_SHOW_PLAYER_CARD;
        case 4:
            return SWITCH_PLAYER_CARD;
        case 5:
            return ANTI_SWITCH_PLAYER_CARD;
        case 6:
            return DOUBLE_MAGNIFICATION;
        case 7:
            return HALF_MAGNIFICATION;

        }
        return null;
    }

    private SkillEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
