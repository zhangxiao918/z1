package com.chinamilitary.test;

//线程类
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadNetThread extends Thread {
	private InputStream randIn;

	private RandomAccessFile randOut;

	private URL url;

	private long block;

	private int threadId = -1;

	private boolean done = false;

	public DownloadNetThread(URL url, RandomAccessFile out, long block,
			int threadId) {
		this.url = url;
		this.randOut = out;
		this.block = block;
		this.threadId = threadId;
	}

	public void run() {
		try {
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			http.setRequestProperty("Range", "bytes=" + block * (threadId - 1)
					+ "-");
			randIn = http.getInputStream();
		} catch (IOException e) {
			System.err.println(e);
		}

		// //////////////////////
		byte[] buffer = new byte[1024];
		int offset = 0;
		long localSize = 0;
		System.out.println("线程" + threadId + "开始下载");
		try {
			while ((offset = randIn.read(buffer)) != -1 && localSize <= block) {
				randOut.write(buffer, 0, offset);
				localSize += offset;
			}
			randOut.close();
			randIn.close();
			done = true;
			System.out.println("线程" + threadId + "完成下载");
			this.interrupt();
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	public boolean isFinished() {
		return done;
	}
}
