package com.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.core.io.FileSystemResource;

public class SpringUtils {
	public static FileSystemResource genFileResourceByEnv(String env,
			String fileName) {
		String value = System.getenv(env);
		if (null == value) {
			throw new RuntimeException(env + " not defined.");
		}
		return new FileSystemResource(value.trim() + File.separator + fileName);
	}

	public static FileSystemResource genFileResourceByProp(String propFileName,
			String key, String fileName) throws IOException {
		InputStream is = SpringUtils.class.getResourceAsStream(propFileName);
		Properties prop = new Properties();
		prop.load(is);
		String value = (String) prop.get(key);
		if (null == value) {
			throw new RuntimeException(key + " not defined in file ["
					+ propFileName + "].");
		}
		return new FileSystemResource(value.trim() + File.separator + fileName);
	}
}
