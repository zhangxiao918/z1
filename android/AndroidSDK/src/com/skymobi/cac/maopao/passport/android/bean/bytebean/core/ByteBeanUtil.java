package com.skymobi.cac.maopao.passport.android.bean.bytebean.core;

public class ByteBeanUtil {
    public static int type2DefaultByteSize(Class<?> type) {
        int ret = -1;
        if (type.equals(byte.class)
                || type.equals(Byte.class)) {
            ret = 1;
        }
        else if (type.equals(short.class)
                || type.equals(Short.class)) {
            ret = 2;
        }
        else if (type.equals(int.class)
                || type.equals(Integer.class)) {
            ret = 4;
        }
        else if (type.equals(long.class)
                || type.equals(Long.class)) {
            ret = 8;
        }
        else {
            ret = -1;
        }
        
        return  ret;
    }
}
