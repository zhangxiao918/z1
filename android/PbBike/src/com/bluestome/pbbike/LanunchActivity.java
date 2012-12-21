package com.bluestome.pbbike;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * @ClassName: LanunchActivity
 * @Description: TODO
 * @author Bluestome.Zhang
 * @date 2012-8-9 下午09:16:40
 */
public class LanunchActivity extends Activity{

    private final String DEST_URL = "http://zjicity.busditu.com";
    
    private static final int MSG_NETWORK_CODE = 0x1000;
    
    private static final int MSG_PROGRESS_DO = 0x2000;
    
    private static final int MSG_PROGRESS_CYCLE = 0x2001;
    
    private static final int MSG_PROGRESS_STOP = 0x2002;
    
    private TextView textView = null;
    
    private ProgressBar progressBar = null;
    
    private int iCount = 0;
    
    private boolean isFinish = false;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher);
        progressBar = (ProgressBar)findViewById(R.id.progress_bar_id);
        progressBar.setVisibility(View.VISIBLE); 
        progressBar.setMax(100);
        progressBar.setProgress(0);
        //处理进度条
        new Thread(progressThread).start();
        //检查服务器网络
        new Thread(mCheckNetwork).start();
        //连接到自己的服务器
        new Thread(connServer).start();
        
    }
    
    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case  MSG_NETWORK_CODE:
                    if(null != msg && null != msg.obj){
                        textView = (TextView)findViewById(R.id.textView1);
                        textView.setText((String)msg.obj);
                    }
                    break;
                case MSG_PROGRESS_DO:
                	progressBar.setProgress(iCount);  
                	break;
                case MSG_PROGRESS_CYCLE:
                	progressBar.setProgress(iCount);  
                	break;
                case MSG_PROGRESS_STOP:
                	progressBar.setVisibility(View.GONE);  
                	break;
            }
        }
        
    };
    
    /**
     *  处理进度条线程
     */
    private Runnable progressThread = new Runnable(){
		public void run() {
			int i = 0;
			while (true) {
				try {
					iCount = (i + 1) * 5;
					if (i == 20) {
						i = 0;
					}
					Message msg = new Message();
					msg.what = MSG_PROGRESS_CYCLE;
					handler.sendMessage(msg);
					Thread.sleep(250L);
				} catch (Exception e) {
					e.printStackTrace();
				}
				i++;
			}
		}
    };
    
    /**
     * 连接到软件服务器
     */
    private Runnable connServer = new Runnable(){
        public void run(){
            URL url = null;
            HttpURLConnection connection = null;
            try{
            	Thread.sleep(500L);
            }catch(Exception e){
                Message msg = new Message();
                msg.what = MSG_NETWORK_CODE;
                msg.obj = "连接异常["+e.getMessage()+"]";
                handler.sendMessage(msg);
                e.printStackTrace();
            }finally{
                if(null != connection){
                    connection.disconnect();
                }
                if(null != url){
                    url = null;
                }
            }
        }
    };
    
    /**
     * 检查网络
     * 
     */
    private Runnable mCheckNetwork = new Runnable(){
        public void run(){
            URL url = null;
            HttpURLConnection connection = null;
            try{
                url = new URL(DEST_URL);
                connection = (HttpURLConnection)url.openConnection();
                connection.addRequestProperty("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
                connection.addRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
                connection.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
                connection.addRequestProperty("Connection", "keep-alive");
                connection.addRequestProperty("Accept", "*/*");
                connection.addRequestProperty("Cache-Control", "max-age=0");
                connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11");
                connection.connect();
                int code = connection.getResponseCode();
                switch(code){
                    case HttpURLConnection.HTTP_OK:
                        handler.postDelayed(new Runnable(){
                        	public void run(){
                        		isFinish = true;
                                startActivity(new Intent(getContext(), MainActivity.class));
                                finish();
                        	}
                        }, 100L);
                        break;
                    default:
                        handler.post(new Runnable(){
                        	public void run(){
                        		progressBar.setVisibility(View.GONE);  
                        	}
                        });
                        Message msg = new Message();
                        msg.what = MSG_NETWORK_CODE;
                        msg.obj = "服务端返回错误\r\n["+code+"|"+connection.getResponseMessage()+"]";
                        handler.sendMessage(msg);
                        break;
                }
            }catch(IOException e){
                handler.post(new Runnable(){
                	public void run(){
                		progressBar.setVisibility(View.GONE);  
                	}
                });
                Message msg = new Message();
                msg.what = MSG_NETWORK_CODE;
                msg.obj = "连接异常\r\n["+e.getMessage()+"]";
                handler.sendMessage(msg);
                e.printStackTrace();
            }catch(Exception e){
                handler.post(new Runnable(){
                	public void run(){
                		progressBar.setVisibility(View.GONE);  
                	}
                });
                Message msg = new Message();
                msg.what = MSG_NETWORK_CODE;
                msg.obj = "连接异常\r\n["+e.getMessage()+"]";
                handler.sendMessage(msg);
                e.printStackTrace();
            }finally{
                if(null != connection){
                    connection.disconnect();
                }
                if(null != url){
                    url = null;
                }
            }
        }
    };
    
    private Context getContext(){
        return this;
    }
}
