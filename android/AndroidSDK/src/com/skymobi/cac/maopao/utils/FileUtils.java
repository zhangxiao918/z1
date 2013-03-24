package com.skymobi.cac.maopao.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

public class FileUtils {
	
	public static File checkExternalStorageState() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File sdCardDir = Environment.getExternalStorageDirectory();// 获取SDCard目录
			return sdCardDir;
		}
		return null;
	}

	/**
	 * 检查SD卡是否有足够空间
	 * @param size
	 * @return
	 */
	public static boolean checkSdcardHavEonughStorageState(long size){
		return checkStorageRom(checkExternalStorageState(),size);
	}
	
	 private static boolean isSDCardExist()
	  {
	    return Environment.getExternalStorageState().equals("mounted");
	  }
	
	public static String getSdFilePath(String saveDir){
		if (isSDCardExist()){
			return (Environment.getExternalStorageDirectory() + File.separator + saveDir);
		}else{
			return saveDir;
		}
	}
	
	/**
	 * 检查手机内存是否有足够空间
	 * @param size
	 * @return
	 */
	public static boolean checkPhoneHavEnghRom(long size){
		return checkStorageRom(Environment.getDataDirectory(),size);
	}
	
	private static boolean checkStorageRom(File file,long size){
		if(file==null)
			return false;
		
		StatFs mStat = new StatFs(file.getAbsolutePath());
		long blockSize = mStat.getBlockSize();
		long avaleCout = mStat.getAvailableBlocks();
		long val = avaleCout * blockSize;
		if(size<=val)
			return true;
		else
			return false;
	}
	
	
	/**
	 * UN ZIP
	 * 
	 * @param targetPath
	 *            destPath
	 * @param zipFilePath
	 *            zip source path
	 * @return
	 */
	public static boolean unZipFile(Context context,String targetPath, String zipFilePath,boolean isInsdcard) {
		File zipFile = new File(zipFilePath);
		InputStream is = null;
		boolean bool = false;
		try {
			is = new FileInputStream(zipFile);
			bool = unZipFile(context,targetPath, is,isInsdcard);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		return bool;
	}

	/**
	 * 
	 * @param context
	 * @param is
	 * @param destFileName
	 * @return
	 */
	public static boolean unZipFile(Context context, InputStream is, String destFileName) {
		if (is == null)
			return false;
		ZipInputStream zis = null;
		FileOutputStream fos = null;
		int len = 4 * 1024;
		byte[] buffer = new byte[len];
		File baseFile = new File(context.getFilesDir(), destFileName);
		if(!baseFile.exists())
			baseFile.mkdirs();
		try {
			zis = new ZipInputStream(is);
			ZipEntry entry = null;
			while ((entry = zis.getNextEntry()) != null) {
				if(!checkPhoneHavEnghRom(entry.getSize()))
					return false;
				String zipPath = entry.getName();
				try {
					if (entry.isDirectory()) {
						File zipFolder = new File(baseFile, zipPath);
						if (!zipFolder.exists()) {
							zipFolder.mkdirs();
						}
					} else {

						File file = new File(baseFile, zipPath);
						if (!file.exists()) {
							File pathDir = file.getParentFile();
							pathDir.mkdirs();
							file.createNewFile();
						}
						int offset = 0;
						fos = new FileOutputStream(file);
						while ((offset = zis.read(buffer, 0, len)) != -1) {
							fos.write(buffer, 0, offset);
							fos.flush();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					Log.e(FileUtils.class.getName(), zipPath + "解压失败");
					continue;
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (zis != null) {
					zis.close();
					zis = null;
				}
				if (is != null) {
					is.close();
					is = null;
				}
				if (fos != null) {
					fos.close();
					fos = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public static boolean unZipFile(Context context,String targetPath, InputStream is,boolean isInsdcard) {
		if (is == null)
			return false;
		ZipInputStream zis = null;
		FileOutputStream fos = null;
		int len = 4 * 1024;
		byte[] buffer = new byte[len];
		
		try {
			zis = new ZipInputStream(is);
			ZipEntry entry = null;
			while ((entry = zis.getNextEntry()) != null) {
				File base = null;
				if(isInsdcard){
					if(!checkSdcardHavEonughStorageState(entry.getSize())){
						return false;
					}else{
						base = checkExternalStorageState();
					}
					
				}else{
					if(!checkPhoneHavEnghRom(entry.getSize())){
						return false;
					}else{
						base = context.getFilesDir();
					}
				}
				String zipPath = entry.getName();
				try {
					String temp = new StringBuffer(targetPath + File.separator + zipPath).toString();
					if (entry.isDirectory()) {
						File zipFolder = new File(base,temp);
						if (!zipFolder.exists()) {
							zipFolder.mkdirs();
						}
					} else {
						File file = new File(base,temp);
						if (!file.exists()) {
							File pathDir = file.getParentFile();
							pathDir.mkdirs();
							file.createNewFile();
						}
						fos = new FileOutputStream(file);
						int offset = 0;
						while ((offset = zis.read(buffer, 0, len)) != -1) {
							fos.write(buffer, 0, offset);
							fos.flush();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					Log.e(FileUtils.class.getName(), zipPath + "解压失败");
					continue;
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (zis != null) {
					zis.close();
					zis = null;
				}
				if (is != null) {
					is.close();
					is = null;
				}
				if (fos != null) {
					fos.close();
					fos = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}
	
	/**
	 * close InputStream
	 * 
	 * @param ins
	 */
	public static void closeInputStream(InputStream ins) {
		if (ins != null) {
			try {
				ins.close();
				ins = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * close OutputStream
	 * 
	 * @param out
	 */
	public static void closeOutputStream(OutputStream out) {
		if (out != null) {
			try {
				out.close();
				out = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static  void copyFile(File sourceFile,File targetFile) 
			throws IOException{ 
		FileInputStream input = new FileInputStream(sourceFile); 
		BufferedInputStream inBuff=new BufferedInputStream(input); 
		FileOutputStream output = new FileOutputStream(targetFile); 
		BufferedOutputStream outBuff=new BufferedOutputStream(output); 
	
		try{
			
			byte[] b = new byte[1024 * 5]; 
			int len; 
			while ((len =inBuff.read(b)) != -1) { 
			outBuff.write(b, 0, len); 
			} 
			outBuff.flush(); 
			
		}catch(IOException e){
			throw(e);
		}finally{
			try{
				inBuff.close(); 
			}catch(IOException e){
				
			}
			try{
				outBuff.close(); 
			}catch(IOException e){
				
			}
			try{
			
				output.close();
			}catch(Exception e){
				
			}
			try{
				input.close();
			}catch(Exception e){
				
			}
		}
	}
}
