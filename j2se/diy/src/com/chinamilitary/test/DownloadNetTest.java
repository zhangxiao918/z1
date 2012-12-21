package com.chinamilitary.test;

import java.io.*;
import java.net.*;

public class DownloadNetTest {
	private File fileOut;

	private URL url;

	private long fileLength = 0;

	// 初始化线程数
	private int ThreadNum = 5;

	public DownloadNetTest() {
		try {
			System.out.println("正在链接URL");
			url = new URL("http://image6.tuku.cn/pic/ziranfengjing/pubu/055.jpg");
			HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
			fileLength = urlcon.getContentLength();
			if (urlcon.getResponseCode() >= 400) {
				System.out.println("服务器响应错误");
				System.exit(-1);
			}
			if (fileLength <= 0)
				System.out.println("无法获知文件大小");
			// 打印信息
			printMIME(urlcon);
			System.out.println("文件大小为" + fileLength / 1024 + "K");
			// 获取文件名
			String trueurl = urlcon.getURL().toString();
			String filename = trueurl.substring(trueurl.lastIndexOf('/') + 1);
			fileOut = new File("D://", filename);
		} catch (MalformedURLException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		}
		init();
	}

	private void init() {
		DownloadNetThread[] down = new DownloadNetThread[ThreadNum];
		try {
			for (int i = 0; i < ThreadNum; i++) {
				RandomAccessFile randOut = new RandomAccessFile(fileOut, "rw");
				randOut.setLength(fileLength);
				long block = fileLength / ThreadNum + 1;
				randOut.seek(block * i);
				down[i] = new DownloadNetThread(url, randOut, block, i + 1);
				down[i].setPriority(7);
				down[i].start();
			}
			// 循环判断是否下载完毕
			boolean flag = true;
			while (flag) {
				Thread.sleep(500);
				flag = false;
				for (int i = 0; i < ThreadNum; i++)
					if (!down[i].isFinished()) {
						flag = true;
						break;
					}
			}// end while
			System.out.println("文件下载完毕，保存在" + fileOut.getPath());
		} catch (FileNotFoundException e) {
			System.err.println(e);
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println(e);
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.err.println(e);
		}
	}

	private void printMIME(HttpURLConnection http) {
		for (int i = 0;; i++) {
			String mine = http.getHeaderField(i);
			if (mine == null)
				return;
			System.out.println(http.getHeaderFieldKey(i) + ":" + mine);
		}
	}

	public static void main(String[] args) {
		DownloadNetTest app = new DownloadNetTest();
	}
}