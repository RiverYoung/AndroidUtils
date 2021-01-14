package com.riveryoung.intentservicedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG="IntentServiceDemo";

    private Button mBtn_upload_img;

    MyIntentService myIntentService = new MyIntentService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtn_upload_img = (Button) findViewById(R.id.btn_upload_img);
        mBtn_upload_img.setOnClickListener(this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(myIntentService.ACTION_REPLY_CALLBACK);
        registerReceiver(mUpLoadImgReceiver, intentFilter);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_upload_img:
                Log.d(TAG, "click upload img");
                addUpLoadImgTask();
                break;
            default:
                break;
        }
    }

    private void addUpLoadImgTask() {
        Log.d(TAG, "addUpLoadImgTask");
        String imgPath = "/sdcard/Resource/1.png";
        myIntentService.startUpLoadImg(this, imgPath);
    }

    BroadcastReceiver mUpLoadImgReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction().equals(myIntentService.ACTION_REPLY_CALLBACK)) {
                String successPath = intent.getStringExtra(myIntentService.EXTRA_IMG_PATH);
                Log.d(TAG, "successPath : " + successPath);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mUpLoadImgReceiver);
    }
}