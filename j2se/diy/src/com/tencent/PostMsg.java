package com.tencent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.chinamilitary.util.IOUtil;
import com.sina.json.WeiboRespJson;
import com.utils.JSONUtils;

public class PostMsg {
	
	static String FILE_PATH = "D:\\verycd.txt";
	static String contents = IOUtil.readFile(FILE_PATH, "GBK", "txt");
	static BlockingQueue<byte[]> byteQuene = new LinkedBlockingQueue<byte[]>(100);
	static final ScheduledExecutorService pool = Executors.newSingleThreadScheduledExecutor();
	
	public static void main(String args[]) {
		PostMsg post = new PostMsg();
		int index = 262;
		post.doReply("我是俗ren- 戒 /On My Ｗay| 等待宝宝出生..... 持续改进中... [9]");
//		do{
//			String ctx = post.getWord(index);
//			System.out.println(index+"|"+ctx);
//			if (null != ctx) {
//				post.doReply(ctx);
//			}
//			System.err.println("end of block");
//			try {
//				byteQuene.poll(30*60 * 1000L, TimeUnit.MILLISECONDS);
//			} catch (InterruptedException e) {
//			} finally{
//				index++;
//			}
//		}while(index<302);
	}
	
	private String getWord(int index){
		String[] cl = contents.split("\r\n");
		int len = cl.length;
		if(index < len){
			return cl[index];
		}
		return null;
	}

	/**
	 * 自动回复消息网络代码
	 */
	private synchronized void doReply(String content){
		URL url = null;
		HttpURLConnection connection = null;
		OutputStream out = null;
		InputStream in = null;
		ByteArrayOutputStream byteArray = null;
		StringBuffer sb = new StringBuffer();
		try{
			url = new URL("http://taotao.qq.com/cgi-bin/emotion_cgi_publish_v6?g_tk=108680217");
			connection.addRequestProperty("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
			connection.addRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
			connection.addRequestProperty("Accept", "*/*");
			connection.addRequestProperty("Referer", "http://262372042.qzone.qq.com/");
			connection.addRequestProperty("Cache-Control", "max-age=0");
			connection.addRequestProperty("Connection", "keep-alive");
			connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11");
			connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.addRequestProperty("Cookie", "pgv_pvid=1999216512; o_cookie=262372042; RK=Lp5KVG4XyC; __Q_w_s_hat_seed=1; randomSeed=931973; login_time=1D3771B843EA3AD0E9942F2C8DEEA2391D1960E359069B3E; pgv_info=ssid=s583678480; qqmusic_uin=34567890; qqmusic_key=34567890; qqmusic_fromtag=6; qz_screen=1366x768; pt2gguin=o0262372042; uin=o0262372042; skey=@UFQFtPBTH; ptisp=ctc; show_id=; fnc=2");
			connection.setConnectTimeout(15*1000);
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.connect();
			
			sb.append(URLEncoder.encode("con", "UTF-8")+"="+URLEncoder.encode(content, "UTF-8"));
			sb.append("&");
			sb.append(URLEncoder.encode("feedversion", "UTF-8")+"="+URLEncoder.encode("1", "UTF-8"));
			sb.append("&");
			sb.append(URLEncoder.encode("ver", "UTF-8")+"="+URLEncoder.encode("1", "UTF-8"));
			sb.append("&");
			sb.append(URLEncoder.encode("hostuin", "UTF-8")+"="+URLEncoder.encode("262372042", "UTF-8"));
			sb.append("&");
			sb.append(URLEncoder.encode("qzreferrer", "UTF-8")+"="+URLEncoder.encode("http://262372042.qzone.qq.com/", "UTF-8"));
			sb.append("&");
			sb.append(URLEncoder.encode("richtype", "UTF-8")+"="+URLEncoder.encode("", "UTF-8"));
			sb.append("&");
			sb.append(URLEncoder.encode("richval", "UTF-8")+"="+URLEncoder.encode("", "UTF-8"));
			sb.append("&");
			sb.append(URLEncoder.encode("special_url", "UTF-8")+"="+URLEncoder.encode("", "UTF-8"));
			sb.append("&");
			sb.append(URLEncoder.encode("subrichtype", "UTF-8")+"="+URLEncoder.encode("", "UTF-8"));
			sb.append("&");
			sb.append(URLEncoder.encode("who", "UTF-8")+"="+URLEncoder.encode("1", "UTF-8"));
			
			String requestBody = sb.toString();
			out = connection.getOutputStream();
			out.write(requestBody.getBytes());
			out.flush();
			out.close();
			
			int code = connection.getResponseCode();
			switch(code){
				case 200:
					String filed = connection.getHeaderField("Content-Encoding");
					in = connection.getInputStream();
					byteArray = new ByteArrayOutputStream();
					int ch;
					while((ch = in.read()) != -1){
						byteArray.write(ch);
					}
					byteArray.flush();
					String result = null;
					if(filed.equals("gzip")){
						System.out.println("do gzip");
						//TODO 使用GZIP进行解码
						ByteArrayInputStream bai = new ByteArrayInputStream(byteArray.toByteArray());
						GZIPInputStream gzipin = new GZIPInputStream(bai);
						byte[] body = new byte[1024];
						int p = gzipin.read(body);
						result = new String(body,0,p);
					}else{
						System.out.println("do normal");
						result = byteArray.toString();
					}
					break;
				default:
					System.err.println(connection.getResponseCode()+":"+connection.getResponseMessage());
					break;
			}
			
			if(null != out){
				out.close();
			}
			if(null != in){
				in.close();
			}
		}catch(Exception e){
			if(null != byteArray){
				try {
					byteArray.close();
				} catch (IOException e1) {
					System.err.println(e1);
				}
			}
			
			if(null != connection){
				connection.disconnect();
				connection = null;
			}
			
			if(null != url){
				url = null;
			}
			sb = null;
		}
	}

	/**
	 * 将json转换为对象
	 * @param content
	 * @return
	 */
	WeiboRespJson getJson(String content){
		WeiboRespJson resp = (WeiboRespJson)JSONUtils.json2Object(content, WeiboRespJson.class);
		/**
		 * code:100000 [成功]
		 *      100001 [失败]
		 */
		return resp;
	}
}

/**
 * 播放音频文件
 * @author Bluestome.Zhang
 *
 */
class MediaPlayCase{
	static void play() {
		new Thread(new Runnable(){
			public void run() {
				try {
					// From file
					AudioInputStream stream = AudioSystem
							.getAudioInputStream(new File(
									"D:\\projects\\sky2.0\\qunke2\\diy_page\\src\\com\\autohome\\notify.wav"));

					// From URL
					// stream = AudioSystem.getAudioInputStream(new URL(
					// "http://hostname/audiofile"));

					// At present, ALAW and ULAW encodings must be converted
					// to PCM_SIGNED before it can be played
					AudioFormat format = stream.getFormat();
					if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
						format = new AudioFormat(
								AudioFormat.Encoding.PCM_SIGNED, format
										.getSampleRate(), format
										.getSampleSizeInBits() * 2, format
										.getChannels(),
								format.getFrameSize() * 2, format
										.getFrameRate(), true); // big endian
						stream = AudioSystem
								.getAudioInputStream(format, stream);
					}

					// Create the clip
					DataLine.Info info = new DataLine.Info(Clip.class, stream
							.getFormat(),
							((int) stream.getFrameLength() * format
									.getFrameSize()));
					Clip clip = (Clip) AudioSystem.getLine(info);

					// This method does not return until the audio file is
					// completely loaded
					clip.open(stream);

					// Start playing
					clip.start();
					
					Thread.sleep(2*1000L);
					
					clip.stop();
					clip = null;
				} catch (MalformedURLException e) {
					System.err.println(e);
				} catch (IOException e) {
					System.err.println(e);
				} catch (LineUnavailableException e) {
					System.err.println(e);
				} catch (UnsupportedAudioFileException e) {
					System.err.println(e);
				} catch (Exception e){
					System.err.println(e);
				}
			}
		}).start();
	}
}
