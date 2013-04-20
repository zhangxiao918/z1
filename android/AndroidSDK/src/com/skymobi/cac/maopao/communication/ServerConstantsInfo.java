
package com.skymobi.cac.maopao.communication;

import android.util.Log;

import com.hifreshday.android.commons.phonecontext.PhoneContext;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerConstantsInfo {

    private final static String TAG = "ServerConstantsInfo";

    public static final int CONNECTTIMEOUT = 5000;
    public static final int SOTIMEOUT = 10000;

    public static final String FactoryCode = "sky";// 暂时写死
    public static final String TerminalCode = "Android";// 暂时写死

    // 应用协议参数
    public static final int NODATA_RECEIVE_TIMEOUT = 34000; // 多超时间没有收到响应算作超时断线

    // 破晓开发服务器环境信息
    public static String SERVER_ACCESS_IP = "115.238.91.226";
    public static int SERVER_ACCESS_PORT = 10014;

    // 破晓测试服务器环境信息
    // public static String SERVER_ACCESS_IP = "115.238.91.240";
    // public static int SERVER_ACCESS_PORT = 10266;

    private final static String TESTENV_FILENAME = "poxiaotestenv.px";
    private final static String TESTENV_ACCESSIP = "accessip";
    private final static String TESTENV_ACCESSPORT = "accessport";

    private static ServerConstantsInfo instance;

    private ServerConstantsInfo() {
    }

    public static ServerConstantsInfo getInstance() {
        if (instance == null) {
            instance = new ServerConstantsInfo();
        }
        return instance;
    }

    public void initEnv() {
        File file = new File(PhoneContext.getInstance().getAppFolder() + File.separator
                + TESTENV_FILENAME);
        if (!file.exists()) {
            return;
        }

        FileInputStream input = null;
        try {
            input = new FileInputStream(file);
            Properties pro = new Properties();
            pro.load(input);
            SERVER_ACCESS_IP = pro.getProperty(TESTENV_ACCESSIP);
            SERVER_ACCESS_PORT = Integer.valueOf(pro.getProperty(TESTENV_ACCESSPORT));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }
    }

    // 客户端连接服务器成功之后，需要发送7 个字节的标示来和接入服务器进行握手。
    public static final byte AUTH[] = {
            (byte) 0x8a, (byte) 0xed, (byte) 0x9c,
            (byte) 0xf3, (byte) 0x7e, (byte) 0x32, (byte) 0xb9
    };

    //
    public static final byte BYTES[] = {
            (byte) 0x24, (byte) 0x2a, (byte) 0x47,
            (byte) 0x46, (byte) 0x58, (byte) 0x3a, (byte) 0x4c,
            (byte) 0x4b, (byte) 0x51, (byte) 0x43, (byte) 0x23,
            (byte) 0x24, (byte) 0x29, (byte) 0x4d, (byte) 0x54,
            (byte) 0x30, (byte) 0x38, (byte) 0x62, (byte) 0x73,
            (byte) 0x64, (byte) 0x7e, (byte) 0x29, (byte) 0x2f
    };

    public static ByteBuffer getUAByteBuffer(AtomicInteger msgSeq, AtomicInteger msgAck,
            int mRedirectTimes) {
        int length = 8 + 72 + ServerConstantsInfo.FactoryCode.length() // +
                                                                       // 4去掉了seq
                + ServerConstantsInfo.TerminalCode.length();

        ByteBuffer sendbuf = ByteBuffer.allocate(length + 7);
        sendbuf.order(ByteOrder.BIG_ENDIAN);
        sendbuf.put(ServerConstantsInfo.AUTH);// 握手字符串
        sendbuf.putChar(BasisMessageConstants.EXT_ID_BASE);
        sendbuf.putChar(BasisMessageConstants.SMOD_ID_ACCESS_TCP);
        sendbuf.putChar((char) length);
        sendbuf.putChar((char) (
                BasisMessageConstants.MSG_FLAG_ENCRYPT_TEMP_UA
                ));
        sendbuf.putChar(BasisMessageConstants.MSGCODE_ACCESS_TCP_UA_NEW_REQ);
        sendbuf.put((byte) mRedirectTimes);
        sendbuf.put((byte) FactoryCode.length());
        sendbuf.put(FactoryCode.getBytes());
        sendbuf.put((byte) ServerConstantsInfo.TerminalCode.length());
        sendbuf.put(ServerConstantsInfo.TerminalCode.getBytes());
        sendbuf.putInt(1);// Virtual Machine Version
        sendbuf.putInt(1);// HSV是移植版本号
        sendbuf.putChar((char) 800);// 终端屏幕宽度
        sendbuf.putChar((char) 480); // 终端屏幕高度
        sendbuf.put((byte) 16);// 终端字体宽度
        sendbuf.put((byte) 16);// 终端字体高度
        sendbuf.putChar((char) 256000);// 终端内存大小,单位是KB
        sendbuf.put((byte) 1);// 终端是否支持触摸屏

        sendbuf.put("354316034817173".getBytes());// TODO : 读取客户端IMEI值 长度为15字节
        sendbuf.put((byte) '\0');
        sendbuf.put("460031234567890".getBytes());// TODO : 读取客户端IMSI值 长度为15字节
        sendbuf.put((byte) '\0');
        sendbuf.putInt(1);// 入口
        sendbuf.putInt(400000);// 业务ID (APPID)
        sendbuf.put((byte) 21);// 手机平台
        sendbuf.put((byte) 1);// 屏幕类型
        sendbuf.put((byte) 0);// 是否支持压缩。
        sendbuf.put((byte) 2);// 预留
        sendbuf.putChar((char) 3);// 预留
        sendbuf.putInt(4);// 预留
        Log.d(TAG, "sendUA");
        return sendbuf;
    }

    public static ByteBuffer sendHeartbeatReq() {

        int length = 19;
        ByteBuffer sendbuf = ByteBuffer.allocate(length);
        sendbuf.order(ByteOrder.BIG_ENDIAN);
        sendbuf.putChar(BasisMessageConstants.EXT_ID_BASE);
        sendbuf.putChar(BasisMessageConstants.SMOD_ID_ACCESS_TCP);
        sendbuf.putChar((char) (length));
        sendbuf.putChar((char) BasisMessageConstants.MSG_FLAG_ENCRYPT_TEMP_UA);// flagBasisMessageConstants.MSG_FLAG_ENCRYPT_SEQ
        sendbuf.putChar(BasisMessageConstants.MSGCODE_MAIN_HEARTBEAT_REQ);

        // 心跳数据服务端不看，以下数据仅仅用来站位。
        sendbuf.put((byte) '4');
        sendbuf.putChar((char) 2);// 上次退出原因，0 = 未知原因，1 = 用户按挂机键，2 = 用户正常退出，3 =
                                  // 用户断线
        sendbuf.putChar((char) 1);// 尝试登陆次数
        sendbuf.putChar((char) 0);
        sendbuf.putChar((char) 0);

        return sendbuf;
    }
}
