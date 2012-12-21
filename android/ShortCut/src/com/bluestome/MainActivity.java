
package com.bluestome;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends Activity {

    private static final String ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
    private static final String ACTION_UNINSTALL_SHORTCUT = "com.android.launcher.action.UNINSTALL_SHORTCUT";
    protected static final String TAG = MainActivity.class.getSimpleName();
    // 该参数可以保存在常量表中
    static boolean isCreateOk = false;

    TextView textView = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        textView = (TextView) findViewById(R.id.text_detail_id);
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        textView.setText("imsi:" + tm.getSimSerialNumber() + "\r\nimsi2:" + tm.getSubscriberId()
                + "\r\n imei:" + tm.getDeviceId());
        // 生成快捷方式
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!hasShortcut(getContext(),
                        "com.android.launcher.permission.WRITE_SETTINGS")) {
                    // TODO 判断是否存在快捷方式
                    createShortcut();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(textView.getText() + "\r\n" + " 成功创建快捷方式!");
                        }
                    });
                } else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(textView.getText() + "\r\n" +
                                    "已经存在该应用的快捷方式了!");
                        }
                    });
                    Log.d(TAG, "已经存在该应用的快捷方式了!");
                }
            }
        }).start();
    }

    private final Handler mHandler = new Handler() {
    };

    private Context getContext() {
        return this;
    }

    /**
     * 生成桌面快捷方式,不知能否知道创建快捷图标的结果？
     */
    private void createShortcut() {
        Intent shortcut = new Intent(ACTION_INSTALL_SHORTCUT); // 快捷方式的名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name)); //
        shortcut.putExtra("duplicate", false); // 不允许重复创建
        Intent intent = new Intent(this, MainActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent); // 快捷方式的图标
        ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(this,
                R.drawable.ic_launcher);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
        sendBroadcast(shortcut);
    }

    /**
     * 删除程序的快捷方式
     */
    private void delShortcut() {
        Intent shortcut = new Intent(ACTION_UNINSTALL_SHORTCUT);
        // 快捷方式的名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
        // 指定当前的Activity为快捷方式启动的对象: 如 com.everest.video.VideoPlayer
        // 注意: ComponentName的第二个参数必须是完整的类名（包名+类名），否则无法删除快捷方式
        String appClass = this.getPackageName() + "." + this.getLocalClassName();
        ComponentName comp = new ComponentName(this.getPackageName(), appClass);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT,
                new Intent(Intent.ACTION_MAIN).setComponent(comp));
        sendBroadcast(shortcut);
    }

    /**
     * 通过上下文和权限找出对应的权威路径
     * 
     * @param context
     * @param permission
     * @return
     */
    private String getAuthorityFromPermission(Context context, String permission) {
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

    /**
     * 判断是否已经创建了桌面快捷方式
     * 
     * @see 该方法依赖终端条件比较强，需要对终端各类参数进行判断过滤
     *      对4.0的支持目前还没有方案,貌似4.0有管理快捷图标唯一的功能，不需要应用来控制
     *      #content://com.android.launcher.settings/favorites?notify=true#
     *      getSystemVersion() < 8
     *      #content://com.android.launcher2.settings/favorites?notify=true#
     * @return
     */
    public boolean hasShortcut(Context ctx, String permission) {
        String url = null;
        Log.d(TAG, "系统版本号：" + getSystemVersion());
        String authority = getAuthorityFromPermission(getContext(), permission);
        if (null != authority) {
            url = "content://" + authority + "/favorites?notify=true";
        }
        if (null != url && !url.equals("")) {
            ContentResolver cr = ctx.getContentResolver();
            Cursor cursor = cr.query(Uri.parse(url), new String[] {
                    "title"
            }, "title=?", new String[] {
                        getString(R.string.app_name)
                    },
                    null);
            if (cursor != null && cursor.moveToNext() && cursor.getCount() > 0) {
                cursor.close();
                return true;
            } else {
                final String tURL = url;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(textView.getText() + "\r\n" + "访问\r\n[" + tURL
                                + "]\r\n失败!");
                    }
                });
                Log.d(TAG, "cursor is null 可能访问[" + tURL + "]失败");
            }
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    textView.setText(textView.getText() + "\r\n" + " URL 获取失败!");
                }
            });

        }
        return false;
    }

    private int getSystemVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }

}
