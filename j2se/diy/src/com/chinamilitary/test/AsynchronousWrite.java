package com.chinamilitary.test;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.chinamilitary.util.CommonUtil;
import com.chinamilitary.util.IOUtil;

public class AsynchronousWrite {

	// 保存路径
	private static String PIC_SAVE_PATH = "F:\\tmp\\tmp1\\";

	// 线程池实例
	private ExecutorService responseExecutor = Executors.newFixedThreadPool(10);

	public AsynchronousWrite() {
	}

	public void write(final byte[] in) {
		OutputStream out = null;
		try {
			out = new FileOutputStream(new File(PIC_SAVE_PATH + File.separator
					+ System.currentTimeMillis() + ".txt"));
			if (null != in) {
				out.write(in);
			}
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					System.out.println("IO异常");
				} catch (Exception e) {
					System.out.println("异常" + e.getMessage());
				}
			}
		}
	}

	public void write(final String url,final int articleId,final String fileName) {
		try {
			if (null != url) {
				IOUtil.createPicFile(url, PIC_SAVE_PATH
						+ CommonUtil.getDate("") + File.separator
						+ articleId + File.separator
						+ fileName.replace(".", "_s."));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void execute(final byte[] in) {
		Runnable handler = new Runnable() {
			public void run() {
				try {
					// TODO
					write(in);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		responseExecutor.execute(handler);
	}

	/**
	 * public void setIn(byte[] in){ this.in = in; }
	 * 
	 * public byte[] getIn(){ return in; }
	 * 
	 * public void setFilePath(String filePath){ this.filePath = filePath; }
	 * 
	 * public String getFilePath(){ return filePath; }
	 */

	public static void main(String args[]) {
		try {
			Thread rth = new Thread(new Runnable() {
				AsynchronousWrite write = new AsynchronousWrite();
				private boolean isRun = true;
				int count = 0;
				public void run() {
					try {
						while (isRun()) {
							System.out.println("count:"+count);
							if(count % 1000 == 0){
								System.out.println("sleep");
								Thread.sleep(1000);
								count = 0;
							}
							count ++;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				public boolean isRun() {
					return isRun;
				}
				public void setRun(boolean isRun) {
					this.isRun = isRun;
				}
				
				
			});
			rth.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	Thread th = new Thread(new Runnable() {
//		AsynchronousWrite write = new AsynchronousWrite();
//		private boolean running = true;
//		public void run() {
//			try {
//				while (isRunning()) {
//					write.write("http://image.tuku.china.com/tuku.military.china.com/military//pic/2010-08-04/cd14504c-9a02-4792-9b0e-990e93610eaf.jpg", 26421, "cd14504c-9a02-4792-9b0e-990e93610eaf.jpg");
//					Thread.sleep(1000);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		public boolean isRunning() {
//			return running;
//		}
//		public void setRunning(boolean running) {
//			this.running = running;
//		}
//		
//		
//	});
//	th.start();

}