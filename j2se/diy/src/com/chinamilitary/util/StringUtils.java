package com.chinamilitary.util;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	/**
	 * 判断连接中是否存在创建文件夹时的非法字符
	 * 
	 * @param str
	 *            输入字符串
	 * @return
	 */
	public static String illageString(String str) {
		
		if(str.indexOf("'") > -1){
			str = str.replaceAll("'", "’");
		}
		
		if (str.indexOf("\"") != -1) {
			str = str.replace("\"", "");
		}

		if (str.indexOf("“") != -1) {
			str = str.replace("“", "");
		}

		if (str.indexOf("”") != -1) {
			str = str.replace("“", "");
		}

//		if (str.indexOf("?") != -1) {
//			str = construct(str);
//		}

		if (str.indexOf("\\") != -1) {
			str = str.replaceAll("\\", "");
		}

		if (str.indexOf("//") != -1) {
			str = str.replaceAll("//", "");
		}

		if (str.indexOf(":") != -1) {
			str = str.replaceAll(":", "：");
		}
		
		if (str.indexOf("*") != -1) {
			str = str.replace("*", "×");
		}
		if(str.indexOf("!") != -1){
			str = str.replace("!", "！");
		}

		if (str.indexOf(">") != -1) {
			str = str.replace(">", "");
		}

		if (str.indexOf("<") != -1) {
			str = str.replace("<", "");
		}

		if (str.indexOf("||") != -1) {
			str = str.replace("||", "");
		}

		if (str.indexOf("?") != -1) {
			str = str.replace("?", "？");
		}
		
		return str;
	}

	public static String construct(String sql) {
		String key = "\\?";
		Pattern p = Pattern.compile(key);
		Matcher m = p.matcher(sql);
		StringBuffer stringBuffer = new StringBuffer();
		int i = 0;
		boolean result = m.find();
		while (result) {
			m.appendReplacement(stringBuffer, "");
			result = m.find();
			i++;
		}
		return String.valueOf(m.appendTail(stringBuffer));
	}
	
	
	/**
	 * 生成保存目录
	 * @param input
	 * @return
	 */
	 public static String gerDir(String input){
		StringBuffer sb = new StringBuffer();
		if(input.length() <= 2){
			for(int i=0;i<input.length();i++){
				sb.append(input.charAt(i)+File.separator);
			}
		}else{
			for(int i=0;i<input.length()-2;i++){
				sb.append(input.charAt(i)+File.separator);
			}
		}
		return sb.toString();
	 }
	 
	/**
	 * 生成保存目录
	 * @param input
	 * @return
	 */
	 public static String gerDir2(String input,int depth){
		StringBuffer sb = new StringBuffer();
		if(input.length() <= 2){
			for(int i=0;i<input.length();i++){
				sb.append(input.charAt(i)+File.separator);
			}
		}else{
			for(int i=0;i<input.length()-depth;i++){
				sb.append(input.charAt(i)+File.separator);
			}
		}
		return sb.toString();
	 }
	 
	public static void main(String args[]){
		String articleId = "1234567";
//		String fileName = "20100902/56261/suyang001.jpg";
//		String str = "?1?2?3?4*5**";
//		System.out.println(illageString(str));
//		System.out.println(fileName.substring(0,fileName.lastIndexOf(File.separator)));
		for(int i=100000;i<100010;i++){
			System.out.println(gerDir(String.valueOf(i))+i);
		}
	}

}
