
package com.bluestome.javatest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 一副牌
 * 
 * @author Archibald.Wang
 */
public class Poker {

    public static final Poker BLACK_HEART_A = new Poker((byte) 0, "黑桃A", Suit.BLACK_HEART,
            PokerNum.NUM_A);
    public static final Poker BLACK_HEART_2 = new Poker((byte) 1, "黑桃2", Suit.BLACK_HEART,
            PokerNum.NUM_2);
    public static final Poker BLACK_HEART_3 = new Poker((byte) 2, "黑桃3", Suit.BLACK_HEART,
            PokerNum.NUM_3);
    public static final Poker BLACK_HEART_4 = new Poker((byte) 3, "黑桃4", Suit.BLACK_HEART,
            PokerNum.NUM_4);
    public static final Poker BLACK_HEART_5 = new Poker((byte) 4, "黑桃5", Suit.BLACK_HEART,
            PokerNum.NUM_5);
    public static final Poker BLACK_HEART_6 = new Poker((byte) 5, "黑桃6", Suit.BLACK_HEART,
            PokerNum.NUM_6);
    public static final Poker BLACK_HEART_7 = new Poker((byte) 6, "黑桃7", Suit.BLACK_HEART,
            PokerNum.NUM_7);
    public static final Poker BLACK_HEART_8 = new Poker((byte) 7, "黑桃8", Suit.BLACK_HEART,
            PokerNum.NUM_8);
    public static final Poker BLACK_HEART_9 = new Poker((byte) 8, "黑桃9", Suit.BLACK_HEART,
            PokerNum.NUM_9);
    public static final Poker BLACK_HEART_10 = new Poker((byte) 9, "黑桃10", Suit.BLACK_HEART,
            PokerNum.NUM_10);
    public static final Poker BLACK_HEART_J = new Poker((byte) 10, "黑桃J", Suit.BLACK_HEART,
            PokerNum.NUM_J);
    public static final Poker BLACK_HEART_Q = new Poker((byte) 11, "黑桃Q", Suit.BLACK_HEART,
            PokerNum.NUM_Q);
    public static final Poker BLACK_HEART_K = new Poker((byte) 12, "黑桃K", Suit.BLACK_HEART,
            PokerNum.NUM_K);
    public static final Poker RED_HEART_A = new Poker((byte) 13, "红桃A", Suit.RED_HEART,
            PokerNum.NUM_A);
    public static final Poker RED_HEART_2 = new Poker((byte) 14, "红桃2", Suit.RED_HEART,
            PokerNum.NUM_2);
    public static final Poker RED_HEART_3 = new Poker((byte) 15, "红桃3", Suit.RED_HEART,
            PokerNum.NUM_3);
    public static final Poker RED_HEART_4 = new Poker((byte) 16, "红桃4", Suit.RED_HEART,
            PokerNum.NUM_4);
    public static final Poker RED_HEART_5 = new Poker((byte) 17, "红桃5", Suit.RED_HEART,
            PokerNum.NUM_5);
    public static final Poker RED_HEART_6 = new Poker((byte) 18, "红桃6", Suit.RED_HEART,
            PokerNum.NUM_6);
    public static final Poker RED_HEART_7 = new Poker((byte) 19, "红桃7", Suit.RED_HEART,
            PokerNum.NUM_7);
    public static final Poker RED_HEART_8 = new Poker((byte) 20, "红桃8", Suit.RED_HEART,
            PokerNum.NUM_8);
    public static final Poker RED_HEART_9 = new Poker((byte) 21, "红桃9", Suit.RED_HEART,
            PokerNum.NUM_9);
    public static final Poker RED_HEART_10 = new Poker((byte) 22, "红桃10", Suit.RED_HEART,
            PokerNum.NUM_10);
    public static final Poker RED_HEART_J = new Poker((byte) 23, "红桃J", Suit.RED_HEART,
            PokerNum.NUM_J);
    public static final Poker RED_HEART_Q = new Poker((byte) 24, "红桃Q", Suit.RED_HEART,
            PokerNum.NUM_Q);
    public static final Poker RED_HEART_K = new Poker((byte) 25, "红桃K", Suit.RED_HEART,
            PokerNum.NUM_K);
    public static final Poker BLACK_DIAMOND_A = new Poker((byte) 26, "梅花A", Suit.BLACK_DIAMOND,
            PokerNum.NUM_A);
    public static final Poker BLACK_DIAMOND_2 = new Poker((byte) 27, "梅花2", Suit.BLACK_DIAMOND,
            PokerNum.NUM_2);
    public static final Poker BLACK_DIAMOND_3 = new Poker((byte) 28, "梅花3", Suit.BLACK_DIAMOND,
            PokerNum.NUM_3);
    public static final Poker BLACK_DIAMOND_4 = new Poker((byte) 29, "梅花4", Suit.BLACK_DIAMOND,
            PokerNum.NUM_4);
    public static final Poker BLACK_DIAMOND_5 = new Poker((byte) 30, "梅花5", Suit.BLACK_DIAMOND,
            PokerNum.NUM_5);
    public static final Poker BLACK_DIAMOND_6 = new Poker((byte) 31, "梅花6", Suit.BLACK_DIAMOND,
            PokerNum.NUM_6);
    public static final Poker BLACK_DIAMOND_7 = new Poker((byte) 32, "梅花7", Suit.BLACK_DIAMOND,
            PokerNum.NUM_7);
    public static final Poker BLACK_DIAMOND_8 = new Poker((byte) 33, "梅花8", Suit.BLACK_DIAMOND,
            PokerNum.NUM_8);
    public static final Poker BLACK_DIAMOND_9 = new Poker((byte) 34, "梅花9", Suit.BLACK_DIAMOND,
            PokerNum.NUM_9);
    public static final Poker BLACK_DIAMOND_10 = new Poker((byte) 35, "梅花10", Suit.BLACK_DIAMOND,
            PokerNum.NUM_10);
    public static final Poker BLACK_DIAMOND_J = new Poker((byte) 36, "梅花J", Suit.BLACK_DIAMOND,
            PokerNum.NUM_J);
    public static final Poker BLACK_DIAMOND_Q = new Poker((byte) 37, "梅花Q", Suit.BLACK_DIAMOND,
            PokerNum.NUM_Q);
    public static final Poker BLACK_DIAMOND_K = new Poker((byte) 38, "梅花K", Suit.BLACK_DIAMOND,
            PokerNum.NUM_K);
    public static final Poker RED_DIAMOND_A = new Poker((byte) 39, "方块A", Suit.RED_DIAMOND,
            PokerNum.NUM_A);
    public static final Poker RED_DIAMOND_2 = new Poker((byte) 40, "方块2", Suit.RED_DIAMOND,
            PokerNum.NUM_2);
    public static final Poker RED_DIAMOND_3 = new Poker((byte) 41, "方块3", Suit.RED_DIAMOND,
            PokerNum.NUM_3);
    public static final Poker RED_DIAMOND_4 = new Poker((byte) 42, "方块4", Suit.RED_DIAMOND,
            PokerNum.NUM_4);
    public static final Poker RED_DIAMOND_5 = new Poker((byte) 43, "方块5", Suit.RED_DIAMOND,
            PokerNum.NUM_5);
    public static final Poker RED_DIAMOND_6 = new Poker((byte) 44, "方块6", Suit.RED_DIAMOND,
            PokerNum.NUM_6);
    public static final Poker RED_DIAMOND_7 = new Poker((byte) 45, "方块7", Suit.RED_DIAMOND,
            PokerNum.NUM_7);
    public static final Poker RED_DIAMOND_8 = new Poker((byte) 46, "方块8", Suit.RED_DIAMOND,
            PokerNum.NUM_8);
    public static final Poker RED_DIAMOND_9 = new Poker((byte) 47, "方块9", Suit.RED_DIAMOND,
            PokerNum.NUM_9);
    public static final Poker RED_DIAMOND_10 = new Poker((byte) 48, "方块10", Suit.RED_DIAMOND,
            PokerNum.NUM_10);
    public static final Poker RED_DIAMOND_J = new Poker((byte) 49, "方块J", Suit.RED_DIAMOND,
            PokerNum.NUM_J);
    public static final Poker RED_DIAMOND_Q = new Poker((byte) 50, "方块Q", Suit.RED_DIAMOND,
            PokerNum.NUM_Q);
    public static final Poker RED_DIAMOND_K = new Poker((byte) 51, "方块K", Suit.RED_DIAMOND,
            PokerNum.NUM_K);
    public static final Poker KINGLET = new Poker((byte) 52, "小王", Suit.KINGLET,
            PokerNum.NUM_KINGLET);
    public static final Poker KING = new Poker((byte) 53, "大王", Suit.KING, PokerNum.NUM_KING);

    public static final Poker[] deck = new Poker[] {
            BLACK_HEART_A, BLACK_HEART_2, BLACK_HEART_3, BLACK_HEART_4, BLACK_HEART_5,
            BLACK_HEART_6,
            BLACK_HEART_7, BLACK_HEART_8, BLACK_HEART_9, BLACK_HEART_10, BLACK_HEART_J,
            BLACK_HEART_Q, BLACK_HEART_K, RED_HEART_A, RED_HEART_2,
            RED_HEART_3, RED_HEART_4, RED_HEART_5, RED_HEART_6, RED_HEART_7, RED_HEART_8,
            RED_HEART_9, RED_HEART_10, RED_HEART_J, RED_HEART_Q,
            RED_HEART_K, BLACK_DIAMOND_A, BLACK_DIAMOND_2, BLACK_DIAMOND_3, BLACK_DIAMOND_4,
            BLACK_DIAMOND_5, BLACK_DIAMOND_6, BLACK_DIAMOND_7,
            BLACK_DIAMOND_8, BLACK_DIAMOND_9, BLACK_DIAMOND_10, BLACK_DIAMOND_J, BLACK_DIAMOND_Q,
            BLACK_DIAMOND_K, RED_DIAMOND_A, RED_DIAMOND_2,
            RED_DIAMOND_3, RED_DIAMOND_4, RED_DIAMOND_5, RED_DIAMOND_6, RED_DIAMOND_7,
            RED_DIAMOND_8, RED_DIAMOND_9, RED_DIAMOND_10, RED_DIAMOND_J,
            RED_DIAMOND_Q, RED_DIAMOND_K, KINGLET, KING
    };

    private static final Map<Byte, Poker> deckMap = new HashMap<Byte, Poker>();

    static {
        for (Poker p : deck) {
            deckMap.put(p.getCode(), p);
        }
    }

    public static final Poker[] values() {
        return deck;
    }

    public static Poker getPoker(byte value) {
        return deckMap.get(value);
    }

    public static Poker[] getPokers(byte[] values) {
        List<Poker> ret = new ArrayList<Poker>();
        for (byte value : values) {
            ret.add(deckMap.get(value));
        }
        return ret.toArray(new Poker[] {});
    }

    public static byte[] getValues(Poker[] pokers) {

        byte[] ret = new byte[pokers.length];

        for (int i = 0; i < pokers.length; i++) {
            ret[i] = pokers[i].getCode();
        }

        return ret;
    }

    public static Poker[] getPoker(PokerNum num) {
        List<Poker> ret = new ArrayList<Poker>();
        for (Poker p : Poker.values()) {
            if (p.getNum() == num) {
                ret.add(p);
            }
        }
        return ret.toArray(new Poker[] {});
    }

    private byte code;
    private String desc;
    private Suit suit;
    private PokerNum num;

    public Poker(byte code, String desc, Suit suit, PokerNum num) {
        this.code = code;
        this.desc = desc;
        this.suit = suit;
        this.num = num;
    }

    public byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public Suit getSuit() {
        return suit;
    }

    public PokerNum getNum() {
        return num;
    }

    public static void main(String[] args) {
        byte[] tests = new byte[] {
                53, 53, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 14, 15, 16, 17, 18, 19, 20, 21,
                22, 23, 24, 25
        };
        for (byte b : tests) {
            System.out.println(Poker.getPoker(b).getDesc());
        }

    }

}
