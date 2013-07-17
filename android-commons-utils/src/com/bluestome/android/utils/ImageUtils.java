/**
 * 
 */

package com.bluestome.android.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.ref.SoftReference;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * 图片工具类
 * 
 * @author bluestome.zhang
 */
public class ImageUtils {

    private final static String TAG = ImageUtils.class.getCanonicalName();
    private static HashMap<String, SoftReference<byte[]>> byteArrayCache = new HashMap<String, SoftReference<byte[]>>();
    // 图片主目录
    public static final String DDZ_PATH = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/bluestome/";

    // 当前应用图片保存目录
    public static final String IMAGE_PATH = DDZ_PATH + "image/";

    /**
     * 从本地读取图片
     * 
     * @param imgName 图片名称
     * @return byte[] 图片流
     */
    public synchronized static byte[] loadImageFromLocal(String imgName) {
        byte[] body = null;
        String fPath = IMAGE_PATH + imgName;
        File file = null;
        try {
            SoftReference<byte[]> cache = byteArrayCache.get(fPath);
            if (null != cache) {
                body = cache.get();
                if (null != body) {
                    return body;
                }
            }
            file = new File(fPath);
            // TODO 对文件名中有二级目录进行本地路径判断处理
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (file.exists()) {
                // TODO 读取图片并转成Bitmap
                FileInputStream fis = new FileInputStream(file);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[2048];
                int c;
                while ((c = fis.read(buffer)) != -1) {
                    baos.write(buffer, 0, c);
                    baos.flush();
                }
                body = baos.toByteArray();
                baos.close();
                fis.close();
            } else {
                Log.d(TAG, "文件不存在本地[" + fPath + "]");
            }
        } catch (Exception e) {
            Log.e(TAG, "loadImageFromLocal.exception:" + e.getMessage());
        }
        return body;
    }

    /**
     * 从服务端下载图片
     * 
     * @param addr 图片保存的服务器地址
     * @param imgName 图片名称
     * @return
     */
    public synchronized static byte[] loadImageFromServer(String addr, String imgName) {
        final String url = addr + imgName;
        byte[] body = null;
        try {
            body = HttpClientUtils.getResponseBodyAsByte(url);
            if (null != body) {
                // 将图片保存到本地
                String fPath = IMAGE_PATH + imgName;
                saveImage2Local(fPath, body);
            }
        } catch (Exception e) {
            Log.e(TAG, "loadImageFromServer.exception:" + e.getMessage());
        }
        return body;
    }

    /**
     * 从服务端下载图片
     * 
     * @param addr 图片保存的服务器地址
     * @param imgName 图片名称
     * @return
     */
    public synchronized static byte[] loadImageFromServer(String url) {
        byte[] body = null;
        try {
            body = HttpClientUtils.getResponseBodyAsByte(url);
            if (null != body) {
                Log.d(TAG, "从服务端下载的文件大小为:" + body.length);
                // 将图片保存到本地
                String fPath = IMAGE_PATH + File.separator
                        + URLEncoder.encode(url);
                saveImage2Local(fPath, body);
            }
        } catch (Exception e) {
            Log.e(TAG, "loadImageFromServer.exception:" + e.getMessage());
        }
        return body;
    }

    /**
     * 将图片流保存到本地文件中
     * 
     * @param fPath 图片在本地的地址
     * @param body 图片流
     * @return
     */
    public synchronized static boolean saveImage2Local(String fPath, byte[] body) {
        File file = null;
        boolean ok = false;
        try {
            Log.d(TAG, "文件保存路径:" + fPath);
            if (!byteArrayCache.containsKey(fPath)) {
                byteArrayCache.put(fPath, new SoftReference<byte[]>(body));
            }
            file = new File(fPath);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(body);
            fos.flush();
            fos.close();
            ok = true;
        } catch (Exception e) {
            Log.e(TAG, "saveImage2Local.exception:" + e.getMessage());
        }
        return ok;
    }

    void release() {
        byteArrayCache.clear();
    }

    public static Bitmap decodeFile(byte[] body) {
        // decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(body, 0, body.length);

        // Find the correct scale value. It should be the power of 2.
        final int REQUIRED_SIZE = 100;
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE
                    || height_tmp / 2 < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeByteArray(body, 0, body.length);
    }
}
