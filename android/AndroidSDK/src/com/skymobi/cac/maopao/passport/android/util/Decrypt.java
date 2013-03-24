package com.skymobi.cac.maopao.passport.android.util;

import java.nio.*;

public class Decrypt {

  private char[] bufa = new char[4];
  private char[] bufb = new char[3];
  private char[] out = new char[256];
  private char[] buf = new char[3];

//  private byte[] bufa = new byte[4];
//  private byte[] bufb = new byte[3];
//  private byte[] out = new byte[256];
//  private byte[] buf = new byte[3];
	
  public Decrypt() {
  }

  private static final char decodeTable(char in) {
    char out = 0xFF;

    if (in == 'D') { //14
      out = 7;
    }
    else if (in == 'h') { //7
      out = 14;
    }
    else if (in == 'x') { //59
      out = 63;
    }
    else if (in >= 'A' && in <= 'Z') {
      out = (char) (in - 'A' + 11);
    }
    else if (in >= 'a' && in <= 'k') {
      out = (char) (in - 'a');
    }
    else if (in >= 'l' && in <= 'z') {
      out = (char) (in - 'l' + 47);
    }
    else if (in >= '0' && in <= '9') {
      out = (char) (in - '0' + 37);
    }
    else if ('+' == in) {
      out = 62;
    }
    else if ('/' == in) {
      out = 59;
    }
    else if ('=' == in) {
      out = 64;
    }
    return out;
  }

  public  final String decode(String in, int len) {
    int x, y, z;
    int i, j;

    x = (len - 4) / 4;
    i = j = 0;
    for (z = 0; z < x; z++) {
      for (y = 0; y < 4; y++) {
        if ( (bufa[y] = decodeTable(in.charAt(j + y))) == 0xFF) {
          return "";
        }
      }
      out[i] = (char) (bufa[0] << 2 | (bufa[1] & 0x30) >> 4);
      out[i + 1] = (char) ( (bufa[1] & 0x0F) << 4 | (bufa[2] & 0x3C) >> 2);
      out[i + 2] = (char) ( (bufa[2] & 0x03) << 6 | (bufa[3] & 0x3F));
      i += 3;
      j += 4;
    }
    for (z = 0; z < 4; z++) {
      if ( (bufa[z] = decodeTable(in.charAt(j + z))) == 0xff) {
        return "";
      }
    }
    if ('=' == in.charAt(len - 2)) {
      y = 2;
    }
    else if ('=' == in.charAt(len - 1)) {
      y = 1;
    }
    else {
      y = 0;
    }
    for (z = 0; z < y; z++) {
      bufa[4 - z - 1] = 0x00;
    }
    bufb[0] = (char) (bufa[0] << 2 | (bufa[1] & 0x30) >> 4);
    bufb[1] = (char) ( (bufa[1] & 0x0F) << 4 | (bufa[2] & 0x3C) >> 2);
    bufb[2] = (char) ( (bufa[2] & 0x03) << 6 | (bufa[3] & 0x3F));

    for (z = 0; z < 3 - y; z++) {
      out[i + z] = bufb[z];
    }
    return new String(out, 0, i + z);
  }

  public  final byte[] decodeAsBytes(String in, int len) {
	    int x, y, z;
	    int i, j;

	    x = (len - 4) / 4;
	    i = j = 0;
	    for (z = 0; z < x; z++) {
	      for (y = 0; y < 4; y++) {
	        if ( (bufa[y] = decodeTable(in.charAt(j + y))) == 0xFF) {
	          return null;
	        }
	      }
	      out[i] = (char) (bufa[0] << 2 | (bufa[1] & 0x30) >> 4);
	      out[i + 1] = (char) ( (bufa[1] & 0x0F) << 4 | (bufa[2] & 0x3C) >> 2);
	      out[i + 2] = (char) ( (bufa[2] & 0x03) << 6 | (bufa[3] & 0x3F));
	      i += 3;
	      j += 4;
	    }
	    for (z = 0; z < 4; z++) {
	      if ( (bufa[z] = decodeTable(in.charAt(j + z))) == 0xff) {
	        return null;
	      }
	    }
	    if ('=' == in.charAt(len - 2)) {
	      y = 2;
	    }
	    else if ('=' == in.charAt(len - 1)) {
	      y = 1;
	    }
	    else {
	      y = 0;
	    }
	    for (z = 0; z < y; z++) {
	      bufa[4 - z - 1] = 0x00;
	    }
	    bufb[0] = (char) (bufa[0] << 2 | (bufa[1] & 0x30) >> 4);
	    bufb[1] = (char) ( (bufa[1] & 0x0F) << 4 | (bufa[2] & 0x3C) >> 2);
	    bufb[2] = (char) ( (bufa[2] & 0x03) << 6 | (bufa[3] & 0x3F));

	    for (z = 0; z < 3 - y; z++) {
	      out[i + z] = bufb[z];
	    }
	    
	    byte[] ret = new byte[i + z];
	    for ( int idx = 0; idx < ret.length; idx++ ) {
	    	ret[idx] = (byte)out[idx];
	    }
	    
	    return ret;
	  }
  
  private static final char encodeTable(char in) {
    char out = 0xFF;

    if (in == 7) { //14
      out = 'D';
    }
    else if (in == 14) { //7
      out = 'h';
    }
    else if (59 == in) {
      out = '/';
    }
    else if (in >= 11 && in <= 36) {
      out = (char) (in + 'A' - 11);
    }
    else if (in >= 47 && in <= 61) {
      out = (char) (in + 'l' - 47);
    }
    else if (in <= 10) {
      out = (char) (in + 'a');
    }
    else if (in >= 37 && in <= 46) {
      out = (char) (in + '0' - 37);
    }
    else if (62 == in) {
      out = '+';
    }
    else if (in == 63) { //59
      out = 'x';
    }
    return out;
  }

  public String encode(String in, int len) {
    int x, y, z;
    int i, j;

    x = len / 3;
    y = len % 3;
    i =
        j = 0;
    for (z = 0; z < x; z++) {
      out[i] = encodeTable( (char) (in.charAt(j) >> 2));
      out[i +
          1] = encodeTable( (char) ( (in.charAt(j) & 0x03) << 4 |
                                    in.charAt(j + 1) >> 4));
      out[i +
          2] = encodeTable( (char) ( (in.charAt(j + 1) & 0x0F) << 2 |
                                    in.charAt(j + 2) >> 6));
      out[i + 3] = encodeTable( (char) (in.charAt(j + 2) & 0x3F));
      if ( (out[i] | out[i + 1] | out[i + 2] | out[i + 3]) == 0xff)
        return "";
      i += 4;
      j += 3;
    }
    if (0 != y) {
      buf[0] =
          buf[1] =
          buf[2] = 0x00;
      for (z = 0; z < y; z++) {
        buf[z] = in.charAt(j + z);
      }
      out[i] = encodeTable( (char) (buf[0] >> 2));
      out[i + 1] = encodeTable( (char) ( (buf[0] & 0x03) << 4 | buf[1] >> 4));
      out[i + 2] = encodeTable( (char) ( (buf[1] & 0x0F) << 2 | buf[2] >> 6));
      out[i + 3] = encodeTable( (char) (buf[2] & 0x3F));
      if ( (out[i] | out[i + 1] | out[i + 2] | out[i + 3]) == 0xff)
        return "";
      i += 4;
      for (z = 0; z < 3 - y; z++) {
        out[i - z - 1] = '=';
      }
    }
    out[i] = 0;
    return (String.valueOf(out).substring(0, i));
  }
  
  public static final String bcd2Str(char[] chars) {
    StringBuffer temp = new StringBuffer(chars.length * 2);
    for (int i = 0; i < chars.length; i++) {
      temp.append( (byte) ( (chars[i] & 0xf0) >>> 4));
      temp.append( (byte) (chars[i] & 0x0f));
    }
    return temp.toString().substring(0, 1).equalsIgnoreCase("0") ?
        temp.toString().substring(1) : temp.toString();
  }

  /**10进制串转为BCD码*/
  public static final byte[] str2Bcd(String str) {
    if (str.length() % 2 != 0)
      str = "0" + str;
    StringBuffer sb = new StringBuffer(str);
    ByteBuffer bb = ByteBuffer.allocate(str.length() / 2);
    int i = 0;
    while (i < str.length()) {
      bb.put( (byte) ( (Integer.parseInt(sb.substring(i, i + 1))) << 4 |
                      Integer.parseInt(sb.substring(i + 1, i + 2))));
      i = i + 2;
    }
    return bb.array();
  }

  private static final int toInt(char[] bRefArr) {
    int iOutcome = 0;
    char bLoop;
    for (int i = 3; i >= 0; i--) {
      bLoop = bRefArr[i];
      iOutcome += (bLoop & 0xFF) << (8 * (3 - i));
    }
    return iOutcome;
  }

  private static final short toShort(char[] bRefArr) {
    short iOutcome = 0;
    char bLoop;
    for (int i = 1; i >= 0; i--) {
      bLoop = bRefArr[i];
      iOutcome += (bLoop & 0xFF) << (8 * (1 - i));
    }
    return iOutcome;
  }

  public String getSmsInfos(String encoded) {
    try {
      int pos = 0;
      String hsman = "";
      String hstype = "";
      String IMEI = "";
      String IMSI = "";
      String appid = "";
      String vmver = "";
      String hsver = "";
      String userLayer = "";
      String decoded = decode(encoded, encoded.length());

      char[] a = decoded.toCharArray();
      /* 内容编码1.0 */
      if ((a[0] != 0x1B) && (a[0] != 0x10)) {
        //decoded = decoded.replaceAll("'", "");
        return "1/" + decoded;
      }
      /* 内容编码2.0 */
      else if ((a[0] == 0x1B)){
        // hsman
        pos += 1;
        hsman = new String(a, pos + 1, a[pos]);
        // hstype
        pos += (1 + a[pos]);
        hstype = new String(a, pos + 1, a[pos]);
        // IMEI
        pos += (1 + a[pos]);
        IMEI = new String(a, pos, 8);
        IMEI = bcd2Str(IMEI.toCharArray());
        IMEI = IMEI.substring(0, 15);
        // IMSI
        pos += 8;
        IMSI = new String(a, pos, 8);
        IMSI = bcd2Str(IMSI.toCharArray());
        IMSI = IMSI.substring(0, 15);
        // AppId
        pos += 8;
        String tmp = new String(a, pos, 4);
        appid = appid.valueOf(toInt(tmp.toCharArray()));
        // VMVer
        pos += 4;
        tmp = new String(a, pos, 4);
        vmver = vmver.valueOf(toInt(tmp.toCharArray()));
        // HSVer
        pos += 4;
        tmp = new String(a, pos, 4);
        hsver = hsver.valueOf(toInt(tmp.toCharArray()));
        // MessageNum
        pos += 4;
        String MessageNum = new String().valueOf(new Integer(a[pos]));
        // MessageIndex
        String MessageIndex = new String().valueOf(new Integer(a[pos + 1]));
        // Reserve
        pos += 2;
        String Reserve = new String().valueOf(new Integer(a[pos]));
        // AppType
        String AppType = new String().valueOf(new Integer(a[pos + 1]));
        pos += 2;
        // userLayer
        userLayer = new String(a, pos, a.length - pos);
        return "2/" + hsman + "/" + hstype + "/" + IMEI + "/" + IMSI + "/" +
            appid + "/" + vmver + "/" + hsver + "/" + MessageNum + "/" +
            MessageIndex + "/" + Reserve + "/" + AppType + "/" + userLayer;
      }
      /* 内容编码2.1 */
      else if ((a[0] == 0x10)){
        // AppId
        pos += 1;
        String tmp = new String(a, pos, 4);
        appid = appid.valueOf(toInt(tmp.toCharArray()));
        // VMVer
        pos += 4;
        tmp = new String(a, pos, 2);
        vmver = vmver.valueOf(toShort(tmp.toCharArray()));
        // HSVer
        pos += 2;
        tmp = new String(a, pos, 4);
        hsver = hsver.valueOf(toInt(tmp.toCharArray()));
        // manufactory
        pos += 4;
        String manufactory = new String(a, pos, 8);
        // HandsetType
        pos += 8;
        String HandsetType = new String(a, pos, 8);
        // IMEI
        pos += 8;
        //IMEI = new String(a, pos, 8);
        //IMEI = bcd2Str(IMEI.toCharArray());
        //IMEI = IMEI.substring(0, 15);
        // IMSI
        //pos += 8;
        IMSI = new String(a, pos, 8);
        IMSI = bcd2Str(IMSI.toCharArray());
        IMSI = IMSI.substring(0, 15);
        // MessageNum
        pos += 8;
        String MessageNum = new String().valueOf(new Integer(a[pos]));
        // MessageIndex
        String MessageIndex = new String().valueOf(new Integer(a[pos + 1]));
        // Reserve
        //pos += 2;
        //String Reserve = new String().valueOf(new Integer(a[pos]));
        // userLayer
        pos += 2;
        userLayer = new String(a, pos, a.length - pos);

        return "3/" + appid + "/" + vmver + "/" + hsver + "/" + manufactory + "/" +
            HandsetType + "/" + IMSI + "/" + MessageNum + "/" +
            MessageIndex + "/" + userLayer;// + "/" + Reserve;
      }
      /* 未知的内容编码 */
      else {
        return "";
      }
    }
    catch (Exception ex) {
      return null;
    }
  }
//  
//  public static void main(String[] args) {
//	  
//	  String s = "20100104121109-1";
//	  System.out.println( new Decrypt().encode(s, s.length()));
//	  
//	  String s2 = "123456";
//	  System.out.println( new Decrypt().encode(s2, s2.length()));
//  }
}