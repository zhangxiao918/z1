/**
 * 
 */

package com.bluestome.javatest;

/**
 * 牌花色
 * 
 * @author Archibald.Wang
 */
public enum Suit {

    RED_DIAMOND(1, "方块"),
    BLACK_DIAMOND(2, "梅花"),
    RED_HEART(3, "红桃"),
    BLACK_HEART(4, "黑桃"),
    KINGLET(5, "小王"),
    KING(6, "大王");

    public static Suit getSuitType(int value) {
        Suit ret = null;
        for (Suit suit : Suit.values()) {
            if (suit.getCode() == value) {
                ret = suit;
                break;
            }
        }
        return ret;
    }

    private int code;
    private String desc;

    private Suit(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
