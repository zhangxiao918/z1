package com.bluestome.stepmessagedemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.bluestome.stepmessagedemo.message.GHandler;
import com.bluestome.stepmessagedemo.message.MsgCode;

public class MainActivity extends Activity {

    private GHandler mHandler = new GHandler(this);
    
    private TextView textView = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.showtext);
        
        mHandler.sendEmptyMessage(MsgCode.STEP_START);
        
    }

}
