/*******************************************************************************
 * CopyRight (c) 2005-2010 SKY-MOBI Ltd. All rights reserved.
 * Filename: ResultCode.java
 * Creator: yangtong_fang
 * Version: 1.0
 * Date: 2010-8-13 上午09:18:38
 * Description:
 *******************************************************************************/
package com.skymobi.cac.maopao.xip.bto.basis;


import java.util.HashMap;
import java.util.Map;

public enum RoomStyleTypeEnum {

    /** 1, "训练场" */
    ROOM_STYLE_TYPE_TRAINING(1, false, "训练场"),

    /** 2, "金豆场" */
    ROOM_STYLE_TYPE_GOLD(2, false, "金豆场"),

    /** 3, "常规赛竞技场" */
    ROOM_STYLE_TYPE_REGULAR_MATCH(3, true, "常规赛竞技场"),

    /** 4, "大乱斗竞技场" */
    ROOM_STYLE_TYPE_MELEE_MATCH(4, true, "大乱斗竞技场"),

    /** 5, "私人房间场" */
    ROOM_STYLE_TYPE_PRIVATE_ROOM(5, false, "私人房间场"),

    /** 6, "闯关赛竞技场" */
    ROOM_STYLE_TYPE_LEVELBREAK_ROOM(6, true, "闯关赛竞技场");

    private long value;
    private boolean isMatch;
    private String message;

    private static Map<Long, RoomStyleTypeEnum> pool = new HashMap<Long, RoomStyleTypeEnum>();
    static {
        for (RoomStyleTypeEnum each : RoomStyleTypeEnum.values()) {
            pool.put(each.getValue(), each);
        }
    }

    private RoomStyleTypeEnum(long value, boolean isMatch, String message) {
        this.value = value;
        this.isMatch = isMatch;
        this.message = message;
    }

    /**
     * 根据传入的房间类型查看是否比赛房间
     * 
     * @param value
     * @return
     */
    public static boolean isMatch(long value) {
        RoomStyleTypeEnum roomStyleType = pool.get(value);
        if (null == roomStyleType) {
            return false;
        }
        return roomStyleType.isMatch;
    }

    public long getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public boolean isMatch() {
        return isMatch;
    }

}
