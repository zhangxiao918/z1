package org.bluestome.satelliteweather;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.Assert;
import android.app.Application;

/** 该类提供全局方法和变量功能，由系统启动时自动构造 */
public class MainApp extends Application {

	private static MainApp instance = null;
	private final ExecutorService executorService = Executors
			.newSingleThreadExecutor();
	private Map<String, String> imageCache = new HashMap<String, String>();
	private String lastModifyTime = null;
	private AtomicInteger atomicInteger = new AtomicInteger(0x0101);
	private boolean isConnected = true;

	public static MainApp i() {
		if (instance == null) {
			Assert.assertTrue("获取到的application 为空", instance != null);
		}
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
	}

	/**
	 * @return the executorService
	 */
	public ExecutorService getExecutorService() {
		return executorService;
	}

	public String getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(String lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	/**
	 * @return the imageCache
	 */
	public Map<String, String> getImageCache() {
		return imageCache;
	}

	/**
	 * @param imageCache
	 *            the imageCache to set
	 */
	public void setImageCache(Map<String, String> imageCache) {
		this.imageCache = imageCache;
	}

	/**
	 * @return the atomicInteger
	 */
	public AtomicInteger getAtomicInteger() {
		return atomicInteger;
	}

	/**
	 * @param atomicInteger
	 *            the atomicInteger to set
	 */
	public void setAtomicInteger(AtomicInteger atomicInteger) {
		this.atomicInteger = atomicInteger;
	}

	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}

}
