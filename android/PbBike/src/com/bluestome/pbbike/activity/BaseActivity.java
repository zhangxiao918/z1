package com.bluestome.pbbike.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

public class BaseActivity extends Activity {

	protected Context mContext = null;
	
	protected static String uuid = "1";
	
    public BaseActivity() {
        super();
    }

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        return super.onCreateDialog(id);
    }

    
}
