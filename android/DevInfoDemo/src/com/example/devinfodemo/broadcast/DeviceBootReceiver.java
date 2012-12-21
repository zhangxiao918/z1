
package com.example.devinfodemo.broadcast;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.example.devinfodemo.service.DeviceService;

/**
 * @ClassName: DeviceBootReceiver
 * @Description: 启动手机时同时启动手信service
 * @author Michael.Pan
 * @date 2012-2-22 下午02:39:42
 */
public class DeviceBootReceiver extends BroadcastReceiver {

    private static final String TAG = DeviceBootReceiver.class.getSimpleName();
    private static final int MSG_DELAY_START_SERVICE = 0x01;
    private static final int DELAY_MILLIS = 3000;// 3000ms
    protected ContentResolver resolver;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_DELAY_START_SERVICE:
                    Context context = (Context) msg.obj;
                    getContactsLocalList(context);
                    Intent IntentService = new Intent(context, DeviceService.class);
                    context.startService(IntentService);
                    break;
            }
        }
    };

    @Override
    public void onReceive(final Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_DELAY_START_SERVICE, context),
                    DELAY_MILLIS);
            Toast.makeText(context, "启动完成...", Toast.LENGTH_LONG).show();
        }
    }

    public void getContactsLocalList(Context context) {
        int cc = 0;
        int rcc = 0;

        resolver = context.getContentResolver();
        StringBuilder sb = new StringBuilder();

        // contacts 表中的联系人
        Cursor cursor = resolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                new String[] {
                    ContactsContract.Contacts._ID
                }, null, null, null);

        while (cursor.moveToNext()) {
            cc = cursor.getCount();
            break;
        }
        cursor.close();

        // raw_contacts 表中的联系人
        cursor = resolver.query(
                ContactsContract.RawContacts.CONTENT_URI,
                new String[] {
                        ContactsContract.RawContacts._ID
                }, null,
                null, null);

        sb = sb.delete(0, sb.length());
        while (cursor.moveToNext()) {
            rcc = cursor.getCount();
        }
        cursor.close();
        Toast.makeText(context, "contact表数据量=" + cc + "|raw_contact表数据量=" + rcc, Toast.LENGTH_LONG)
                .show();
    }

}
