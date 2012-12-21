package com.bluestome.hzti.qry;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bluestome.hzti.qry.common.Constants;
import com.bluestome.hzti.qry.net.QueryTools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity implements OnClickListener {

	private Spinner spinner = null;
	private Button btnOk;
	private Button btnCancel;

	// 车牌号码
	private EditText textCarNum;
	// 车辆识别码
	private EditText textCarId;
	// 校验码
	private EditText textCheckCode;

	private String carType = "小型汽车";

	private ImageView imageView = null;

	public static String AUTH_CODE_URL = "http://www.hzti.com/government/CreateCheckCode.aspx?"
			+ System.currentTimeMillis();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initNetwork();
		initUI();
	}

	/**
	 * 初始化网络 获取网站的COOKIE
	 */
	private void initNetwork() {
		request(Constants.URL);
	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
		// 获取spinner
		spinner = (Spinner) findViewById(R.id.spinner1);
		// 从资源中获取数组数据
		String[] array = getResources().getStringArray(R.array.select_car_type);
		ArrayAdapter<String> array_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, array);
		array_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(array_adapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				carType = parent.getItemAtPosition(position).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
			}
		});

		// 车牌号码
		textCarNum = (EditText) findViewById(R.id.e_car_license_num);
		textCarNum.setText("浙A");

		// 车辆识别码
		textCarId = (EditText) findViewById(R.id.e_car_id);

		// 校验码
		textCheckCode = (EditText) findViewById(R.id.e_check_code);

		// 校验码图片
		imageView = (ImageView) findViewById(R.id.checkCodeView);
		imageView.setOnClickListener(this);

		loadImage2(AUTH_CODE_URL, R.id.checkCodeView);

		// 初始化按钮
		btnOk = (Button) findViewById(R.id.btn_ok);
		btnOk.setOnClickListener(this);

		btnCancel = (Button) findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int vid = v.getId();
		switch (vid) {
		case R.id.btn_cancel:
			finish();
			break;
		case R.id.btn_ok:
			// 获取所有输入的参数
			final String carNum = textCarNum.getText().toString().trim();
			if (carNum.equals("") || carNum.length() < 7) {
				Toast.makeText(this, getString(R.string.tip_car_num), 2000)
						.show();
				return;
			}
			final String carId = textCarId.getText().toString().trim();
			if (carId.equals("") || carId.length() < 6) {
				Toast.makeText(this, getString(R.string.tip_car_id), 2000)
						.show();
				return;
			}
			final String checkCode = textCheckCode.getText().toString().trim();
			if (checkCode.equals("") || checkCode.length() < 4) {
				Toast.makeText(this, getString(R.string.tip_check_code), 2000)
						.show();
				return;
			}
			Toast.makeText(this,
					carNum + "|" + carId + "|" + checkCode + "|" + carType,
					3000).show();
			new Thread(new Runnable() {
				@Override
				public void run() {
					if (null != sb && !sb.toString().equals(""))
						QueryTools.doQuery(sb.toString(), Constants.URL, carId,
								carNum, carId, checkCode);
				}
			}).start();
			// TODO 将参数传入另一个界面处理网络相关
			// startActivity(new Intent(this,GridShowActivity.class));
			break;
		case R.id.checkCodeView:
			loadImage2(AUTH_CODE_URL, R.id.checkCodeView);
			break;
		}
	}

	final Handler handler2 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			((ImageView) MainActivity.this.findViewById(msg.arg1))
					.setImageBitmap((Bitmap) msg.obj);
		}
	};

	/**
	 * 获取校验码图片
	 */
	// private void loadImage(final String url, final int id) {
	// new Thread(new Runnable() {
	// @Override
	// public void run() {
	// Drawable drawable = null;
	// try {
	// drawable = Drawable.createFromStream(
	// new URL(url).openStream(), "image.png");
	// } catch (IOException e) {
	// Log.d("test", e.getMessage());
	// }
	// Message message = handler2.obtainMessage();
	// message.arg1 = id;
	// message.obj = drawable;
	// handler2.sendMessage(message);
	// }
	// }).start();
	// }

	void loadImage2(final String site, final int id) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				URL url = null;
				HttpURLConnection connection = null;
				InputStream is = null;
				ByteArrayOutputStream baos = null;
				try {
					url = new URL(site);
					connection = (HttpURLConnection) url.openConnection();
					connection.addRequestProperty("Accept-Charset",
							"GBK,utf-8;q=0.7,*;q=0.3");
					connection.addRequestProperty("Accept-Encoding",
							"gzip,deflate,sdch");
					connection.addRequestProperty("Accept-Language",
							"zh-CN,zh;q=0.8");
					connection.addRequestProperty("Connection", "keep-alive");
					connection.addRequestProperty("Origin",
							"http://www.hzti.com");
					connection.addRequestProperty("X-MicrosoftAjax",
							"Delta=true");
					connection.addRequestProperty("Accept", "*/*");
					connection.addRequestProperty("Referer", Constants.URL); // ?type=2&node=249
					connection.addRequestProperty("Cookie", sb.toString());
					connection.addRequestProperty("Cache-Control", "no-cache");
					connection
							.addRequestProperty(
									"User-Agent",
									"Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.63 Safari/535.7");
					connection.addRequestProperty("Content-Type",
							"application/x-www-form-urlencoded; charset=UTF-8");
					connection.setReadTimeout(10 * 1000);
					connection.setConnectTimeout(15 * 1000);
					connection.setRequestMethod("POST");
					connection.setDoOutput(true);
					connection.setDoInput(true);
					connection.connect();

					if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
						baos = new ByteArrayOutputStream();
						is = connection.getInputStream();
						int c;
						byte[] tmp = new byte[2048];
						while ((c = is.read(tmp)) != -1) {
							baos.write(tmp, 0, c);
						}
						byte[] body = baos.toByteArray();
						Bitmap bitMap = BitmapFactory.decodeByteArray(body, 0,
								body.length);
						Message message = handler2.obtainMessage();
						message.arg1 = id;
						message.obj = bitMap;
						handler2.sendMessage(message);
						baos.flush();
						baos.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (null != connection)
						connection.disconnect();
				}
			}
		}).start();
	}

	static StringBuilder sb = null;

	static void request(String site) {
		URL url = null;
		HttpURLConnection connection = null;
		try {
			url = new URL(site);
			connection = (HttpURLConnection) url.openConnection();
			connection.addRequestProperty("Connection", "keep-alive");
			if (null != sb)
				connection.addRequestProperty("Cookie", sb.toString());
			connection
					.addRequestProperty(
							"User-Agent",
							"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.89 Safari/537.1");
			connection.connect();
			if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
				// 服务端响应成功
				Map<String, List<String>> map = connection.getHeaderFields();
				if (null == sb) {
					sb = new StringBuilder();
					Iterator<String> it = map.keySet().iterator();
					while (it.hasNext()) {
						String key = it.next();
						if (null != key && !key.equals("")
								&& key.equals("Set-Cookie")) {
							List<String> values = map.get(key);
							for (String value : values) {
								System.out.println(key + "|" + value);
								sb.append(parserValue(value)).append(";");
							}

						}
					}
					if (sb.toString().endsWith(";")) {
						String tmp = sb.toString();
						tmp = tmp.substring(0, tmp.lastIndexOf(";"));
						sb = null;
						sb = new StringBuilder(tmp);
					}
					System.out.println("\r\trequest:" + sb.toString());
				} else {
					Iterator<String> it = map.keySet().iterator();
					while (it.hasNext()) {
						String key = it.next();
						List<String> values = map.get(key);
						for (String value : values) {
							System.out.println(key + "|" + value);
						}
					}

				}
			}
		} catch (IOException e) {

		} catch (Exception e) {

		} finally {
			if (null != connection)
				connection.disconnect();
		}
	}

	static String parserValue(String value) {
		String[] tmp = value.split(";");
		if (null != tmp && tmp.length > 0) {
			value = tmp[0];
		}
		System.out.println("\t parserValue:" + value);
		return value;
	}
}