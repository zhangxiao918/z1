package com.skymobi.cac.maopao.utils;

public class d {
	public static native byte[] de(byte[] key, byte[] data);
	public static native byte[] en(byte[] key, byte[] data);
	public static native void i(byte[] bytes);
	static {
		System.loadLibrary("e");
	}
}
	
