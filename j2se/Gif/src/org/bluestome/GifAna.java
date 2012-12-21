package org.bluestome;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

import org.bluestome.decoder.GifDecoder;
import org.bluestome.encoder.AnimatedGifEncoder;
import org.bluestome.utils.FileUtils;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class GifAna {

	/**
	 * 把多张jpg图片 合成一张
	 * 
	 * @param pic
	 *            String[] 多个jpg文件名 包含路径
	 * @param newPic
	 *            String 生成的gif文件名 包含路径
	 */
	public synchronized void jpgToGif(String pic[], String newPic) {
		try {
			AnimatedGifEncoder e = new AnimatedGifEncoder(); // 网上可以找到此类
			e.setRepeat(0);
			e.start(newPic);
			BufferedImage src[] = new BufferedImage[pic.length];
			for (int i = 0; i < src.length; i++) {
				e.setDelay(200); // 设置播放的延迟时间，默认是200ms
				src[i] = ImageIO.read(new File(pic[i])); // 读入需要播放的jpg文件
				e.addFrame(src[i]); // 添加到帧中
			}
			e.finish();
		} catch (Exception e) {
			System.out.println("jpgToGif Failed:");
			e.printStackTrace();
		}
	}

	/**
	 * 把多张jpg图片 合成一张
	 * 
	 * @param pic
	 *            String[] 多个jpg文件名 包含路径
	 * @param newPic
	 *            String 生成的gif文件名 包含路径
	 * @param delay
	 *            gif每帧之间的延时时间
	 */
	public synchronized void jpgToGif(String pic[], String newPic, int delay) {
		try {
			AnimatedGifEncoder e = new AnimatedGifEncoder(); // 网上可以找到此类
			e.setRepeat(0);
			e.start(newPic);
			BufferedImage src[] = new BufferedImage[pic.length];
			for (int i = 0; i < src.length; i++) {
				e.setDelay(delay); // 设置播放的延迟时间
				long s = System.currentTimeMillis();
				src[i] = ImageIO.read(new File(pic[i])); // 读入需要播放的jpg文件
				System.out.println("\t 设置图片到输入流耗时:"
						+ (System.currentTimeMillis() - s));
				e.addFrame(src[i]); // 添加到帧中
			}
			e.finish();
		} catch (Exception e) {
			System.out.println("jpgToGif Failed:");
			e.printStackTrace();
		}
	}

	/**
	 * 把gif图片 按帧拆分成jpg图片
	 * 
	 * @param gifName
	 *            String 小gif图片 (路径+名称)
	 * @param path
	 *            String 生成小jpg图片 的路径
	 * @return String[] 返回生成小jpg图片 的名称
	 */
	private synchronized String[] splitGif(String gifName, String path) {
		try {
			GifDecoder decoder = new GifDecoder();
			decoder.read(gifName);
			int n = decoder.getFrameCount(); // 得到frame的个数
			String[] subPic = new String[n];
			File tmp = null;
			for (int i = 0; i < n; i++) {
				BufferedImage frame = decoder.getFrame(i); // 得到帧
				// 生成小的JPG文件
				subPic[i] = path + String.valueOf(i) + ".jpg";
				tmp = new File(subPic[i]);
				if (!tmp.getParentFile().exists()) {
					tmp.getParentFile().mkdirs();
				}
				FileOutputStream out = new FileOutputStream(tmp);
				ImageIO.write(frame, "jpeg", out);
				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
				encoder.encode(frame); // 存盘
				out.flush();
				out.close();
			}
			return subPic;
		} catch (Exception e) {
			System.out.println("splitGif Failed!");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据提供的文字生成jpg图片
	 * 
	 * @param s
	 *            String 文字
	 * @param smallWidth
	 *            int 每个字的宽度和高度是一样的
	 * @param bgcolor
	 *            Color 背景色
	 * @param fontcolor
	 *            Color 字色
	 * @param fontPath
	 *            String 字体文件
	 * @param jpgname
	 *            String jpg图片 名
	 * @return
	 */
	// private String createImageByFont(String s, int smallWidth, Color bgcolor,
	// Color fontcolor, String fontPath, String jpgname) {
	// try { // 宽度 高度
	// BufferedImage bimage = new BufferedImage(s.length() * smallWidth,
	// smallWidth, BufferedImage.TYPE_INT_RGB);
	// Graphics2D g = bimage.createGraphics();
	// g.setColor(bgcolor); // 背景色
	// g.fillRect(0, 0, smallWidth, smallWidth); // 画一个矩形
	// g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	// RenderingHints.VALUE_ANTIALIAS_ON); // 去除锯齿(当设置的字体过大的时候,会出现锯齿)
	// g.setColor(fontcolor); // 字的颜色
	// File file = new File(fontPath); // 字体文件
	// Font font = Font.createFont(Font.TRUETYPE_FONT, file); //
	// 根据字体文件所在位置,创建新的字体对象(此语句在jdk1.5下面才支持)
	// g.setFont(font.deriveFont((float) smallWidth)); // font.deriveFont(float
	// // f)复制当前 Font
	// // 对象并应用新设置字体的大小
	// g.drawString(s, 0, smallWidth); // 在指定坐标除添加文字
	// g.dispose();
	// FileOutputStream out = new FileOutputStream(jpgname); // 指定输出文件
	// JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
	// JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bimage);
	// param.setQuality(50f, true);
	// encoder.encode(bimage, param); // 存盘
	// out.flush();
	// out.close();
	// } catch (Exception e) {
	// System.out.println("createJpgByFont Failed!");
	// e.printStackTrace();
	// }
	// }

	/**
	 * 将多个小图片 合成一张大jpg图 (小的jpg图片 按照行列顺序平铺)
	 * 
	 * @param smallJPG
	 *            ArrayList 一组小的jpg图片
	 * @param bigWidth
	 *            int 大图宽度
	 * @param smallWidth
	 *            int 单个文字生成的小图的宽度和高度是一致的
	 * @return
	 */
	// private void createBigJPG(ArrayList smallJPG, int bigWidth, int
	// smallHeigh,
	// Color bgColor, String picName) {
	// try {
	// if (bigWidth < smallWidth) // 如果大图片 的高度比小图片 的高度还小 直接返回
	// return;
	// int colCount = bigWidth / smallWidth; // 每行放置的字数
	// int leftMargin = (int) ((bigWidth - colCount * smallWidth) / 2f); // 左边距
	// int rowCount = smallJPG.size(); // 小图行数
	// int setWidth = bigWidth; // 每列中间不留空隙，只留左右边距
	// int setHeight = smallWidth * rowCount;
	// // 按照大图片 宽高绘制一个背景图片
	// BufferedImage bufImage = new BufferedImage(setWidth, setHeight,
	// BufferedImage.TYPE_INT_RGB);
	// Graphics2D g = bufImage.createGraphics();
	// g.setColor(bgColor); // 背景的颜色
	// g.fillRect(0, 0, setWidth, setHeight);
	// int y = 0; // 纵坐标
	// for (int i = 0; i < rowCount; i++) { // 遍历每行
	// ArrayList col = (ArrayList) (smallJPG.get(i));
	// int x = leftMargin; // 横坐标 可能会出现左边距
	// for (int j = 0; j < col.size(); j++) {
	// String jpgname = (String) (col.get(j));
	// ImageIcon icon = new ImageIcon(jpgname);
	// Image img = icon.getImage();
	// int imgWidth = img.getHeight(null);
	// g.drawImage(img, x, y, null);
	// x += imgWidth;
	// }
	// y += (smallWidth);
	// }
	// g.dispose();
	// FileOutputStream out = new FileOutputStream(picName); // 指定输出文件
	// JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out); // 设置文件格式
	// JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bufImage); //
	// 从图片
	// // 缓冲中读取
	// param.setQuality(50f, true);
	// encoder.encode(bufImage, param); // 存盘
	// out.flush();
	// out.close();
	// } catch (Exception e) {
	// System.out.println("createBigJPG Failed!");
	// e.printStackTrace();
	// }
	// }

	public static void main(String args[]) {
		String gifSrc = "d:\\tmp\\123.gif";
		GifAna gif = new GifAna();
		// gif.splitGif(gifSrc, "d:\\tmp\\gif\\");

		File file = null;
		try {
			file = new File("d:\\tmp\\gif\\");
			String[] srcs = new String[file.list().length];
			File[] fils = file.listFiles();
			FileUtils.sortFilesByFileName(fils);
			for (int i = 0; i < fils.length; i++) {
				File f = fils[i];
				srcs[i] = f.getAbsolutePath();
				System.out.println(f.getAbsolutePath());
			}
			long s = System.currentTimeMillis();
			gif.jpgToGif(srcs, "d:\\tmp\\" + System.currentTimeMillis()
					+ ".gif", 60);
			System.out
					.println("\tzhang:耗时:" + (System.currentTimeMillis() - s));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
