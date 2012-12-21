package com.utils;

import java.io.FileNotFoundException;

public class CharacterUtils {

	public static boolean containUnrecognizedCode(String s) {
		boolean ret = false;
		if (s == null) {
			return ret;
		}

		for (int i = 0; i < s.length(); i++) {

			char c = s.charAt(i);
			if (c < 0x4E00 || c > 0x9FA5) {
				ret = true;
				break;
			}
		}
		return ret;
	}

	public static void main(String[] args) throws FileNotFoundException {

		System.out.println("containUnrecognizedCode(\"Ñ°\"):"
				+ containUnrecognizedCode("Ñ°"));
		System.out.println("containUnrecognizedCode(\"寻找前世今生，。！--《》\"):"
				+ containUnrecognizedCode("寻找前世今生，。！--《》"));
	}

}
