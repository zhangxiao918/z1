package com.skymobi.cac.maopao.passport.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileHelper {

	private FileHelper() {
	}

	public static String extractFilePath(String filePathName) {
		if (filePathName == null || filePathName.trim().length() == 0)
			return null;
		filePathName = filePathName.replaceAll("\\\\", "/");
		boolean b = filePathName.endsWith("/");
		int pos = filePathName.lastIndexOf("/");
		String temp = filePathName.substring(pos + 1);
		if (!b && temp != null && temp.indexOf(".") == -1) {
			filePathName += "/";
		} else {
			filePathName = filePathName.substring(0, pos + 1);
		}
		return filePathName;
	}

	public static void makeDirs(String dirs) {
		File file = new File(extractFilePath(dirs));
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	public static void createFile(String filename) throws Exception {
		File file = new File(filename);
		if (!file.exists()) {
			file.createNewFile();
		}
	}

	/**
	 * 删除文件，可以是单个文件或文件夹
	 * 
	 * @param path 待删除的文件名
	 * @return 文件删除成功返回true,否则返回false
	 */
	public static void delFile(String path) {
		File file = new File(path);
		if (!file.exists() && file.isFile()) {
			file.delete();
		}
	}

	/**
	 * 将对象序列化至磁盘
	 * */
	public static void writeObject(String path, List<Object> objs) {
		ObjectOutputStream oos = null;
		try {
			createFile(path);
			oos = new ObjectOutputStream(new BufferedOutputStream(
					new FileOutputStream(new File(path))));
			for (Object obj : objs) {
				oos.writeObject(obj);
			}
		} catch (Exception e) {
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * 从磁盘读取对象
	 * */
	public static List<Object> readObject(String path) {
		ObjectInputStream ois = null;
		List<Object> objs = null;
		File file = new File(path);
		if (file.exists()) {
			try {
				ois = new ObjectInputStream(new BufferedInputStream(
						new FileInputStream(file)));
				objs = new ArrayList<Object>();
				while (true) {
					try {
						Object obj = ois.readObject();
						if(obj!=null){
							objs.add(obj);
						}
					} catch (EOFException e) {
						break;
					}
				}
			} catch (Exception e) {
			} finally {
				if (ois != null) {
					try {
						ois.close();
					} catch (Exception e) {
					}
				}
			}
		}
		return null;
	}

}
