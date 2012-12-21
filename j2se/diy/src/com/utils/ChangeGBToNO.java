package com.utils;

import java.util.HashMap;

public class ChangeGBToNO {

	private final String[] GBNOStr = {"零", "一", "二", "三", "四", "五", "六",
			"七", "八", "九" };

	private final String[] GBUnitStr_1 = { "十", "百", "千"};// 单位

	private final String[] GBUnitStr_2 = { "亿", "万" };// order

	private final HashMap GBNOMap = new HashMap();

	private final HashMap GBUnitMap = new HashMap();

	public ChangeGBToNO() {
		for (int n = 0; n < GBNOStr.length; n++) {
			GBNOMap.put(GBNOStr[n], n + "");
		}
		GBUnitMap.put("十", 10 + "");
		GBUnitMap.put("百", 100 + "");
		GBUnitMap.put("千", 1000 + "");
		GBUnitMap.put("万", 10000 + "");
		GBUnitMap.put("亿", 100000000 + "");
		GBUnitMap.put("仟", 1000 + "");
	}
	
	public long change(String src) {
		long result = 0;
		int pos = 0;
		for (int n = GBUnitStr_2.length - 1; n >= 0; n--) {
			if (src.indexOf(GBUnitStr_2[n]) < 0)
				src = GBNOStr[0] + GBUnitStr_2[n] + src;
		}
		//按照亿和万做切割。
		String[] block = src.split("[" + GBUnitStr_2[0] + GBUnitStr_2[1] + "]");
		//然后按照亿或万前的位数：比如说二亿就是 2*GBUnitMap.get("亿")
		for (int n = 0; n < block.length - 1; n++) {
			result += deal(block[n])
					* (new Integer((String) GBUnitMap.get(GBUnitStr_2[n]))
							.intValue());
		}

		result += deal(block[2]);

		return result;
	}

	//是算到千位数
	private long deal(String str) {
		if (str == null || str.equals("")) {
			return 0;
		}
		int result = 0;
		int pos = 0;

		// deal 捌
		if (!GBUnitMap.containsKey(str.substring(str.length() - 1))) {
			result += new Integer((String) GBNOMap.get(str.substring(str
					.length() - 1))).intValue();
		}

		//
		for (int n = 0; n < str.length(); n++) {
			String tmp = str.substring(n, n + 1);
			if (GBUnitMap.containsKey(tmp)) {
				if(n==0){
					result += 1
							* (new Integer((String) GBUnitMap.get(tmp)).intValue());
				}
				else{
					result += (new Integer((String) GBNOMap.get(str.substring(
							n - 1, n))).intValue())
							* (new Integer((String) GBUnitMap.get(tmp)).intValue());
				}
			}
		}
		return result;
	}

	public static void main(String[] args) {
		ChangeGBToNO changeGBToNO = new ChangeGBToNO();
		System.out.println("一=" + changeGBToNO.change("一"));
		System.out.println("二=" + changeGBToNO.change("二"));
		System.out.println("三=" + changeGBToNO.change("三"));
		System.out.println("四=" + changeGBToNO.change("四"));
		System.out.println("五=" + changeGBToNO.change("五"));
		System.out.println("六=" + changeGBToNO.change("六"));
		System.out.println("七=" + changeGBToNO.change("七"));
		System.out.println("八=" + changeGBToNO.change("八"));
		System.out.println("九=" + changeGBToNO.change("九"));
		System.out.println("十=" + changeGBToNO.change("十"));
		System.out.println("十一" + changeGBToNO.change("十一"));
		System.out.println("一十一=" + changeGBToNO.change("一十一"));
		System.out.println("二十=" + changeGBToNO.change("二十"));
		System.out.println("二十九=" + changeGBToNO.change("二十九"));
		System.out.println("九十九=" + changeGBToNO.change("九十九"));
		System.out.println("一百=" + changeGBToNO.change("一百"));
		System.out.println("一百零一=" + changeGBToNO.change("一百零一"));
		System.out.println("一百一十三=" + changeGBToNO.change("一百一十三"));
		System.out.println("九百九十九=" + changeGBToNO.change("九百九十九"));
		System.out.println("一千=" + changeGBToNO.change("一千"));
		System.out.println("一千零二十八=" + changeGBToNO.change("一千零二十八"));
		System.out.println("一千四百七十八=" + changeGBToNO.change("一千四百七十八"));
		System.out.println("二万一千四百七十八=" + changeGBToNO.change("二万一千四百七十八"));
		System.out.println("二亿三千万七百二十八=" + changeGBToNO.change("二亿三千万七百二十八"));
		
	}
}