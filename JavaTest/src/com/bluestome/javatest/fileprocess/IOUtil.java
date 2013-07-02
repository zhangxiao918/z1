
package com.bluestome.javatest.fileprocess;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

public class IOUtil {

    /**
     * 复制文件到指定目录
     * 
     * @param url 文件地址
     * @param fileName 文件名（绝对路径）
     */
    public static synchronized void createPicFile(String url, String fileName) throws IOException { // File
                                                                                                    // src,
                                                                                                    // File
                                                                                                    // dst,
                                                                                                    // int
                                                                                                    // BUFFER_SIZE
        InputStream in = null;
        OutputStream out = null;
        URL urls = null;
        File file = null;
        try {
            url = url.replace(" ", "%20");
            urls = new URL(url);
            in = urls.openStream();

            file = new File(fileName);

            if (file.exists()) {
                System.out.println("存在文件[" + file.getAbsolutePath() + "]");
                return;
            }

            if (!file.exists()) {
                file.getParentFile().mkdirs();
            }

            out = new BufferedOutputStream(new FileOutputStream(file),
                    1024);
            int bytesRead = 0;
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            while ((bytesRead = in.read()) != -1) {
                byteBuffer.write(bytesRead);
            }
            byteBuffer.flush();
            byteBuffer.writeTo(out);
            byteBuffer.close();
        } finally {
            if (null != in) {
                in.close();
            }
            if (null != out) {
                out.close();
            }
        }
    }

    /**
     * 创建文件
     * 
     * @param content 文件内容
     * @param fileName 文件名 [默认保存在用户目录下]
     */
    public static synchronized void createFile(String content, String fileName) {
        File file = null;
        OutputStream out = null;
        try {
            try {
                file = new File(System.getProperty("user.dir") + "/" + fileName + ".txt");
                out = new BufferedOutputStream(new FileOutputStream(file), 1024);
                out.write(content.getBytes(), 0, content.getBytes().length);
                out.close();
            } finally {
                if (out != null)
                    out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 创建文件
     * 
     * @param content 文件内容
     * @param fileName 文件名
     * @param ext 文件扩展名
     * @return
     */
    public static synchronized String createFile(String content, String fileName, String ext) {
        String filePath = System.getProperty("user.dir") + "/" + fileName + ext;
        File file = null;
        OutputStream out = null;
        try {
            try {
                file = new File(filePath);
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                }
                out = new BufferedOutputStream(new FileOutputStream(file), 1024);
                out.write(content.getBytes(), 0, content.getBytes().length);
                out.close();
            } finally {
                if (out != null)
                    out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }

    /**
     * 将二进制内容写入指定文件
     * 
     * @param bytes
     * @param fileName
     */
    public synchronized static void createFile(byte[] bytes, String fileName) throws IOException {
        File file = null;
        OutputStream out = null;
        try {
            try {
                file = new File(fileName);

                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                }

                out = new BufferedOutputStream(new FileOutputStream(file), 1024);
                out.write(bytes, 0, bytes.length);
                out.close();
            } finally {
                if (out != null)
                    out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException();
        }

    }

    /**
     * 获取文件内容
     * 
     * @param filePath 文件路径
     * @param encoding 编码格式
     * @param ext 文件扩展名
     */
    public static String readFile(String filePath, String encoding, String ext) {
        File file = null;
        InputStreamReader read = null;
        BufferedReader is = null;
        StringBuffer sb = new StringBuffer();
        try {
            file = new File(filePath);
            // 判断是否为文件夹
            if (file.isDirectory()) {
                return null;
            } else if (file.isFile() && file.getName().endsWith(ext)) { // 单个文件且文件名义为文本文件
                read = new InputStreamReader(new FileInputStream(file), encoding);
                is = new BufferedReader(read);
                String line;
                while ((line = is.readLine()) != null) {
                    sb.append(line);
                    sb.append("\r\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 读取文件内容
     * 
     * @param fileName 文件名
     * @return
     */
    public static String readFile(String fileName) {
        InputStreamReader read = null;
        BufferedReader is = null;
        StringBuffer sb = new StringBuffer();
        ClassLoader classLoader = IOUtil.class.getClassLoader();
        try {
            read = new InputStreamReader(classLoader.getResourceAsStream(fileName));
            is = new BufferedReader(read);
            String line;
            while ((line = is.readLine()) != null) {
                sb.append(line);
                sb.append("\r\n");
            }
        } catch (IOException e) {
            System.err.println(" >> 找不到文件");
        } catch (Exception e) {
            System.err.println(" >> 异常");
            e.printStackTrace();
        } finally {
            try {
                if (null != is) {
                    is.close();
                }
            } catch (Exception e) {

            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
    }

    /**
     * 将字节数组保存在指定文件中
     * 
     * @param pathname 需要保存的文件路径
     * @param buffer 需要被保存的内容
     * @param append 是否采用追加的方式
     * @return
     */
    public static int saveFile(String pathname, byte[] buffer, boolean append) {
        String path = null;
        if (null == pathname)
            return -2;
        pathname = pathname.replaceAll("\\\\", "/");
        path = pathname.substring(0, pathname.lastIndexOf("/"));
        java.io.File dir = new java.io.File(path);
        if (!dir.isDirectory())
            dir.mkdirs();// 创建不存在的目录
        try {
            OutputStream bos = new FileOutputStream(pathname, append);
            bos.write(buffer);
            bos.close();
        } catch (FileNotFoundException fnfe) {
            return -1;
        } catch (IOException ioe) {
            return -2;
        }
        return 0;
    }

}
