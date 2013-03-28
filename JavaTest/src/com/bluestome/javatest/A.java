
package com.bluestome.javatest;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: A
 * @Description: TODO
 * @author bluestome.zhang
 * @date 2013-3-19 上午10:15:08
 */
public class A {

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println((byte) '4');
        ByteBuffer sendbuf = ByteBuffer.allocate(19);
        // 对字节进行排序
        sendbuf.order(ByteOrder.BIG_ENDIAN);
        // 1个字节
        sendbuf.put((byte) 1);
        // 2个字节
        sendbuf.putChar((char) 2);
        // 8个字节
        sendbuf.putDouble((double) 3.2);
        // 4个字节
        sendbuf.putFloat((float) 3);
        // 4个字节
        sendbuf.putInt(3);

        byte[] bytes = sendbuf.array();
        System.out.println("包长度：" + bytes.length);
        System.out.println(ByteUtils.bytesAsHexString(bytes, Short.MAX_VALUE));
        System.out.println("牌数量:" + Poker.deck.length);
        String date = "2000-03-22";
        System.out.println(date + " 是否为一个日期格式：" + dataCheck(date));
    }

    private static boolean dataCheck(String date) {
        String eL = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
        Pattern p = Pattern.compile(eL);
        Matcher m = p.matcher(date);
        boolean flag = m.matches();
        return flag;
    }

}
