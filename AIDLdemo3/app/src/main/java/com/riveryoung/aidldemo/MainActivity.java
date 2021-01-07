package com.riveryoung.aidldemo;

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
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private Button btn_connect_service;

    private IMyAidlInterface iMyAidlInterface;

    private ServiceConnection serviceConnection = new ServiceConnection(){
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            iMyAidlInterface = IMyAidlInterface.Stub.asInterface(iBinder);
            Log.d(TAG, "connect service success");

            try {
                String str = iMyAidlInterface.getInfo("String come from activity");
                Log.d(TAG, "Catch string from remote service : " + str);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(TAG, "service disconnected");
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
                Log.d(TAG, "onClick");
                Toast.makeText(this, "onclick" , Toast.LENGTH_SHORT).show();
                startAndConnectService();
                break;
            default:
                break;
        }
    }

    private void startAndConnectService() {
        Intent intent = new Intent(MainActivity.this, MyService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }
}