package com.bluestome.hzti.qry;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bluestome.hzti.qry.common.CoreHandler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @ClassName: LanunchActivity
 * @Description: TODO
 * @author Bluestome.Zhang
 * @date 2012-8-9 下午09:16:40
 */
public class LanunchActivity extends Activity {

	private final String DEST_URL = "http://www.hzti.com/service/qry/violation_veh.aspx?type=2&node=249";

	private static final int MSG_NETWORK_CODE = 0x1000;
	private static final int MSG_PROGRESS_CYCLE = 0x2001;
	private static final int MSG_PROGRESS_STOP = 0x2002;

	private TextView textView = null;

	private ProgressBar progressBar = null;

	private int iCount = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.launcher);
		progressBar = (ProgressBar) findViewById(R.id.progress_bar_id);
		progressBar.setVisibility(View.VISIBLE);
		progressBar.setMax(100);
		progressBar.setProgress(0);
		new Thread(mCheckNetwork).start();
		new Thread(progressThread).start();
	}

	private final Handler handler = new CoreHandler(this) {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_NETWORK_CODE:
				if (null != msg && null != msg.obj) {
					textView = (TextView) findViewById(R.id.textView1);
					textView.setText((String) msg.obj);
				}
				break;
			case MSG_PROGRESS_CYCLE:
				progressBar.setProgress(iCount);
				break;
			case MSG_PROGRESS_STOP:
				progressBar.setVisibility(View.GONE);
				break;
			default:
				super.handleMessage(msg);
				break;
			}
		}

	};

	/**
	 * 处理进度条线程
	 */
	private final Runnable progressThread = new Runnable() {
		@Override
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

	private final Runnable mCheckNetwork = new Runnable() {
		@Override
		public void run() {
			URL url = null;
			HttpURLConnection connection = null;
			try {
				url = new URL(DEST_URL);
				connection = (HttpURLConnection) url.openConnection();
				connection.addRequestProperty("Accept-Charset",
						"GBK,utf-8;q=0.7,*;q=0.3");
				connection.addRequestProperty("Accept-Encoding",
						"gzip,deflate,sdch");
				connection.addRequestProperty("Accept-Language",
						"zh-CN,zh;q=0.8");
				connection.addRequestProperty("Connection", "keep-alive");
				connection.addRequestProperty("Origin", "http://www.hzti.com");
				connection.addRequestProperty("X-MicrosoftAjax", "Delta=true");
				connection.addRequestProperty("Accept", "*/*");
				connection.addRequestProperty("Cache-Control", "no-cache");
				connection
						.addRequestProperty(
								"User-Agent",
								"Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.63 Safari/535.7");
				connection.connect();
				int code = connection.getResponseCode();
				switch (code) {
				case HttpURLConnection.HTTP_OK:
					handler.post(new Runnable() {
						@Override
						public void run() {
							progressBar.setVisibility(View.GONE);
						}
					});
					startActivity(new Intent(getContext(), MainActivity.class));
					LanunchActivity.this.finish();
					break;
				default:
					handler.post(new Runnable() {
						@Override
						public void run() {
							progressBar.setVisibility(View.GONE);
						}
					});
					Message msg = new Message();
					msg.what = MSG_NETWORK_CODE;
					msg.obj = "服务端返回错误[" + code + "|"
							+ connection.getResponseMessage() + "]";
					handler.sendMessage(msg);
					break;
				}
			} catch (IOException e) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						progressBar.setVisibility(View.GONE);
					}
				});
				Message msg = new Message();
				msg.what = MSG_NETWORK_CODE;
				msg.obj = "连接异常[" + e.getMessage() + "]";
				handler.sendMessage(msg);
				e.printStackTrace();
			} catch (Exception e) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						progressBar.setVisibility(View.GONE);
					}
				});
				Message msg = new Message();
				msg.what = MSG_NETWORK_CODE;
				msg.obj = "连接异常[" + e.getMessage() + "]";
				handler.sendMessage(msg);
				e.printStackTrace();
			} finally {
				if (null != connection) {
					connection.disconnect();
				}
				if (null != url) {
					url = null;
				}
			}
		}
	};

	private Context getContext() {
		return this;
	}
}
