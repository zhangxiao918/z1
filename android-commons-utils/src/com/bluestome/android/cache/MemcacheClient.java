
package com.bluestome.android.cache;

import android.content.Context;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

public class MemcacheClient {

    private static final String TAG = MemcacheClient.class.getCanonicalName();
    // 创建全局的唯一实例
    private Context ctx;
    protected MemcachedClient cache = null;
    private InputStream in;
    private Properties properties;
    protected static MemcacheClient instance = null;

    /**
     * 保护型构造方法，不允许实例化！
     */
    protected MemcacheClient(Context ctx) {
        this.ctx = ctx;
        try {
            in = this.ctx.getResources().getAssets().open("cache.config");
            properties = new Properties();
            properties.load(in);
            in.close();
            init();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "can not find file ");
        } catch (IOException e) {
            Log.e(TAG, "can not read file");
        }
    }

    private void init() {
        if (null == properties) {
            Log.e(TAG, "unable to get properties instance");
            return;
        }
        // 设置与缓存服务器的连接池

        // 服务器列表和其权重
        String[] servers = {
                properties.getProperty("server_ip")
        };
        Integer[] weights = {
                1
        };

        // 获取socke连接池的实例对象
        SockIOPool pool = SockIOPool.getInstance();

        // 设置服务器信息
        pool.setServers(servers);
        pool.setWeights(weights);

        // 设置初始连接数、最小和最大连接数以及最大处理时间
        int iConn = Integer.valueOf(properties.getProperty("init_conn", "1"));
        Log.d(TAG, "iConn:" + iConn);
        pool.setInitConn(iConn);
        int minConn = Integer.valueOf(properties.getProperty("min_conn", "1"));
        Log.d(TAG, "minConn:" + minConn);
        pool.setMinConn(minConn);
        int maxConn = Integer.valueOf(properties.getProperty("max_conn", "1"));
        Log.d(TAG, "maxConn:" + maxConn);
        pool.setMaxConn(maxConn);
        int maxIdle = Integer.valueOf(properties.getProperty("max_idle", "360000"));
        Log.d(TAG, "maxIdle:" + maxIdle);
        pool.setMaxIdle(maxIdle);

        // 设置主线程的睡眠时间
        int mainSleep = Integer.valueOf(properties.getProperty("main_sleep_time", "30"));
        Log.d(TAG, "mainSleep:" + mainSleep);
        pool.setMaintSleep(mainSleep);

        // 设置TCP的参数，连接超时等
        pool.setNagle(false);
        pool.setSocketTO(3000);
        pool.setSocketConnectTO(3000);

        // 初始化连接池
        pool.initialize();
        // 初始化缓存客户端
        cache = new MemcachedClient();
    }

    /**
     * 获取唯一实例.
     * 
     * @return
     */
    public static MemcacheClient getInstance(Context ctx) {
        if (null == instance) {
            instance = new MemcacheClient(ctx);
        }
        return instance;
    }

    /**
     * 按照过期时间添加一个指定的值到缓存中.
     * 
     * @param key
     * @param value
     * @return
     */
    public boolean add(String key, Object value) {
        if (null == cache)
            return false;
        return cache.add(key, value, value.hashCode());
    }

    public boolean add(String key, Object value, Date expiry) {
        if (null == cache)
            return false;
        return cache.add(key, value, expiry);
    }

    /**
     * 按照过期时间替换缓存中的数据
     * 
     * @param key
     * @param value
     * @return
     */
    public boolean replace(String key, Object value) {
        if (null == cache)
            return false;
        return cache.replace(key, value, value.hashCode());
    }

    public boolean replace(String key, Object value, Date expiry) {
        if (null == cache)
            return false;
        return cache.replace(key, value, expiry);
    }

    /**
     * 删除缓存中的记录
     * 
     * @param key
     * @return
     */
    public boolean remove(String key) {
        if (null == cache)
            return false;
        return cache.delete(key);
    }

    /**
     * 根据指定的关键字获取对象.
     * 
     * @param key
     * @return
     */
    public Object get(String key) {
        if (null == cache)
            return false;
        return cache.get(key);
    }

    public Map getStats() {
        if (null != cache) {
            return cache.stats();
        }
        return null;
    }
}
