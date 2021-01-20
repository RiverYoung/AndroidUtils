package com.riveryoung.handlerdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private TextView tv_name;

    private Button btn_change_text;

    private Button btn_change_text_post;

    private Button btn_change_text_runOnUiThread;

    public static final int UPDATE_TEXT = 0;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case UPDATE_TEXT:
                    // 主线程更新 UI
                    Log.d(TAG, "主线程更新 UI, 线程id ： " + Thread.currentThread().getId());
                    tv_name.setText("My name is river");
                    break;
                default:
                    break;
            }
        }
    };

    private Handler mHandler_post = new Handler();

    private Button btn_change_text_handlerThread;

    private HandlerThread mHandlerThread = new HandlerThread("myHandlerThread");

    private Handler mHandler3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_name = (TextView) findViewById(R.id.tv_text);
        tv_name.setText("What is your name ? ");
        btn_change_text = (Button) findViewById(R.id.btn_change_text);
        btn_change_text.setOnClickListener(this);

        btn_change_text_post = (Button) findViewById(R.id.btn_change_text_post);
        btn_change_text_post.setOnClickListener(this);

        btn_change_text_runOnUiThread = (Button) findViewById(R.id.btn_change_text_runOnUiThread);
        btn_change_text_runOnUiThread.setOnClickListener(this);

        btn_change_text_handlerThread = (Button) findViewById(R.id.btn_change_text_handlerThread);
        btn_change_text_handlerThread.setOnClickListener(this);

        mHandlerThread.start();
        mHandler3 = new Handler(mHandlerThread.getLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Log.d(TAG, "handleMessage and thread id : " + mHandlerThread.getId());
                final int what = msg.what;

                //获取到通知信息后，在子线程通知主线程更新 UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "runOnUiThread thread id : " + Thread.currentThread().getId());
                        tv_name.setText("get message form handlerthread : " + what);
                    }
                });
            }
        };
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_change_text:
                Log.d(TAG, "onClick change text");

                //开启子线程进行耗时操作后，再通知主线程更新UI
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //在子线程中执行耗时任务
                        Log.d(TAG, "执行耗时任务的子线程id ： " + Thread.currentThread().getId());
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        //子线程执行完耗时任务后，通过 sendMessage 通知主线程更新 UI 界面
                        Message msg = Message.obtain(); //推荐使用 obtain
                        msg.what = UPDATE_TEXT;
                        mHandler.sendMessage(msg);
                    }
                }).start();
                break;

            case R.id.btn_change_text_post:
                Log.d(TAG,"on Clicked change text post");

                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        //在子线程中执行耗时任务
                        Log.d(TAG, "执行耗时任务的子线程id ： " + Thread.currentThread().getId());
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        //子线程执行完耗时任务后，通过 post 通知主线程更新 UI 界面
                        mHandler_post.post(new Runnable() {
                            @Override
                            public void run() {
                                // 主线程更新 UI
                                Log.d(TAG, "主线程更新 UI, 线程id ： " + Thread.currentThread().getId());
                                tv_name.setText("My name is river, Nice to meet you, post sir");
                            }
                        });
                    }
                }).start();
                break;

            case R.id.btn_change_text_runOnUiThread:
                Log.d(TAG, "onClick change text by runOnUiThread");

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        //在子线程中执行耗时任务
                        Log.d(TAG, "执行耗时任务的子线程id ： " + Thread.currentThread().getId());
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        //使用 runOnUiThread 方法更新UI，可以省去使用 handler 需要的大量冗余代码
                        //包括新建子线程并发送message， 然后再在主线程的 handler 进行 handleMessage
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run() {
                                Log.d(TAG, "执行 runOnUiThread 的线程 ： " + Thread.currentThread().getId());
                                tv_name.setText("My name is river, Nice to meet you!");
                            }
                        });
                    }
                }).start();
                break;

            case R.id.btn_change_text_handlerThread:
                Log.d(TAG, "onClick change text by HandlerThread");
                mHandler3.post(new Runnable() {
                    @Override
                    public void run() {
                        //在子线程中执行耗时任务
                        Log.d(TAG, "执行耗时任务的子线程id ： " + Thread.currentThread().getId());
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });

                //通知 HandlerThread 绑定的子线程，且信息为 1
                mHandler3.sendMessage(mHandler3.obtainMessage(1));
                break;
            default:
                break;
        }
    }
}