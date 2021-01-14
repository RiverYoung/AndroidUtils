package com.riveryoung.intentservicedemo;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * helper methods.
 */
public class MyIntentService extends IntentService {

    private static final String TAG = "MyIntentService"; //^(?!.*(MyIntentService)).*$

    private static final String ACTION_START_UPLOAD_IMG = "com.riveryoung.intentservicedemo.action.START_UPLOAD_IMG";

    public static final String ACTION_REPLY_CALLBACK = "com.riveryoung.intentservicedemo.action.REPLY_CALLBACK";

    public static final String EXTRA_IMG_PATH = "com.riveryoung.intentservicedemo.extra.IMG_PATH";

    public MyIntentService() {
        super("MyIntentService");
    }

    /**
     * Starts this service to perform action startUpLoadImg with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startUpLoadImg(Context context, String imgPath) {
        Log.d(TAG, "startUpLoadImg");
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_START_UPLOAD_IMG);
        intent.putExtra(EXTRA_IMG_PATH, imgPath);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent");
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_START_UPLOAD_IMG.equals(action)) {
                final String imgPath = intent.getStringExtra(EXTRA_IMG_PATH);
                handleUpLoadImage(imgPath);
            }
        }
    }

    /**
     * Handle action handleUpLoadImage in the provided background thread with the provided
     * parameters.
     */
    private void handleUpLoadImage(String imgPath) {
        Log.d(TAG, "handlerUpLoadImage...");
        try {
            Thread.sleep(3000);
            replyCaller(imgPath);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //执行完耗时的上传图像后，通知调用者任务执行完成
    private void replyCaller(String imgPath) {
        Log.d(TAG, "replyCaller");
        Intent intent = new Intent(ACTION_REPLY_CALLBACK);
        intent.putExtra(EXTRA_IMG_PATH, imgPath);
        sendBroadcast(intent);
    }
}
