package com.chinamilitary.util;
import java.io.*;
import java.net.SocketException;
import java.util.*;

public class CommonUtil {

	  /**
	   * 得到当前日期
	   * @param chr为年月日间的间隔符，如2003-08-31中的'-'
	   * @param str为时分秒之间的间隔符，如11:24:30.332
	   * @return String
	   */
	  public static String getDate(String chr,String str) {
	    //设置时区为北京时间 GMT+08为东八区的意思，即北京时间所在时区
		TimeZone tz = TimeZone.getTimeZone("GMT+08"); 
		//通过参数tz和时区国家获得Calendar实例
	    Calendar cal = Calendar.getInstance(tz,Locale.CHINA);
	    
	    StringBuffer sb = new StringBuffer();
	    
	    //年份
	    sb.append(cal.get(Calendar.YEAR) + chr);
	    //月份
	    if(cal.get(Calendar.MONTH)+1 < 10){
		    sb.append("0");
	    	sb.append((cal.get(Calendar.MONTH)+1));
		    sb.append(chr);
	    }else{
		    sb.append((cal.get(Calendar.MONTH)+1));
		    sb.append(chr);
	    }
	    //日
	    if(cal.get(Calendar.DAY_OF_MONTH) < 10){
	    	sb.append("0");
		    sb.append((cal.get(Calendar.DAY_OF_MONTH)));
		    sb.append((char)32);
	    }else{
		    sb.append((cal.get(Calendar.DAY_OF_MONTH)));
		    sb.append((char)32);
	    }
	    //小时
	    if(cal.get(Calendar.HOUR_OF_DAY) < 10){
	    	sb.append("0");
	    	sb.append(cal.get(Calendar.HOUR_OF_DAY));
	    	sb.append(str);
	    }else{
	    	sb.append(cal.get(Calendar.HOUR_OF_DAY));
	    	sb.append(str);
	  	}
	    //分钟
	    if(cal.get(Calendar.MINUTE) < 10){
	    	sb.append("0");
	    	sb.append(cal.get(Calendar.MINUTE));
	    	sb.append(str);
	    }else{
	    	sb.append(cal.get(Calendar.MINUTE));
	    	sb.append(str);
	    }
	    //秒
	    if(cal.get(Calendar.SECOND) < 10){
	    	sb.append("0");
	    	sb.append(cal.get(Calendar.SECOND));
	    }else{
	    	sb.append(cal.get(Calendar.SECOND));
	    }
	    
	    return sb.toString();
	  }
	  
	  /**
	   * 生成时间日期字符串 例如:20080101000000
	   * @return
	   */
	  public static String getDateTimeString(){
		    //设置时区为北京时间 GMT+08为东八区的意思，即北京时间所在时区
			TimeZone tz = TimeZone.getTimeZone("GMT+08"); 
			//通过参数tz和时区国家获得Calendar实例
		    Calendar cal = Calendar.getInstance(tz,Locale.CHINA);
		    
		    StringBuffer sb = new StringBuffer();
		    
		    //年份
		    sb.append(cal.get(Calendar.YEAR));
		    //月份
		    if(cal.get(Calendar.MONTH)+1 < 10){
			    sb.append("0");
		    	sb.append((cal.get(Calendar.MONTH)+1));
		    }else{
			    sb.append((cal.get(Calendar.MONTH)+1));
		    }
		    //日
		    if(cal.get(Calendar.DAY_OF_MONTH) < 10){
		    	sb.append("0");
			    sb.append((cal.get(Calendar.DAY_OF_MONTH)));
		    }else{
			    sb.append((cal.get(Calendar.DAY_OF_MONTH)));
		    }
		    //小时
		    if(cal.get(Calendar.HOUR_OF_DAY) < 10){
		    	sb.append("0");
		    	sb.append(cal.get(Calendar.HOUR_OF_DAY));
		    }else{
		    	sb.append(cal.get(Calendar.HOUR_OF_DAY));
		  	}
		    //分钟
		    if(cal.get(Calendar.MINUTE) < 10){
		    	sb.append("0");
		    	sb.append(cal.get(Calendar.MINUTE));
		    }else{
		    	sb.append(cal.get(Calendar.MINUTE));
		    }
		    //秒
		    if(cal.get(Calendar.SECOND) < 10){
		    	sb.append("0");
		    	sb.append(cal.get(Calendar.SECOND));
		    }else{
		    	sb.append(cal.get(Calendar.SECOND));
		    }
		    
		    return sb.toString();
	  }
	  
	  /**
	   * 获取当前日期和时间
	   * @return  String
	   */
	  public static String getDate(){
		    //设置时区为北京时间 GMT+08为东八区的意思，即北京时间所在时区
			TimeZone tz = TimeZone.getTimeZone("GMT+08"); 
			//通过参数tz和时区国家获得Calendar实例
		    Calendar cal = Calendar.getInstance(tz,Locale.CHINA);
		    
		    StringBuffer sb = new StringBuffer();
		    
		    //年份
		    sb.append(cal.get(Calendar.YEAR));
		    //月份
		    if(cal.get(Calendar.MONTH)+1 < 10){
			    sb.append("0");
		    	sb.append((cal.get(Calendar.MONTH)+1));
		    }else{
			    sb.append((cal.get(Calendar.MONTH)+1));
		    }
		    //日
		    if(cal.get(Calendar.DAY_OF_MONTH) < 10){
		    	sb.append("0");
			    sb.append((cal.get(Calendar.DAY_OF_MONTH)));
			    sb.append((char)32);
		    }else{
			    sb.append((cal.get(Calendar.DAY_OF_MONTH)));
			    sb.append((char)32);
		    }
		    //小时
		    if(cal.get(Calendar.HOUR_OF_DAY) < 10){
		    	sb.append("0");
		    	sb.append(cal.get(Calendar.HOUR_OF_DAY));
		    	sb.append(":");
		    }else{
		    	sb.append(cal.get(Calendar.HOUR_OF_DAY));
		    	sb.append(":");
		  	}
		    //分钟
		    if(cal.get(Calendar.MINUTE) < 10){
		    	sb.append("0");
		    	sb.append(cal.get(Calendar.MINUTE));
		    	sb.append(":");
		    }else{
		    	sb.append(cal.get(Calendar.MINUTE));
		    	sb.append(":");
		    }
		    //秒
		    if(cal.get(Calendar.SECOND) < 10){
		    	sb.append("0");
		    	sb.append(cal.get(Calendar.SECOND));
		    }else{
		    	sb.append(cal.get(Calendar.SECOND));
		    }
		    return sb.toString();
	  }
	  
	  /**
	   * 得到当前日期
	   * @param chr为数字间的间隔符，,如2003-08-31中的'-' 
	   * @return String
	   */
	  public static String getDate(String chr) {
	    //设置时区为北京时间
		TimeZone tz = TimeZone.getTimeZone("GMT+08"); 
		//通过参数tz和时区国家获得Calendar实例
	    Calendar cal = Calendar.getInstance(tz,Locale.CHINA);
	    
	    StringBuffer sb = new StringBuffer();
	    
	    //年份
	    sb.append(cal.get(Calendar.YEAR) + chr);
	    //月份
	    if(cal.get(Calendar.MONTH)+1 < 10){
		    sb.append("0");
	    	sb.append((cal.get(Calendar.MONTH)+1));
		    sb.append(chr);
	    }else{
		    sb.append((cal.get(Calendar.MONTH)+1));
		    sb.append(chr);
	    }
	    //日
	    if(cal.get(Calendar.DAY_OF_MONTH) < 10){
	    	sb.append("0");
		    sb.append((cal.get(Calendar.DAY_OF_MONTH)));
	    }else{
		    sb.append((cal.get(Calendar.DAY_OF_MONTH)));
	    }
	    return sb.toString();
	  }
	  
	  /**
	   * 将字符串型数据数组转换为整形对象数据数据数组
	   * @param id 数据类型数组
	   * @return Integer[]
	   */
	  public static Integer[] paraseToInteger(String[] id){
		  Integer[] ids = new Integer[id.length];
		  for(int i=0;i<id.length;i++){
			   ids[i] = Integer.valueOf(id[i]); 
		  }
		  return ids;
	  }
	  
//	  public static String paraseTo
	  
	  public static void main(String[] args){
//		  System.out.println("获取当前时间：" + Util.getDate("-",":"));
//		  System.out.println("获取当前时间：" + Util.getDate("-"));
		  System.out.println("获取当前时间: " + getDate());
		  System.out.println(""+GenerateSequence(0));
		  System.out.println(""+getDate("_"));
//		  String date = Util.getDate();
//		  System.out.println("字符位置:" + date.indexOf((char)32));
//		  System.out.println("组合后的字符串：" + date.substring(0, date.indexOf((char)32)));
//		  String[] str = {"1","2","3","4","5"};
//		  System.out.println("Util.StringArray2String(str):\t"+Util.StringArray2String(str));
//		  System.out.println(Util.getDate("-",":"));
//		  System.out.println(Util.getDate());
//		  System.out.println(Util.getDate("-"));
//		  String str = "12345";
//		  String ch = CommonUtil.addFillSpaceRight(str,20); 
//		  System.out.println("ch:"+ch+"ch.length():"+ch.length());
//		  String headString = "345678901232149874873456789012321498748715800371329         0001                ";
//		  CommonUtil.parseResponseHeadString(headString.getBytes());
//		  for(int i=0;i<test.length;i++){
//			  System.out.println("test["+i+"]:"+test[i]);
//		  }
//		  String abs = CommonUtil.GenerateRandomStr(8);
//		  if(abs.startsWith("0")){
//			  System.out.println("随机数:"+abs+" 开头带0");
//		  }else{
//			  System.out.println("随机数:"+abs+" 正常");
//		  }
//		  String sequence = GenerateSequence(6);
//		  System.out.println(sequence+"\t位数:"+sequence.length());
//		  System.out.println("b:"+CommonUtil.getDate("")+":a");
	  }
   
	   /**
	    * 将字符串数组转化为字符串
	    * @param param
	    * @return
	    */
	   public static String StringArray2String(String[] param){
		   StringBuffer sb = new StringBuffer();
		   for(int i = 0 ;i < param.length;i++){
			   sb.append(param[i]);
			   if(i != param.length-1){
				   sb.append(",");
			   }
		   }
		   return sb.toString();
	   }
	   
	   
		/**
		 * 
		 * 根据长度接收输入流
		 * @param dataLength
		 * @param reader
		 * @return
		 */
		public static byte[] receiveByteArray(int dataLength, DataInputStream reader) {
			byte[] receiveData = new byte[dataLength];
			int tempLength = 0;
			int readLength = 0;
			while (readLength < dataLength) {
				try {
					tempLength = reader.read(receiveData, readLength, dataLength
							- readLength);
					if (tempLength >= 0)
						readLength = readLength + tempLength;
					else
						break;
				} catch (SocketException se) {
					receiveData = null;
					break;
				} catch (IOException se) {
					receiveData = null;
					break;
				} catch (Exception se) {
					receiveData = null;
					break;
				}
			}
			return receiveData;
		}

		/**
		 * int转为字节数组
		 * 
		 * @param i
		 * @return
		 */
		public static byte[] intTobyteArray(int i) {

			byte[] bRet = new byte[4];
			bRet[0] = (byte) ((i >> 24) & 0xff);
			bRet[1] = (byte) ((i >> 16) & 0xff);
			bRet[2] = (byte) ((i >> 8) & 0xff);
			bRet[3] = (byte) (i & 0xff);

			return bRet;
		}

		/**
		 * 字节数组转为int
		 * 
		 * @param bArr
		 * @return
		 */
		public static int byteArrayToint(byte[] bArr) {
			int i = 0;

			i = (((int) bArr[0]) & 0xff) << 24 | (((int) bArr[1]) & 0xff) << 16
					| (((int) bArr[2]) & 0xff) << 8 | (((int) bArr[3]) & 0xff);

			return i;
		}	
		
		public static String addFillSpaceRight(String str, int length) {
			
			if ( str == null )
				str = "";
			
			byte[] fillData = new byte[length];
			byte[] data = str.getBytes();
			
			int offset = data == null? 0: data.length;
			
			if(data != null) System.arraycopy(data, 0, fillData, 0, data.length);
			
			for(int i=offset; i<length; i++){
				fillData[i] = 0x20;
			}
			
			return new String(fillData);
		}
		public static String test(String test,int length){
			StringBuffer re = new StringBuffer();
			if(test.length() < length){
				for(int i=0;i<length-test.length();i++){
					re.append((char)43);
				}
				return re.toString()+test;
			}else{
				return test;
			}
		}
		
	    /**
	     * 根据随机种子生成指定位数的随机数
	     * @param seed
	     * @param num
	     * @return
	     */
		public static String GenerateRandomStr(int num) {
		    StringBuffer generateRandStr=new StringBuffer();
		    Random rand = new Random();
		    for (int i = 0; i < num; i++) {
		         int randNum = rand.nextInt(9);
		         generateRandStr.append(randNum);
		    }
		    return generateRandStr.toString();
		}
		
		/**
		 * 生成指定格式的序列号 格式为:14位时间日期  num为随机数
		 * @param num
		 * @return
		 */
		public static String GenerateSequence(int num){
			StringBuffer sb = new StringBuffer(getDateTimeString());
		    Random rand = new Random();
		    for (int i = 0; i < num; i++) {
		         int randNum = rand.nextInt(9);
		         sb.append(randNum);
		    }
			return sb.toString();
		}
		
		public static byte[][] parseResponseHeadString(byte[] firstData){
			byte[][] result = new byte[4][];
			byte[] serviceCode = new byte[4];
			System.arraycopy(firstData, 0, serviceCode, 0, 4);
			byte[] resultCode = new byte[4];
			System.arraycopy(firstData, 4, resultCode, 0, 4);
			byte[] phoneNum = new byte[20];
			System.arraycopy(firstData, 8, phoneNum, 0, 20);
			byte[] sequenceNum = new byte[20];
			System.arraycopy(firstData, 28, sequenceNum, 0, 20);
			
			result[0] = serviceCode;
			result[1] = resultCode;
			result[2] = phoneNum;
			result[3] = sequenceNum;
			
			return result;
		}
		
		
		/**
		 * @return
		 */
		public static String getInfo() {

			/** My Physiognomy : 0001, Love Index : 0002, My Junior : 0003 */
			String serviceCode = "                0001";
			/** Default : 0000 */
			String resultCode = "                0000";
			/** User Phone Number */
			String phoneNumber = "         15900455454";
			/** Sequence Number */
			String seqNumber = "                   1";

			/** dummy data */
			byte[] reserved = new byte[256];

			String bizCommonHeader = serviceCode + resultCode + phoneNumber
					+ seqNumber + new String(reserved);

			return bizCommonHeader;
		}
		
		/**
		 * 从指定的原文件中读取数据并转为字节数组 
		 * @param src 源文件路径
		 * @return byte[]
		 * @throws IOException
		 */
		public static byte[] inputStream(String src) throws IOException{
			FileInputStream fi = new FileInputStream(new File(src));
			byte[] imgByte = new byte[fi.available()];
			fi.read(imgByte);
			fi.close();
			return imgByte;
		}
		/**
		 * 复制文件到指定目录
		 * @param src
		 * @param dst
		 * @param BUFFER_SIZE
		 */
		public static void copy(File src, File dst,int BUFFER_SIZE)  {   
	        try  {   
	           InputStream in = null ;   
	           OutputStream out = null ;   
	            try  {                   
	               in = new BufferedInputStream( new FileInputStream(src), BUFFER_SIZE);  
	               if(!dst.exists()){
	            	   dst.getParentFile().mkdirs();
	               }
	               out = new BufferedOutputStream( new FileOutputStream(dst), BUFFER_SIZE);   
	                byte [] buffer = new byte [in.available()];   
	                while ( in.read(buffer) > 0 )  {   
	                   out.write(buffer,0,buffer.length);   
	               }    
	            } finally  {   
	                if ( null != in)  {   
	                   in.close();   
	               }    
	                 if ( null != out)  {   
	                   out.close();   
	               }    
	           }    
	        } catch (Exception e)  {   
	           e.printStackTrace();   
	       }    
	   }
}
