package org.bluestome.pcs.memcache;

import java.util.Date;
import java.util.Iterator;

import org.bluestome.pcs.utils.Configuration;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;

public class MemcacheClient {

	// 创建全局的唯一实例
	protected static MemCachedClient cache = new MemCachedClient();
	
	protected static MemcacheClient client = new MemcacheClient();

	// 设置与缓存服务器的连接池
	static {
		System.out.println(">> memcache:"+Configuration.getValue("memcacheserver.1")+","+Configuration.getValue("memcacheserver.2") );
		// 服务器列表和其权重
		String[] servers = { Configuration.getValue("memcacheserver.1"),Configuration.getValue("memcacheserver.2") };
		Integer[] weights = { 1,2 };

		// 获取socke连接池的实例对象
		SockIOPool pool = SockIOPool.getInstance();

		// 设置服务器信息
		pool.setServers(servers);
		pool.setWeights(weights);

		// 设置初始连接数、最小和最大连接数以及最大处理时间
		pool.setInitConn(5);
		pool.setMinConn(5);
		pool.setMaxConn(250);
		pool.setMaxIdle(1000 * 60 * 60 * 6);

		// 设置主线程的睡眠时间
		pool.setMaintSleep(30);

		// 设置TCP的参数，连接超时等
		pool.setNagle(false);
		pool.setSocketTO(3000);
		pool.setSocketConnectTO(3000);

		// 初始化连接池
		pool.initialize();

		// 压缩设置，超过指定大小（单位为K）的数据都会被压缩
		cache.setCompressEnable(true);
		cache.setCompressThreshold(64 * 1024);
	}

	/**
	 * 保护型构造方法，不允许实例化！
	 * 
	 */
	protected MemcacheClient() {
	}

	/**
	 * 获取唯一实例.
	 * 
	 * @return
	 */
	public static MemcacheClient getInstance() {
		return client;
	}

	/**
	 * 按照过期时间添加一个指定的值到缓存中.
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean add(String key, Object value) {
		return cache.add(key, value,new Date(System.currentTimeMillis()+1000*Long.valueOf(Configuration.getValue("china.expiry"))));
	}

	public boolean add(String key, Object value, Date expiry) {
		return cache.add(key, value, expiry);
	}

	/**
	 * 按照过期时间替换缓存中的数据
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean replace(String key, Object value) {
		return cache.replace(key, value,new Date(System.currentTimeMillis()+1000*Long.valueOf(Configuration.getValue("china.expiry"))));
	}

	public boolean replace(String key, Object value, Date expiry) {
		return cache.replace(key, value, expiry);
	}
	
	/**
	 * 删除缓存中的记录
	 * @param key
	 * @return
	 */
	public boolean remove(String key){
		return cache.delete(key);
	}

	/**
	 * 根据指定的关键字获取对象.
	 * 
	 * @param key
	 * @return
	 */
	public Object get(String key) {
		return cache.get(key);
	}
	
	/**
	 * 获取KEY列表
	 * @return
	 */
	public void listKey(){
		if(cache.stats().size() > 0){
			Iterator it = cache.statsItems().keySet().iterator();
			while(it.hasNext()){
				System.out.println(">> toString:"+it.next().toString());
			}
			Iterator it2 = cache.statsSlabs().keySet().iterator();
			while(it2.hasNext()){
				System.out.println(">> toString:"+it2.next().toString());
			}
			return;
		}else{
			return;
		}
//		return cache.statsItems().keySet().iterator();
	}
}