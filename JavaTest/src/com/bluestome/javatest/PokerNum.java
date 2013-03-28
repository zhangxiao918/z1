/**
 * 
 */

package com.bluestome.javatest;

/**
 * 牌数字
 * 
 * @author Archibald.Wang
 */
public enum PokerNum {

    NUM_3(1, "3"),
    NUM_4(2, "4"),
    NUM_5(3, "5"),
    NUM_6(4, "6"),
    NUM_7(5, "7"),
    NUM_8(6, "8"),
    NUM_9(7, "9"),
    NUM_10(8, "10"),
    NUM_J(9, "J"),
    NUM_Q(10, "Q"),
    NUM_K(11, "K"),
    NUM_A(12, "A"),
    NUM_2(13, "2"),
    NUM_KINGLET(14, "KINGLET"),
    NUM_KING(15, "KING"), ;

    public static PokerNum getPokerNum(int value) {
        PokerNum ret = null;
        for (PokerNum num : PokerNum.values()) {
            if (num.getCode() == value) {
                ret = num;
                break;
            }
        }
        return ret;
    }

    private int code;
    private String desc;

    private PokerNum(int code, String desc) {
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
