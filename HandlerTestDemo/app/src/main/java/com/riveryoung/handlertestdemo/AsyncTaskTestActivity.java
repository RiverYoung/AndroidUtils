package com.riveryoung.handlertestdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AsyncTaskTestActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AsyncTaskTestActivity";

    private Button btn_download_img;

    private Button btn_cancel_download;

    private ProgressBar progressBar;

    private ImageView imageView;

    private MyAsyncTask myAsyncTask;

    //public String mUrl = "http://image.uisdc.com/wp-content/uploads/2014/07/085625KMV.jpg";
    public String mUrl = "http://bkimg.cdn.bcebos.com/pic/c2fdfc039245d688d43f9f4639886a1ed21b0ff48eb5?x-bce-process=image/watermark,image_d2F0ZXIvYmFpa2UxODA=,g_7,xp_5,yp_5";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_task_test);

        btn_download_img = (Button) findViewById(R.id.btn_download_img);
        btn_cancel_download = (Button) findViewById(R.id.btn_cancel_download);
        btn_download_img.setOnClickListener(this);
        btn_cancel_download.setOnClickListener(this);

        progressBar = (ProgressBar) findViewById(R.id.pgb_download_progress);
        imageView = (ImageView) findViewById(R.id.imgv_download_image);

        myAsyncTask = new MyAsyncTask();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_download_img:
                Log.d(TAG, "onClick download img");
                checkSelfPermission();
                myAsyncTask.execute(mUrl);
                break;
            case R.id.btn_cancel_download:
                Log.d(TAG, "onClick cancel download");
                myAsyncTask.cancel(true);
                break;
            default:
                break;
        }
    }

    public void checkSelfPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

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
            Log.d(TAG, "doInBackground : 子线程执行耗时任务, 子线程 id ： "+ Thread.currentThread().getId());
            Log.d(TAG, "下载的图片地址 ： " + strings[0]);

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
            Log.d(TAG, "onProgressUpdate : 下载进度" + values[0] +", 运行在主线程，线程 id ： " + Thread.currentThread().getId());
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //下载完成后，需要更新 UI
            Log.d(TAG, "onPostExecute : 下载完成, 运行在主线程, 线程 id ： " + Thread.currentThread().getId() + ", 下载图片储存路径 : " + s);

            Bitmap bitmap = BitmapFactory.decodeFile(s);
            imageView.setImageBitmap(bitmap);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.d(TAG, "onCancelled : 取消下载, 线程 id ： " + Thread.currentThread().getId());
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
            Log.d(TAG, "onCancelled : " + s);
        }
    }
}