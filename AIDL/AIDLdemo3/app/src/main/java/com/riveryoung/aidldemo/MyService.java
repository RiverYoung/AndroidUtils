package com.riveryoung.aidldemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class MyService extends Service {
    private static final String TAG = "Remote Service";

    private IBinder mBinder = new IMyAidlInterface.Stub() {
        @Override
        public String getInfo(String name) throws RemoteException {
            return TAG;
        }
    };

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
