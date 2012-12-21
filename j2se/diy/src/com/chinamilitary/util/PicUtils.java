package com.chinamilitary.util;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;

import com.chinamilitary.bean.PicsModel;

public class PicUtils {

	public static void main(String[] args) {
//		PicsModel model = getFileAttributes("E:/hekai/hk0001.jpg");
//		System.out.println("picextendname is:" + model.getpicsExtendName());
//		System.out.println("picwidth is:" + model.getpicsWidth());
//		System.out.println("picheight is:" + model.getpicsHeight());
//		System.out.println("piccolor:" + model.getpicsColor());
//		System.out.println("picsize:" + model.getpicsSize());
		
		String url = "http://image6.tuku.cn/pic/ziranfengjing/jufufengjing/001.jpg";
//		String fileName = getFileNameFromWeb(url);
//		System.out.println("fileName:"+fileName);
//		System.out.println("临时文件夹:"+System.getProperty("java.io.tmpdir"));
		
//		Iterator it = System.getProperties().keySet().iterator();
//		while(it.hasNext()){
//			System.out.println(it.next());
//		}
		
//		File _file = new File("F:/china/military/20100818/30338/002.jpg");
//		try{
//			BufferedImage src = javax.imageio.ImageIO.read(_file);
//			String[] names = src.getPropertyNames();
//			if(null != names){
//				for(String name:names){
//					System.out.println("name:"+name+""+src.getProperty(name));
//				}
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
		String fileName = getFileNameFromWeb(url);
		if(null != fileName){
			File file = createPicFile(url, fileName);
			if(null != file){
				Map<String, Long> img = getImgInfo(file.getAbsolutePath());
				if(null != img){
					for (Map.Entry<String, Long> entry : img.entrySet()) { 
	                    System.out.println(entry.getKey() + " " + entry.getValue()); 
					} 
				}
			}
		}

	}

	 /** 
     * 计算图片尺寸大小等信息：w宽、h高、s大小。异常时返回null。 
     * 
     * @param imgpath 图片路径 
     * @return 图片信息map 
     */ 
    public static Map<String, Long> getImgInfo(String imgpath) { 
            Map<String, Long> map = new HashMap<String, Long>(3); 
            File imgfile = new File(imgpath); 
            try { 
                    FileInputStream fis = new FileInputStream(imgfile); 
                    BufferedImage buff = ImageIO.read(imgfile); 
                    map.put("w", buff.getWidth() * 1L); 
                    map.put("h", buff.getHeight() * 1L); 
                    map.put("s", imgfile.length()); 
                    fis.close(); 
            } catch (FileNotFoundException e) { 
                    System.err.println("所给的图片文件" + imgfile.getPath() + "不存在！计算图片尺寸大小信息失败！"); 
                    map = null; 
            } catch (IOException e) { 
                    System.err.println("计算图片" + imgfile.getPath() + "尺寸大小信息失败！"); 
                    map = null; 
            } 
            return map; 
    } 
    
    private static PicsModel getFileAttributes(String picpath) {
		PicsModel model = null;
		String picextendname;
		picextendname = null;
		byte[] content = null;
		try {
			content = readFromFile(picpath);
			int k;
			k = content.length;
			// 不想处理的话，请直接获取其字节数
			Integer kk;
			kk = null;
			String picsize;
			picsize = null;
			if (k >= 1024) {
				// bigger than fact pic file sizes
				k = k / 1024 + 1;
				kk = new Integer(k);
				picsize = kk.toString() + "K";
			} else if (k > 0) {
				kk = new Integer(k);
				picsize = kk.toString();
			}
			model = new PicsModel();
			model.setpicsSize(picsize);
		} catch (IOException e) {
			content = new byte[0];
			e.printStackTrace();
		}
		picextendname = getFileExtendName(content);
		int picwidth, picheight, color;
		String piccolor;
		picwidth = 0;
		picheight = 0;
		color = 0;
		piccolor = null;

		if (picextendname.equals("GIF")) {
			// picwidth position
			picwidth = getFileAttribute(content, 7, 2, picextendname);
			// picheight position
			picheight = getFileAttribute(content, 9, 2, picextendname);
			// piccolor position
			color = getFileAttribute(content, 10, 1, picextendname);
			color = color % 8 + 1;
			piccolor = getPicColor(color);
		}
		if (picextendname.equals("JPG")) {
			// 考虑了两种情况
			picwidth = getFileAttribute(content, 166, 2, picextendname);
			picheight = getFileAttribute(content, 164, 2, picextendname);
			color = getFileAttribute(content, 167, 1, picextendname);
			color = color * 8;
			if ((picwidth == 0) || (picheight == 0) || (color > 3)) {
				picwidth = getFileAttribute(content, 197, 2, picextendname);
				picheight = getFileAttribute(content, 195, 2, picextendname);
				color = getFileAttribute(content, 198, 1, picextendname);
				color = color * 8;
			}
			piccolor = getPicColor(color);
		}
		if (picextendname.equals("BMP")) {
			picwidth = getFileAttribute(content, 19, 2, picextendname);
			picheight = getFileAttribute(content, 23, 2, picextendname);
			color = getFileAttribute(content, 28, 1, picextendname);
		}
		if (picextendname.equals("PNG")) {
			picwidth = getFileAttribute(content, 19, 2, picextendname);
			picheight = getFileAttribute(content, 23, 2, picextendname);
			// usually is "16M"??
			piccolor = "16M";
		}
		model.setpicsExtendName(picextendname);
		model.setpicsWidth(picwidth);
		model.setpicsHeight(picheight);
		model.setpicsColor(piccolor);
		return model;
	}

	private static byte[] readFromFile(String fileName) throws IOException {
		PicsModel model = null;
		FileInputStream fin = new FileInputStream(fileName);
		byte[] buf = new byte[fin.available()];
		fin.read(buf);
		fin.close();
		return buf;
	}

	/**
	 * 从网站中获取数据
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	private static byte[] readFromWeb(String url) throws IOException {
		String fileName = getFileNameFromWeb(url);
		byte[] buf = null;
		if(null != fileName){
			File file = createPicFile(url, fileName);
			if(null != file){
				buf = readFromFile(file.getAbsolutePath());
			}
		}
		return buf;
	}

	/**
	 * 复制文件到指定目录
	 * 
	 * @param src
	 * @param dst
	 * @param BUFFER_SIZE
	 */
	private static File createPicFile(String url, String fileName) {
		InputStream in = null;
		OutputStream out = null;
		URL urls = null;
		File file = null;
		try {
			try {
				urls = new URL(url);
				in = urls.openStream();
				file = new File(System.getProperty("java.io.tmpdir")+File.separator+fileName);
				if (!file.exists()) {
					file.getParentFile().mkdirs();
				}
				out = new BufferedOutputStream(new FileOutputStream(file), 1024);
				byte[] buffer = new byte[1024];
				int bytesRead = 0;
				while ((bytesRead = in.read(buffer, 0, 1024)) != -1) {
					out.write(buffer, 0, bytesRead);
				}
			} finally {
				if (null != in) {
					in.close();
				}
				if (null != out) {
					out.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}

	/**
	 *  从网址中获取文件名
	 */
	public static String getFileNameFromWeb(String url){
		if(url.startsWith("http://") && url.indexOf(".") > -1){
			int start = url.lastIndexOf("/");
			String tmp = url.substring(start+1,url.length());
			return tmp;
		}
		return null;
	}
	
	private static String getFileExtendName(byte[] byte1) {
		String strFileExtendName;
		strFileExtendName = null;

		// header bytes contains GIF87a or GIF89a?
		if ((byte1[0] == 71) && (byte1[1] == 73) && (byte1[2] == 70)
				&& (byte1[3] == 56) && ((byte1[4] == 55) || (byte1[4] == 57))
				&& (byte1[5] == 97)) {
			strFileExtendName = "GIF";
		}
		// header bytes contains JFIF?
		if ((byte1[6] == 74) && (byte1[7] == 70) && (byte1[8] == 73)
				&& (byte1[9] == 70)) {
			strFileExtendName = "JPG";
		}
		// header bytes contains BM?
		if ((byte1[0] == 66) && (byte1[1] == 77)) {
			strFileExtendName = "BMP";
		}
		// header bytes contains PNG?
		if ((byte1[1] == 80) && (byte1[2] == 78) && (byte1[3] == 71)) {
			strFileExtendName = "PNG";
		}
		return strFileExtendName;
	}

	private static int getFileAttribute(byte[] byte2, int n, int m,
			String fileextendname) {
		int j, FileAttributeValue;
		j = 0;
		FileAttributeValue = 0;
		String str, str1;
		str = "";
		str1 = "";

		// 如果其大于127，则反之出现少于0，需要进行＋256运算
		for (int k = 0; k < m; k++) {
			if (byte2[n - k] < 0) {
				j = byte2[n - k];
				j = j + 256;
			} else {
				j = byte2[n - k];
			}

			str1 = Integer.toHexString(j);
			// 转化为16进制，不足位补0
			if (str1.length() < 2) {
				str1 = "0" + str1;
			}

			// 格式的不同，表达属性的字节也有变化
			if (fileextendname.equalsIgnoreCase("JPG")
					|| fileextendname.equalsIgnoreCase("PNG")) {
				str = str1 + str;
			} else {
				str = str + str1;
			}
		}
		FileAttributeValue = HexToDec(str);
		return FileAttributeValue;
	}

	private static int HexToDec(String cadhex) {
		int n, i, j, k, decimal;
		String CADHEX1;
		n = 0;
		i = 0;
		j = 0;
		k = 0;
		decimal = 0;
		CADHEX1 = null;
		n = cadhex.length();
		CADHEX1 = cadhex.trim().toUpperCase();

		while (i < n) {
			j = CADHEX1.charAt(i);
			if ((j >= 48) && (j < 65)) {
				j = j - 48;
			}
			if (j >= 65) {
				j = j - 55;
			}
			i = i + 1;

			// 16幂运算
			k = 1;
			for (int m = 0; m < (n - i); m++) {
				k = 16 * k;
			}
			decimal = j * k + decimal;
		}

		return decimal;
	}

	private static String getPicColor(int color) {
		int k;
		k = 1;
		String piccolor;
		piccolor = null;
		// 2幂运算
		for (int m = 0; m < color; m++) {
			k = 2 * k;
		}

		Integer kk;
		kk = null;
		if (k >= 1048576) {
			k = k / 1048576;
			kk = new Integer(k);
			piccolor = kk.toString() + "M";
		} else if (k >= 1024) {
			k = k / 1024;
			kk = new Integer(k);
			piccolor = kk.toString() + "K";
		} else if (k > 0) {
			kk = new Integer(k);
			piccolor = kk.toString();
		}
		return piccolor;
	}

}
