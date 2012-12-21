package com.utils;

import java.util.ArrayList;
import java.util.Collections;

public class NumberUtils{

	// 把"1"转化成1时用下标找TOBIG[1]就是对应的
	private static final String[] TOBIG = { "零", "一", "二", "三", "四", "五", "六",
			"七", "八", "九" };

	private static final String[] FOUR_DIGIT_UNIT = { "", "十", "百", "千" };

	private static ChangeGBToNO changeGBTONO = new ChangeGBToNO();
	
	
	public static String numeric2Chinese(int digit) {
		return numeric2Chinese(String.valueOf(digit));
	}

	public static String chinese2Numeric(String chapterName) {
		chapterName = chapterName.substring(1, chapterName.length()-1);
		return String.valueOf(changeGBTONO.change(chapterName));
	}
	
	public static String numeric2Chinese(String digit) {
		String[] unit = { "", "万", "亿" };
		int indexOfUnit = 0;

		ArrayList<String> array = new ArrayList<String>();
		if (digit.length() <= 4) {
			return fourDigits2Chinese(digit);
		} else {
			for (int i = digit.length(); i > 0; i -= 4, indexOfUnit++) {
				String part = digit.substring(i - 4 < 0 ? 0 : i - 4, i);
				array.add(fourDigits2Chinese(part) + unit[indexOfUnit]);
			}
		}

		StringBuilder chineseDigit = new StringBuilder();
		Collections.reverse(array);
		for (String temp : array) {
			chineseDigit.append(temp);
		}
		return chineseDigit.toString();
	}

	private static String fourDigits2Chinese(String digit) {

		int value = Integer.parseInt(digit);
		boolean hasZero = false;

		StringBuilder chineseDigit = new StringBuilder();

		int exponent = 3;
		for (; exponent >= 0; exponent--) {

			int divisor = (int) Math.pow(10, exponent);
			int result = value / divisor;
			if (result != 0) {
				chineseDigit.append(TOBIG[result]);
				chineseDigit.append(FOUR_DIGIT_UNIT[exponent]);
				hasZero = false;
			} else if (result == 0 && !hasZero) {
				chineseDigit.append(TOBIG[0]);
				hasZero = true;
			}
			value %= divisor;
		}

		// Delete redundant zero at head
		if (chineseDigit.toString().startsWith("零")) {
			chineseDigit.deleteCharAt(0);
		}

		// Delete redundant zero at tail
		if (chineseDigit.toString().endsWith("零")) {
			return chineseDigit.deleteCharAt(chineseDigit.length() - 1)
					.toString();
		} else {
			return chineseDigit.toString();
		}

	}

	public static void main(String[] args) {
		for (int i = 0; i <= 200; i++) {
			System.out.println("numeric2Chinese(" + i + "):"
					+ numeric2Chinese(i));
		}

		System.out.println("numeric2Chinese(1004):" + numeric2Chinese(1004));
		System.out.println("numeric2Chinese(12345):" + numeric2Chinese(12345));
		System.out.println("numeric2Chinese(11150040):"
				+ numeric2Chinese("11150040"));
		System.out.println("numeric2Chinese(14007000):"
				+ numeric2Chinese("14007000"));
		System.out.println("numeric2Chinese(123456789):"
				+ numeric2Chinese("123456789"));
		
		System.out.println("一=" + changeGBTONO.change("一"));
		System.out.println("二=" + changeGBTONO.change("二"));
		System.out.println("三=" + changeGBTONO.change("三"));
		System.out.println("四=" + changeGBTONO.change("四"));
		System.out.println("五=" + changeGBTONO.change("五"));
		System.out.println("六=" + changeGBTONO.change("六"));
		System.out.println("七=" + changeGBTONO.change("七"));
		System.out.println("八=" + changeGBTONO.change("八"));
		System.out.println("九=" + changeGBTONO.change("九"));
		System.out.println("十=" + changeGBTONO.change("十"));
		System.out.println("十一" + changeGBTONO.change("十一"));
		System.out.println("一十一=" + changeGBTONO.change("一十一"));
		System.out.println("二十=" + changeGBTONO.change("二十"));
		System.out.println("二十九=" + changeGBTONO.change("二十九"));
		System.out.println("九十九=" + changeGBTONO.change("九十九"));
		System.out.println("一百=" + changeGBTONO.change("一百"));
		System.out.println("一百零一=" + changeGBTONO.change("一百零一"));
		System.out.println("一百一十三=" + changeGBTONO.change("一百一十三"));
		System.out.println("九百九十九=" + changeGBTONO.change("九百九十九"));
		System.out.println("一千=" + changeGBTONO.change("一千"));
		System.out.println("一千零二十八=" + changeGBTONO.change("一千零二十八"));
		System.out.println("一千四百七十八=" + changeGBTONO.change("一千四百七十八"));
		System.out.println("二万一千四百七十八=" + changeGBTONO.change("二万一千四百七十八"));
		System.out.println("二亿三千万七百二十八=" + changeGBTONO.change("二亿三千万七百二十八"));
		

	}
}
