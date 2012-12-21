
package com.example.devinfodemo.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.devinfodemo.R;
import com.example.devinfodemo.json.bean.Hardware;
import com.example.devinfodemo.json.bean.Phone;
import com.example.devinfodemo.json.bean.Software;
import com.example.devinfodemo.json.bean.hd.JCamera;
import com.example.devinfodemo.json.bean.hd.JNetwork;
import com.example.devinfodemo.json.bean.hd.JSDInfo;
import com.example.devinfodemo.json.bean.hd.JSensor;
import com.example.devinfodemo.json.bean.hd.JSensorDetail;
import com.example.devinfodemo.json.bean.sd.DatabaseInfo;
import com.example.devinfodemo.service.DeviceService;
import com.google.gson.Gson;

import org.json.JSONException;

import java.io.File;
import java.util.List;

public class MainActivity extends Activity implements OnClickListener {
    private final static String TAG = "MainActivity";

    private final Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            String json = "";
            if (null != msg) {
                json = (String) msg.obj;
            }
            Toast.makeText(getContext(),
                    "生成Json成功，耗时:[" + msg.arg1 + "ms],长度:" + json.getBytes().length,
                    Toast.LENGTH_SHORT).show();
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // 启动服务
        Intent intentService = new Intent(this, DeviceService.class);
        startService(intentService);

        DeviceService.getInstance();

        Button btnSersor = (Button) findViewById(R.id.btn_sensor);
        btnSersor.setOnClickListener(this);

        Button btnCamera = (Button) findViewById(R.id.btn_camera);
        btnCamera.setOnClickListener(this);

        Button btnMultiSim = (Button) findViewById(R.id.btn_multi_sim);
        btnMultiSim.setOnClickListener(this);

        Button btnNetwork = (Button) findViewById(R.id.btn_network);
        btnNetwork.setOnClickListener(this);

        Button btnAbout = (Button) findViewById(R.id.btn_abount);
        btnAbout.setOnClickListener(this);

        // Button btnOther = (Button) findViewById(R.id.btn_other);
        // btnOther.setOnClickListener(this);

        Button jsonBtn = (Button) findViewById(R.id.btn_json);
        jsonBtn.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sensor:
                startActivity(new Intent(this, SensorActivity.class));
                break;
            case R.id.btn_camera:
                startActivity(new Intent(this, CameraActivity.class));
                break;
            case R.id.btn_multi_sim:
                startActivity(new Intent(this, MultiSimActivity.class));
                break;
            case R.id.btn_network:
                startActivity(new Intent(this, NetworkActivity.class));
                break;
            case R.id.btn_abount:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.btn_json:
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            initJson();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                Toast.makeText(this, "正在生成json...", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, "等待开发...", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private Context getContext() {
        return this;
    }

    /**
     * 
     */
    private void initJson() throws JSONException {
        long start = System.currentTimeMillis();
        // 系统版本号
        int version = android.os.Build.VERSION.SDK_INT;

        Phone phone = new Phone();
        Hardware hd = new Hardware();

        // 摄像头
        JCamera camera = new JCamera();
        try {
            if (version > 8) {
                int cameraNum = getCameraNum();
                camera.setCameraCount(cameraNum);
            } else {
                String exceptionId = String.valueOf(System.currentTimeMillis());
                camera.setExceptionId(exceptionId);
                phone.getExceptions().put(exceptionId, "当前手机系统版本号低于可以运行获取摄像头数量的API的最低系统版本[9]");
            }
        } catch (Exception e) {
            String exceptionId = String.valueOf(System.currentTimeMillis());
            camera.setExceptionId(exceptionId);
            phone.getExceptions().put(exceptionId, "当前手机系统版本号低于可以运行获取摄像头数量的API的最低系统版本[9]");
        }
        hd.setCamera(camera);

        TelephonyManager tm = (TelephonyManager) getContext()
                .getSystemService(Context.TELEPHONY_SERVICE);

        try {
            // IMSI
            if (null != tm.getSubscriberId()) {
                hd.setImsi(tm.getSubscriberId());
            }
        } catch (Exception e) {
            String exceptionId = String.valueOf(System.currentTimeMillis());
            camera.setExceptionId(exceptionId);
            hd.setImsi(exceptionId);
            phone.getExceptions().put(exceptionId, "获取手机IMSI异常,");

        }

        try {
            // IMEI
            if (null != tm.getDeviceId()) {
                hd.setImei(tm.getDeviceId());
            }
        } catch (Exception e) {
            String exceptionId = String.valueOf(System.currentTimeMillis());
            hd.setImei(exceptionId);
            phone.getExceptions().put(exceptionId, "获取手机IMEI异常,");

        }

        // 传感器
        // 从系统服务中获得传感器管理器
        JSensor sensor = new JSensor();
        try {
            SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            // 从传感器管理器中获得全部的传感器列表
            List<Sensor> allSensors = sm.getSensorList(Sensor.TYPE_ALL);
            sensor.setTotal(allSensors.size());
            // 显示每个传感器的具体信息
            for (Sensor s : allSensors) {
                JSensorDetail detail = new JSensorDetail();
                detail.setTypeId(s.getType());
                detail.setTypeName(s.getName());
                detail.setTypeVersion(String.valueOf(s.getVersion()));
                sensor.getList().add(detail);
            }
        } catch (Exception e) {
            String exceptionId = String.valueOf(System.currentTimeMillis());
            sensor.setExceptionId(exceptionId);
            phone.getExceptions().put(exceptionId, "获取手机终端传感器异常,");
        }
        hd.setSersor(sensor);

        // 网络
        JNetwork network = new JNetwork();
        try {
            ConnectivityManager mgr = (ConnectivityManager) getContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = mgr.getActiveNetworkInfo();
            if (null != networkInfo) {
                int type = networkInfo.getType();
                switch (type) {
                    case ConnectivityManager.TYPE_MOBILE:
                        network.setActiviteNetwork("TYPE_MOBILE");
                        break;
                    case ConnectivityManager.TYPE_WIFI:
                        network.setActiviteNetwork("TYPE_WIFI");
                        break;
                    case ConnectivityManager.TYPE_WIMAX:
                        network.setActiviteNetwork("TYPE_WIMAX");
                        break;
                    default:
                        network.setActiviteNetwork("TYPE_" + type);
                        break;
                }
                String networkType = getPhoneNetworkType(getContext());
                network.setPhoneNetworkType(networkType);
            } else {
                network.setPhoneNetworkType(JNetwork.TYPE_UNKNOW);
                network.setActiviteNetwork(JNetwork.TYPE_UNKNOW);
            }
        } catch (Exception e) {
            String exceptionId = String.valueOf(System.currentTimeMillis());
            network.setExceptionId(exceptionId);
            phone.getExceptions().put(exceptionId, "获取手机网络状态异常,");
        }
        hd.setNetwork(network);

        // SD卡
        JSDInfo sdi = new JSDInfo();
        try {
            long[] sds = getSDCardMemory();
            sdi.setTotalSize(String.valueOf(formatSize(sds[0])));
            sdi.setAvailableSize(String.valueOf(formatSize(sds[1])));
            sdi.setInternalMemorySize(String.valueOf(formatSize(getTotalInternalMemorySize())));
        } catch (Exception e) {
            String exceptionId = String.valueOf(System.currentTimeMillis());
            sdi.setExceptionId(exceptionId);
            phone.getExceptions().put(exceptionId, "获取手机SD卡异常,");
        }
        hd.setSdInfo(sdi);
        phone.setHardware(hd);

        Software sd = new Software();
        // 数据库信息
        DatabaseInfo db = new DatabaseInfo();
        ContentResolver resolver;
        resolver = getContext().getContentResolver();
        Cursor cursor;
        try {
            // contacts 表中的联系人
            cursor = resolver.query(
                    ContactsContract.Contacts.CONTENT_URI,
                    new String[] {
                        ContactsContract.Contacts._ID
                    }, null, null, null);

            while (cursor.moveToNext()) {
                db.setContactOk(true);
                break;
            }
            cursor.close();
        } catch (Exception e) {
            String exceptionId = String.valueOf(System.currentTimeMillis());
            db.setContactExceptionId(exceptionId);
            phone.getExceptions().put(exceptionId, "访问手机数据库表[contact]库异常,");
        }

        try {
            // raw_contacts 表中的联系人
            cursor = resolver.query(
                    ContactsContract.RawContacts.CONTENT_URI,
                    new String[] {
                            ContactsContract.RawContacts._ID
                    }, null,
                    null, null);

            while (cursor.moveToNext()) {
                db.setRawContactOk(true);
            }
            cursor.close();

        } catch (Exception e) {
            String exceptionId = String.valueOf(System.currentTimeMillis());
            db.setRawContactExceptionId(exceptionId);
            phone.getExceptions().put(exceptionId, "访问手机数据库表[raw_contact]库异常,");
        }

        try {
            String url = null;
            String authority = getAuthorityFromPermission(getContext(),
                    "com.android.launcher.permission.WRITE_SETTINGS");
            if (null != authority) {
                url = "content://" + authority + "/favorites?notify=true";
            }
            if (null != url && !url.equals("")) {
                cursor = resolver.query(Uri.parse(url), new String[] {
                        "title"
                }, null, null, null);
                while (cursor.moveToNext()) {
                    db.setLanuncher(true);
                    break;
                }
            } else {
                String exceptionId = String.valueOf(System.currentTimeMillis());
                db.setLanuncherExceptionId(exceptionId);
                phone.getExceptions().put(exceptionId,
                        "访问手机数据库表[lanuncher]库异常,构建URL[" + url + "]失败");
            }
        } catch (Exception e) {
            String exceptionId = String.valueOf(System.currentTimeMillis());
            db.setLanuncherExceptionId(exceptionId);
            phone.getExceptions().put(exceptionId, "访问手机数据库表[lanuncher]库异常,");
        }
        sd.setDatabas(db);

        try {
            // 系统版本号
            sd.setSysVersion(String.valueOf(version));

            // 厂商
            sd.setHsman(android.os.Build.MANUFACTURER);

            // 机型
            sd.setHstype(android.os.Build.MODEL);

            // 屏幕大小
            DisplayMetrics dm = new DisplayMetrics();
            WindowManager wmgr = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            wmgr.getDefaultDisplay().getMetrics(dm);
            int screenWidth = dm.widthPixels;
            int screenHeight = dm.heightPixels;
            sd.setScreenSize(screenWidth + "x" + screenHeight);
        } catch (Exception e) {
            String exceptionId = String.valueOf(System.currentTimeMillis());
            phone.getExceptions().put(exceptionId, "获取手机基本信息异常,");
        }

        phone.setSoftware(sd);
        // JSONObject json;
        Gson gson = new Gson();
        phone.setIp("172.168.1.110");
        String json = gson.toJson(phone);
        Log.d(TAG, "JSON长度:" + json.length());
        Log.d(TAG, json.toString());
        Long spend = System.currentTimeMillis() - start;
        Message msg = new Message();
        msg.obj = json;
        msg.arg1 = spend.intValue();
        mHandler.sendMessage(msg);
    }

    @TargetApi(9)
    private int getCameraNum() {
        int cameraCount = 0;
        /**
         * 三星 手机会崩溃 摩托ME860会崩溃 可能是因为TargetApi的最低版本不符合导致
         */
        cameraCount = Camera.getNumberOfCameras();

        return cameraCount;
    }

    /**
     * 获取手机网络类型
     * 
     * @param context
     * @return
     */
    private String getPhoneNetworkType(Context context) {
        String networkType = "";
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        int type = tm.getNetworkType();
        switch (type) {
            case TelephonyManager.NETWORK_TYPE_CDMA:
                networkType = "NETWORK_TYPE_CDMA";
                break;
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                networkType = "NETWORK_TYPE_1xRTT";
                break;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                networkType = "NETWORK_TYPE_EDGE";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                networkType = "NETWORK_TYPE_EVDO_0";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                networkType = "NETWORK_TYPE_EVDO_A";
                break;
            // case TelephonyManager.NETWORK_TYPE_EVDO_B:
            // networkType = "NETWORK_TYPE_EVDO_B";
            // break;
            case TelephonyManager.NETWORK_TYPE_GPRS:
                networkType = "NETWORK_TYPE_GPRS";
                break;
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                networkType = "NETWORK_TYPE_HSDPA";
                break;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                networkType = "NETWORK_TYPE_HSPA";
                break;
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                networkType = "NETWORK_TYPE_HSUPA";
                break;
            // case TelephonyManager.NETWORK_TYPE_IDEN:
            // networkType = "NETWORK_TYPE_IDEN";
            // break;
            case TelephonyManager.NETWORK_TYPE_UMTS:
                networkType = "NETWORK_TYPE_UMTS";
                break;
            default:
                networkType = "NETWORK_TYPE_UNKNOWN";
                break;
        }
        return networkType;
    }

    /**
     * SD卡存储大小
     * 
     * @return
     */
    public long[] getSDCardMemory() {
        long[] sdCardInfo = new long[2];
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(sdcardDir.getPath());
            long bSize = sf.getBlockSize();
            long bCount = sf.getBlockCount();
            long availBlocks = sf.getAvailableBlocks();

            sdCardInfo[0] = bSize * bCount;// 总大小
            sdCardInfo[1] = bSize * availBlocks;// 可用大小
        }
        return sdCardInfo;
    }

    /**
     * 内部存储大小
     * 
     * @return
     */
    public long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    /**
     * 格式化数据的方法
     * 
     * @param size
     * @return
     */
    public String formatSize(long size) {
        String suffix = null;
        float fSize = 0;

        if (size >= 1024) {
            suffix = "KB";
            fSize = size / 1024;
            if (fSize >= 1024) {
                suffix = "MB";
                fSize /= 1024;
            }
            if (fSize >= 1024) {
                suffix = "GB";
                fSize /= 1024;
            }
        } else {
            fSize = size;
        }
        java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");
        StringBuilder resultBuffer = new StringBuilder(df.format(fSize));
        if (suffix != null)
            resultBuffer.append(suffix);
        return resultBuffer.toString();
    }

    /**
     * 通过上下文和权限找出对应的权威路径
     * 
     * @param context
     * @param permission
     * @return
     */
    private static String getAuthorityFromPermission(Context context, String permission) {
        if (permission == null)
            return null;
        List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(
                PackageManager.GET_PROVIDERS);
        if (packs != null) {
            for (PackageInfo pack : packs) {
                ProviderInfo[] providers = pack.providers;
                if (providers != null) {
                    for (ProviderInfo provider : providers) {
                        if (permission.equals(provider.readPermission))
                            return provider.authority;
                        if (permission.equals(provider.writePermission))
                            return provider.authority;
                    }
                }
            }
        }
        return null;
    }

}
