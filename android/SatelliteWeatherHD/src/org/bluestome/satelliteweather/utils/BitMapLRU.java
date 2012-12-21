
package org.bluestome.satelliteweather.utils;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * Bitmap工具类,缓存用过的指定数量的图片,使用此工具类,不再需要手动管理Bitmap内存 原理:
 * 用一个队列保存使用Bitmap的顺序,每次使用Bitmap将对象移动到队列头 当内存不够,或者达到制定的缓存数量的时候,回收队列尾部图片
 * 保证当前使用最多的图片得到最长时间的缓存,提高速度
 * 
 * @author liaoxingliao
 */
public final class BitMapLRU {

    private static int CACHE_BYTE_SIZE = 10 * 1024 * 1024; // 缓存10M图片

    private static int CACHE_SIZE = 2000; // 缓存图片数量

    private static int byteSize = 0;

    private static final byte[] LOCKED = new byte[0];

    private static final LinkedList<String> CACHE_ENTRIES = // 此对象用来保持Bitmap的回收顺序,保证最后使用的图片被回收
    new LinkedList<String>() {
        private static final long serialVersionUID = 1L;

        @Override
        public void addFirst(String object) {
            while (remove(object))
                ;
            super.addFirst(object);
        }
    };
    private static final Stack<QueueEntry> TASK_QUEUE = new Stack<QueueEntry>(); // 线程请求创建图片的队列
    private static final Set<String> TASK_QUEUE_INDEX = new HashSet<String>(); // 保存队列中正在处理的图片的key,有效防止重复添加到请求创建队列
    private static final Map<String, Bitmap> IMG_CACHE_INDEX = new HashMap<String, Bitmap>(); // 缓存Bitmap
                                                                                              // 通过图片路径,图片大小

    static {
        // 初始化创建图片线程,并等待处理
        new Thread() {
            {
                setDaemon(true);
            }

            @Override
            public void run() {
                while (true) {
                    synchronized (TASK_QUEUE) {
                        if (TASK_QUEUE.isEmpty()) {
                            try {
                                TASK_QUEUE.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    QueueEntry entry = TASK_QUEUE.pop();
                    String key = createKey(entry.path, entry.width, entry.height);
                    TASK_QUEUE_INDEX.remove(key);
                    createBitmap(entry.path, entry.width, entry.height);
                }
            }
        }.start();
    }

    /**
     * 创建一张图片 如果缓存中已经存在,则返回缓存中的图,否则创建一个新的对象,并加入缓存
     * 宽度,高度,为了缩放原图减少内存的,如果输入的宽,高,比原图大,返回原图
     * 
     * @param path 图片物理路径 (必须是本地路径,不能是网络路径)
     * @param width 需要的宽度
     * @param height 需要的高度
     * @return
     */
    public static Bitmap createBitmap(String path, int width, int height) {
        Bitmap bitMap = null;
        try {
            while (CACHE_ENTRIES.size() >= CACHE_SIZE || byteSize >= CACHE_BYTE_SIZE) {
                destoryLast();
            }
            bitMap = useBitmap(path, width, height);
            if (bitMap != null && !bitMap.isRecycled()) {
                return bitMap;// 4294967296

            }
            bitMap = BitmapUtil.createBitmap(path, width, height);
            if (bitMap == null) { // 可能不是有效的图片..
                return null;
            }
            byteSize += (bitMap.getRowBytes() * bitMap.getHeight());

            String key = createKey(path, width, height);
            synchronized (LOCKED) {
                IMG_CACHE_INDEX.put(key, bitMap);
                CACHE_ENTRIES.addFirst(key);
            }
        } catch (OutOfMemoryError err) {
            System.out.println("OOM:" + byteSize);
            destoryLast();
            return createBitmap(path, width, height);
        }
        return bitMap;
    }

    /**
     * 设置缓存图片数量 如果输入负数,会产生异常
     * 
     * @param size
     */
    public static void setCacheSize(int size) {
        if (size <= 0) {
            throw new RuntimeException("size :" + size);
        }
        while (size < CACHE_ENTRIES.size()) {
            destoryLast();
        }
        CACHE_SIZE = size;
    }

    /**
     * 加入一个图片处理请求到图片创建队列
     * 
     * @param path 图片路径(本地)
     * @param width 图片宽度
     * @param height 图片高度
     */
    public static void addTask(String path, int width, int height) {
        QueueEntry entry = new QueueEntry();
        entry.path = path;
        entry.width = width;
        entry.height = height;
        synchronized (TASK_QUEUE) {
            while (TASK_QUEUE.size() > 20) {
                QueueEntry e = TASK_QUEUE.lastElement();
                TASK_QUEUE.remove(e);
                TASK_QUEUE_INDEX.remove(createKey(e.path, e.width, e.height));
            }
            String key = createKey(path, width, height);
            if (!TASK_QUEUE_INDEX.contains(key) && !IMG_CACHE_INDEX.containsKey(key)) {
                TASK_QUEUE.push(entry);
                TASK_QUEUE_INDEX.add(key);
                TASK_QUEUE.notify();
            }
        }
    }

    public static void cleanTask() {
        synchronized (TASK_QUEUE) {
            TASK_QUEUE_INDEX.clear();
            TASK_QUEUE.clear();
        }
    }

    // 将图片加入队列头
    private static Bitmap useBitmap(String path, int width, int height) {
        Bitmap bitMap = null;
        String key = createKey(path, width, height);
        synchronized (LOCKED) {
            bitMap = IMG_CACHE_INDEX.get(key);
            if (null != bitMap) {
                CACHE_ENTRIES.addFirst(key);
            }
        }
        return bitMap;
    }

    // 回收最后一张图片
    private static void destoryLast() {
        synchronized (LOCKED) {
            if (!CACHE_ENTRIES.isEmpty()) {
                String key = CACHE_ENTRIES.removeLast();
                if (key.length() > 0) {
                    Bitmap bitMap = IMG_CACHE_INDEX.remove(key);
                    if (bitMap != null) {
                        bitMap.recycle();
                        byteSize -= (bitMap.getRowBytes() * bitMap.getHeight());
                        bitMap = null;
                    }
                }
            }
        }
    }

    // 创建键
    private static String createKey(String path, int width, int height) {
        if (null == path || path.length() == 0) {
            return "";
        }
        return path + "_" + width + "_" + height;
    }

    // 队列缓存参数对象
    static class QueueEntry {
        public String path;
        public int width;
        public int height;
    }
}
