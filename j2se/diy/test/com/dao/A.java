package com.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.Random;

public class A {

	// http://bizhi.zhuoku.com/2012/03/18/jingxuan/jingxuan286.jpg
	// http://image.tuku.china.com/tuku.travel.china.com/travel//pic/2012-03-21/6a3d3419-3b1e-48b6-8f2f-98006e9fbe8b.jpg
	final static String IURL = "http://img.tupian.com/64/15864_5.jpg";

	final static String[] URLS = {
			"http://bizhi.zhuoku.com/2012/03/24/youxi/youxi26.jpg",
			"http://bizhi.zhuoku.com/2012/03/24/youxi/youxi27.jpg",
			"http://bizhi.zhuoku.com/2012/03/24/youxi/youxi28.jpg",
			"http://bizhi.zhuoku.com/2012/03/24/youxi/youxi29.jpg",
			"http://bizhi.zhuoku.com/2012/03/24/youxi/youxi30.jpg",
			"http://bizhi.zhuoku.com/2012/03/24/youxi/youxi31.jpg",
			"http://bizhi.zhuoku.com/2012/03/24/youxi/youxi32.jpg",
			"http://bizhi.zhuoku.com/2012/03/24/youxi/youxi33.jpg",
			"http://bizhi.zhuoku.com/2012/03/24/youxi/youxi34.jpg" };
	
	static String SAVE_DIR = "D:/tmp/image/";

	public static void main(String args[]) {
		long start = System.currentTimeMillis();
		try {
			// doGet();
//			doGetContent();
			mutiThread();
			System.out.println("消耗:[" + (System.currentTimeMillis() - start)
					+ "]ms");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void doGet() throws IOException {
		URL url = new URL(IURL);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.connect();

		System.out.println(" > code:" + conn.getResponseCode());
		System.out.println(" > length:" + conn.getContentLength());
		String modifyTime = conn.getHeaderField("Last-Modified");
		System.out.println(" > Last-Modified:" + modifyTime);

	}

	public static void doGetContent() throws IOException {
		URL url = new URL("http://v.youku.com/v_show/id_XMzU5NTA0NDIw.html");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.connect();
		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			InputStream his = (InputStream) conn.getContent();
			byte[] buf = new byte[24];
			int pos;
			while ((pos = his.read(buf)) != -1) {
				System.out.write(buf, 0, pos);
			}
			if (null != his) {
				his.close();
			}
		}
	}

	/**
	 * 多个线程实例化下载请求
	 *
	 */
	public static void mutiThread() {
		for (final String sUrl : URLS) {
			final Thread th = new Thread(new Runnable() {
				public void run() {
					try{
						File file = null;
						Random random = null;
						URL url = new URL(sUrl);
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
						conn.setDoOutput(true);
						conn.connect();
						if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
							random = new Random();
							file = new File(SAVE_DIR+File.separator+random.nextInt()+".jpg");
							InputStream his = (InputStream) conn.getInputStream();
							byte[] buf = new byte[1024];
							int pos;
							OutputStream os = new FileOutputStream(file);
							while ((pos = his.read(buf)) != -1) {
								os.write(buf, 0, pos);
							}
							if(null != os){
								os.flush();
								os.close();
							}
							if (null != his) {
								his.close();
							}
						}
					}catch(Exception e){
					}
				}
			}, String.valueOf(System.currentTimeMillis()));
			th.start();
		}
	}

}
