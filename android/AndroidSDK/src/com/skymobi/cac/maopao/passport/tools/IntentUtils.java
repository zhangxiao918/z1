package com.skymobi.cac.maopao.passport.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class IntentUtils {

	public static void startActivity(Context context,
			Class<? extends Activity> clazz) {
		Intent intent = new Intent(context, clazz);
		context.startActivity(intent);
	}

	public static void invokeWebBrowser(Activity activity, String uriString) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(uriString));
		activity.startActivity(intent);
	}

	public static void startActivity(Context context,
			Class<? extends Activity> clazz, Bundle bundle) {
		Intent intent = new Intent(context, clazz);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}
}
