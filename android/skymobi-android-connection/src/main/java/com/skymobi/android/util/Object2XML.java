package com.skymobi.android.util;


import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * 根据对象生成XML文档.
 * 使用Java提供的java.beans.XMLEncoder和java.beans.XMLDecoder类�?
 * 这是JDK 1.4以后才出现的�?
 */
public class Object2XML {
    /**
     * 对象输出到XML文件
     * @param obj   待输出的对象
     * @param outFileName   目标XML文件的文件名
     * @return  返回输出XML文件的路�?
     * @throws FileNotFoundException
     */
    public static String object2XML(Object obj, String outFileName)
            throws FileNotFoundException {
        // 构�?输出XML文件的字节输出流
        File outFile = new File(outFileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outFile));

        // 构�?�?��XML编码�?
        XMLEncoder xmlEncoder = new XMLEncoder(bos);
        // 使用XML编码器写对象
        xmlEncoder.writeObject(obj);
        // 关闭编码�?
        xmlEncoder.close();
        
        return outFile.getAbsolutePath();
    }
    /**
     * 把XML文件解码成对�?
     * @param inFileName    输入的XML文件
     * @return  返回生成的对�?
     * @throws FileNotFoundException
     */
    public static Object xml2Object(String inFileName)
            throws FileNotFoundException {
        // 构�?输入的XML文件的字节输入流
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inFileName));
        // 构�?�?��XML解码�?
        XMLDecoder xmlDecoder = new XMLDecoder(bis);
        // 使用XML解码器读对象
        Object obj = xmlDecoder.readObject();
        // 关闭解码�?
        xmlDecoder.close();
        
        return obj;
    }
}
