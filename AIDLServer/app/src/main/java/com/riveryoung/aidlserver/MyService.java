package com.riveryoung.aidlserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.appcompat.widget.DialogTitle;

public class MyService extends Service {

    private static final String TAG = "MyService";

    private IBinder mBinder = new IMyAidlInterface.Stub() {
        @Override
        public String getName() throws RemoteException {
            return "I am server";
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
