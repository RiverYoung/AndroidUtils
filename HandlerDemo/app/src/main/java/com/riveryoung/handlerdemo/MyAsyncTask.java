package com.riveryoung.handlerdemo;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyAsyncTask extends AsyncTask<String, Integer, String> {

    private static final String TAG = "MyAsyncTask";

    private OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
            .connectTimeout(15000, TimeUnit.MILLISECONDS)
            .readTimeout(15000, TimeUnit.MILLISECONDS)
            .writeTimeout(15000, TimeUnit.MILLISECONDS)
            .build();

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //常用于初始化UI, 比如先显示下载进度提示框
        Log.d(TAG, "onPreExecute : 初始化下载界面并准备下载, 运行在主线程, 线程 id ： " + Thread.currentThread().getId());
    }

    @Override
    protected String doInBackground(String... strings) {
        //子线程执行耗时任务
        Log.d(TAG, "doInBackground : 子线程执行耗时任务"+ strings[0] +", 子线程 id ： "+ Thread.currentThread().getId());

        Request request = new Request.Builder()
                .url(strings[0])
                .build();
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        long downloadSize = 0;
        long totalSize;
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                inputStream = response.body().byteStream();
                totalSize = response.body().contentLength();
                File file = new File(Environment.getExternalStorageDirectory(), "test1111.jpg");
                fileOutputStream = new FileOutputStream(file);
                byte[] buffer = new byte[512 * 1024];
                int len;
                while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
                    fileOutputStream.write(buffer, 0, len);
                    fileOutputStream.flush();
                    downloadSize += len;
                    float percent = (float) downloadSize / totalSize;
                    publishProgress((int) (percent * 100)); //通知主线程更新 UI 中，下载进度的数值
                }
                return file.getAbsolutePath();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != inputStream) {
                    inputStream.close();
                }
                if (null != fileOutputStream) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        Log.d(TAG, "onProgressUpdate : 下载进度, 运行在主线程，线程 id ： " + Thread.currentThread().getId());
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //下载完成后，需要更新 UI
        Log.d(TAG, "onPostExecute : 下载完成, 运行在主线程, 线程 id ： " + Thread.currentThread().getId());

    }
}

