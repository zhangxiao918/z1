package com.sina.weibo;

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
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.chinamilitary.util.IOUtil;
import com.sina.json.WeiboRespJson;
import com.utils.ByteUtils;
import com.utils.JSONUtils;

public class PostMsg {
	
	static String FILE_PATH = "D:\\verycd.txt";
	static String contents = IOUtil.readFile(FILE_PATH, "GBK", "txt");
	static BlockingQueue<byte[]> byteQuene = new LinkedBlockingQueue<byte[]>(100);
	static final ScheduledExecutorService pool = Executors.newSingleThreadScheduledExecutor();
	
	public static void main(String args[]) {
		PostMsg post = new PostMsg();
		int index = 200;
		do{
			String ctx = post.getWord(index);
			System.out.println(index+"|"+ctx);
			if (null != ctx) {
				post.doReply(ctx);
			}
			System.err.println("end of block");
			try {
				byteQuene.poll(30*60 * 1000L, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
			} finally{
				index++;
			}
		}while(index<302);
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
			url = new URL("http://weibo.com/aj/mblog/add?__rnd="+System.currentTimeMillis());
			connection = (HttpURLConnection)url.openConnection();
			connection.addRequestProperty("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
			connection.addRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
			connection.addRequestProperty("Accept", "*/*");
			connection.addRequestProperty("Origin", "http://weibo.com");
			connection.addRequestProperty("Referer", "http://weibo.com/u/2113646754");
			connection.addRequestProperty("Cookie", "ASP.NET_SessionId=i5rklwmtposzhh55egotym55; isLoginedWeb=T");
			connection.addRequestProperty("Connection", "keep-alive");
			connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11");
			connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.addRequestProperty("Cookie", "UOR=ruyi.etao.com,widget.weibo.com,; lzstat_uv=4543329613709587116|2893156; wvr=4; un=zhangxiao918@21cn.com; USRHAWB=usrmdins212_219; SUE=es%3D281458f1ee2ba227b5e46ecb917f1ec2%26ev%3Dv1%26es2%3D61bacb8bb83bb1bd5fd837f32794f78f%26rs0%3DhQ1PZdeqKERZ1yoh%252BaTxhk%252BpX9E77XeEZufQlvVHXa1x72PJ%252FNXnYUlievYXRXkXP3%252BbDpePKH3RWsmLBdim0kFRHbLyS1fVE7iGtmSXc4XKm9Nsezk6fBkhp%252BSp3k5XlwaNI0cLUQIh4Zz%252FECNnJDoAYkKk%252FH1eqjL3X4DPjg4%253D%26rv%3D0; SUP=cv%3D1%26bt%3D1342108850%26et%3D1342195250%26d%3Dc909%26i%3D00c4%26us%3D1%26vf%3D0%26vt%3D0%26ac%3D2%26uid%3D2113646754%26user%3Dzhangxiao918%254021cn.com%26ag%3D4%26name%3Dzhangxiao918%254021cn.com%26nick%3D%25E6%259B%25BE%25E7%25BB%258F%25E7%259A%2584%25E6%25B5%2581%25E6%2598%259F%25E9%259B%25A8%26fmp%3D%26lcp%3D; SUS=SID-2113646754-1342108850-JA-xhqlt-9ec79bbde6e588a6a08002f3bbbc72ee; ALF=1342611941; SSOLoginState=1342108850; SinaRot/u/2113646754%3Fwvr%3D3.6%26lf%3Dreg=65; _s_tentry=login.sina.com.cn; Apache=9095031078904.867.1342108856778; ULV=1342108856787:156:13:5:9095031078904.867.1342108856778:1342007108794; ads_ck=1; _olympicMedalsTable=1341933000; SINAGLOBAL=9095031078904.867.1342108856778");
			connection.setConnectTimeout(15*1000);
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.connect();
			
			sb.append(URLEncoder.encode("text", "UTF-8")+"="+URLEncoder.encode(content, "UTF-8"));
			sb.append("&");
			sb.append(URLEncoder.encode("pic_id", "UTF-8")+"="+URLEncoder.encode("", "UTF-8"));
			sb.append("&");
			sb.append(URLEncoder.encode("rank", "UTF-8")+"="+URLEncoder.encode("", "UTF-8"));
			sb.append("&");
			sb.append(URLEncoder.encode("location", "UTF-8")+"="+URLEncoder.encode("home", "UTF-8"));
			sb.append("&");
			sb.append(URLEncoder.encode("module", "UTF-8")+"="+URLEncoder.encode("stissue", "UTF-8"));
			sb.append("&");
			sb.append(URLEncoder.encode("_t", "UTF-8")+"="+URLEncoder.encode("0", "UTF-8"));
			
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
					WeiboRespJson resp = getJson(result);
					if(null != resp){
						if(resp.getCode().equals("100000")){
							MediaPlayCase.play();
							System.out.println("提示:["+resp.getCode()+"]"+resp.getMsg());
						}else if(resp.getCode().equals("100001")){
							System.err.println("提示:["+resp.getCode()+"]"+resp.getMsg());
						}else{
							System.err.println("提示:未知类型");
						}
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
