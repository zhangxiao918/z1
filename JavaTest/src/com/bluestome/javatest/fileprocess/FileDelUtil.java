
package com.bluestome.javatest.fileprocess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class FileDelUtil {

    static List<String> tagFile = new ArrayList<String>();
    static AtomicInteger atCount = new AtomicInteger();
    static AtomicInteger lineCount = new AtomicInteger();
    static AtomicLong wordCount = new AtomicLong();
    static String FILE_SAVE_PATH = "D:\\word.txt";

    /**
     * @param args
     */
    public static void main(String[] args) {
        String filePath = "F:\\temp\\SVN\\";
        // 判断是否为目录，如果不为目录，则判断文件扩展名是否以参数中的一致，否则CONTINUE
        searchDirectory(filePath, "java");
        System.out.println("文件数量：" + atCount.get());
        System.out.println("行数：" + lineCount.get());
        System.out.println("文字数量:" + wordCount.get());
    }

    /**
     * @param filePath 文件路径
     * @param ext 文件扩展/目标文件名
     * @param isDirDel 是否删除目录
     */
    public synchronized static void searchDirectoryWithName(String filePath, String name,
            boolean isDirDelte) {
        File file = new File(filePath);
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            String fileName = files[i].getName();
            // 判断是否目录删除
            if (!isDirDelte) { // 单独文件删除
                if (files[i].isDirectory()) { // 如果是目录，则继续寻找符合条件的文件 (迭代)
                    searchDirectoryWithName(files[i].getAbsolutePath(), name, isDirDelte);
                } else if (files[i].isFile()) {
                    if (fileName.substring(0,
                            fileName.lastIndexOf(".")).toLowerCase().equals(fileName)) {
                        // files[i].delete();
                        if (files[i].exists()) {
                            // files[i].delete();
                        }
                        System.out.println("应该删除该文件:"
                                + files[i].getAbsolutePath());
                    } else {
                        continue;
                    }
                }
            } else {
                // 目录删除
                if (files[i].isDirectory()
                        && fileName.equals(name)) { // 目录删除
                                                    // 条件:文件是否为目录，且目录名为参数中的扩展名
                    System.out.println("删除目录");
                    deleteDirectory(files[i].getAbsolutePath()); // 执行目录删除

                } else {
                    // 如果当前文件为目录，则执行迭代继续查找符合条件的目录
                    if (files[i].isDirectory()) {
                        searchDirectoryWithName(files[i].getAbsolutePath(), name, isDirDelte);
                    }
                }

            }
        }
    }

    public synchronized static StringBuilder readFromFile(String path) {
        StringBuilder sb = new StringBuilder();
        try {
            File file = new File(path);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            while (null != (line = reader.readLine())) {
                sb.append(new String(line.getBytes(), "UTF-8")).append("\r");
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb;
    }

    public synchronized static StringBuffer readFromFile(File file) {
        StringBuffer sb = new StringBuffer();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            while (null != (line = reader.readLine())) {
                long cw = wordCount.get();
                // if (cw < 450000L) {
                long tmp = cw + line.getBytes().length / 2;

                lineCount.getAndIncrement();
                sb.append(new String(line.getBytes("GBK"), "GBK")).append("\r");
                wordCount.set(tmp);
                // } else {
                // System.out.println("文件数量：" + atCount.get());
                // System.out.println("行数：" + lineCount.get());
                // System.out.println("文字数量:" + wordCount.get());
                // System.exit(0);
                // }
            }
            // TODO 写入文件
            reader.close();
            saveFile(FILE_SAVE_PATH, sb.toString(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb;
    }

    /**
     * @param filePath 文件路径
     * @param ext 文件扩展/目标文件名
     * @param isDirDel 是否删除目录
     */
    public synchronized static void searchDirectory(String filePath, String ext) {
        File file = new File(filePath);
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            String fileName = files[i].getName();
            // 判断是否目录删除
            if (files[i].isDirectory()) { // 如果是目录，则继续寻找符合条件的文件 (迭代)
                searchDirectory(files[i].getAbsolutePath(), ext);
            } else if (files[i].isFile()) {
                if (fileName.substring(fileName.indexOf(".") + 1,
                        fileName.length()).toLowerCase().equals(ext)) {
                    atCount.getAndIncrement();
                    System.out.println("fileName:" + fileName);
                    StringBuffer sb = readFromFile(files[i]);
                    // if (null != sb && !sb.toString().equals("")) {
                    // System.out.println(sb.toString());
                    // }
                } else {
                    continue;
                }
            }
        }
    }

    /**
     * 删除单个文件
     * 
     * @param fileName 被删除文件的文件名
     * @return 单个文件删除成功返回true,否则返回false
     */
    public synchronized static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.isFile() && file.exists()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件" + fileName + "失败！");
            return false;
        }
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     * 
     * @param dir 被删除目录的文件路径
     * @return 目录删除成功返回true,否则返回false
     */
    public synchronized static boolean deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator)) {
            dir = dir + File.separator;
        }
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            System.out.println("删除目录失败" + dir + "目录不存在！");
            return false;
        }
        boolean flag = true;
        // 删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
            // 删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
        }

        if (!flag) {
            System.out.println("删除目录失败");
            return false;
        }

        // 删除当前目录
        if (dirFile.delete()) {
            System.out.println("删除目录" + dir + "成功！");
            return true;
        } else {
            System.out.println("删除目录" + dir + "失败！");
            return false;
        }
    }

    /**
     * 将字节数组保存在指定文件中
     * 
     * @param pathname 需要保存的文件路径
     * @param buffer 需要被保存的内容
     * @param append 是否采用追加的方式
     * @return
     */
    public static int saveFile(String pathname, String buffer, boolean append) {
        String path = null;
        if (null == pathname)
            return -2;
        pathname = pathname.replaceAll("\\\\", "/");
        path = pathname.substring(0, pathname.lastIndexOf("/"));
        java.io.File dir = new java.io.File(path);
        if (!dir.isDirectory())
            dir.mkdirs();// 创建不存在的目录
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(pathname, true)));
            out.write(buffer);
            out.flush();
            out.close();
        } catch (FileNotFoundException fnfe) {
            return -1;
        } catch (IOException ioe) {
            return -2;
        }
        return 0;
    }
}
