
package com.example.devinfodemo.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.TextView;

import com.example.devinfodemo.R;

/**
 * @ClassName: MultiSimActivity
 * @Description: TODO
 * @author Bluestome.Zhang
 * @date 2012-9-28 下午12:50:10
 */
public class MultiSimActivity extends Activity {

    private static final String ISDOUBLE = "MULTI_SIM_ISDOUBLE";
    private static final String SIMCARD = "MULTI_SIM_SIMCARD";
    private static final String SIMCARD_1 = "MULTI_SIM_SIMCARD_1";
    private static final String SIMCARD_2 = "MULTI_SIM_SIMCARD_2";
    private final boolean isDouble = false;
    String imsi = "NULL";
    String imei = "NULL";
    String simSerialNumber = "UNKNOWN";
    String networkType = "NETWORK_TYPE_UNKNOWN";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multi_sim_main);
        // 准备显示信息的UI组建
        final TextView tx1 = (TextView) findViewById(R.id.TextView01);
        initIsDoubleTelephone(this);
        initNetworkType(this);
        tx1.setText("当前手机卡参数:" + "\n\tIMSI:" + imsi + "\n\tIMEI:" + imei
                + "\n\t手机卡串号:"
                + simSerialNumber
                + "\n\t手机网络类型:" + networkType);
    }

    public void initIsDoubleTelephone(Context context) {
        // Method method = null;
        // Object result_0 = null;
        // Object result_1 = null;
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        imsi = tm.getSubscriberId();
        imei = tm.getDeviceId();
        simSerialNumber = tm.getSimSerialNumber();
        System.out.println("\tsimSerialNumber:" + simSerialNumber);
        // try {
        // // 只要在反射getSimStateGemini
        // // 这个函数时报了错就是单卡手机（这是我自己的经验，不一定全正确）getSimStateGemini 该方法适用于MTK平台
        // method = TelephonyManager.class.getMethod("getSimStateGemini()", new
        // Class[] {
        // int.class
        // });
        // // 获取SIM卡1
        // result_0 = method.invoke(tm, new Object[] {
        // new Integer(0)
        // });
        // // 获取SIM卡1
        // result_1 = method.invoke(tm, new Object[] {
        // new Integer(1)
        // });
        // } catch (SecurityException e) {
        // isDouble = false;
        // e.printStackTrace();
        // // System.out.println("1_ISSINGLETELEPHONE:"+e.toString());
        // } catch (NoSuchMethodException e) {
        // isDouble = false;
        // e.printStackTrace();
        // // System.out.println("2_ISSINGLETELEPHONE:"+e.toString());
        // } catch (IllegalArgumentException e) {
        // isDouble = false;
        // e.printStackTrace();
        // } catch (IllegalAccessException e) {
        // isDouble = false;
        // e.printStackTrace();
        // } catch (InvocationTargetException e) {
        // isDouble = false;
        // e.printStackTrace();
        // } catch (Exception e) {
        // isDouble = false;
        // e.printStackTrace();
        // // System.out.println("3_ISSINGLETELEPHONE:"+e.toString());
        // }
        // SharedPreferences sp =
        // PreferenceManager.getDefaultSharedPreferences(context);
        // Editor editor = sp.edit();
        // if (isDouble) {
        // // 保存为双卡手机
        // editor.putBoolean(ISDOUBLE, true);
        // // 保存双卡是否可用
        // // 如下判断哪个卡可用.双卡都可以用
        // if (result_0.toString().equals("5") &&
        // result_1.toString().equals("5")) {
        // if (!sp.getString(SIMCARD, "2").equals("0")
        // && !sp.getString(SIMCARD, "2").equals("1")) {
        // editor.putString(SIMCARD, "0");
        // }
        // editor.putBoolean(SIMCARD_1, true);
        // editor.putBoolean(SIMCARD_2, true);
        // } else if (!result_0.toString().equals("5") &&
        // result_1.toString().equals("5")) {// 卡二可用
        // if (!sp.getString(SIMCARD, "2").equals("0")
        // && !sp.getString(SIMCARD, "2").equals("1")) {
        // editor.putString(SIMCARD, "1");
        // }
        // editor.putBoolean(SIMCARD_1, false);
        // editor.putBoolean(SIMCARD_2, true);
        // } else if (result_0.toString().equals("5") &&
        // !result_1.toString().equals("5")) {// 卡一可用
        // if (!sp.getString(SIMCARD, "2").equals("0")
        // && !sp.getString(SIMCARD, "2").equals("1")) {
        // editor.putString(SIMCARD, "0");
        // }
        // editor.putBoolean(SIMCARD_1, true);
        // editor.putBoolean(SIMCARD_2, false);
        // } else {// 两个卡都不可用(飞行模式会出现这种种情况)
        // editor.putBoolean(SIMCARD_1, false);
        // editor.putBoolean(SIMCARD_2, false);
        // }
        // } else {
        // // 保存为单卡手机
        // editor.putString(SIMCARD, "0");
        // editor.putBoolean(ISDOUBLE, false);
        // }
        // editor.commit();
    }

    private void initNetworkType(Context context) {
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
    }
}
