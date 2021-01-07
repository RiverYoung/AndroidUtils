package com.riveryoung.aidlclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.riveryoung.aidlserver.IMyAidlInterface;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private Button btn_connect_service;

    private IMyAidlInterface iMyAidlInterface;

    private ServiceConnection serviceConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(TAG, "onServiceConnected");
            iMyAidlInterface = IMyAidlInterface.Stub.asInterface(iBinder);

            try {
                String str = iMyAidlInterface.getName();
                Log.d(TAG, "catch name from service : " + str);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(TAG, "onServiceDisconnected");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_connect_service = (Button) findViewById(R.id.btn_connect_service);
        btn_connect_service.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_connect_service:
                Log.d(TAG, "connect service");
                startAndConnectService();
                break;
            default:
                break;
        }
    }

    private void startAndConnectService() {
        Intent intent = new Intent();
        intent.setPackage("com.riveryoung.aidlserver");
        intent.setAction("com.riveryoung.aidlserver.MyService");
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }
}