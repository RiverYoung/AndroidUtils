# AndroidUtils
Summary of some testing and development tools related Android.



AIDL ：Android AIDL demo；

Utils：AndroidUtils 、ShellUtils；



ShellUtils 使用说明：

```java
ShellUtils.CommandResult commandResult = new ShellUtils.CommandResult();
commandResult = ShellUtils.execCommand("cat /proc/asound/devices",false);
if (0 == commandResult.result){
    Log.d(TAG, "command success : " + commandResult.successMsg);
}else{
    Log.e(TAG, "command failed : " + commandResult.errorMsg);
}
```



HandlerTestDemo ：

[MainActivity.java](https://github.com/RiverYoung/AndroidUtils/blob/master/HandlerTestDemo/app/src/main/java/com/riveryoung/handlertestdemo/MainActivity.java) 分别使用了 Handler、runOnUiThread 和 HandlerThread 完成 UI 的更新；

[AsyncTaskTestActivity.java](https://github.com/RiverYoung/AndroidUtils/blob/master/HandlerTestDemo/app/src/main/java/com/riveryoung/handlertestdemo/AsyncTaskTestActivity.java) 则使用 AsyncTask 完成异步下载图片的例子，展示 AsyncTask 的使用方法；

以上两个文件主要为了熟悉 Android Handler 消息机制，并引入 HandlerThread，AsyncTask 的使用例子加以巩固；加强对 Handler 的理解；



[IntentServiceDemo](https://github.com/RiverYoung/AndroidUtils/blob/a556f69eb4e0485cf5514820edca907eeb22a6f5/IntentServiceDemo/src/main/java/com/riveryoung/intentservicedemo/MyIntentService.java#L34)：使用 IntentService 实现异步下载图像的功能，加强对 IntentService 的理解，区分 IntentService 与 Service 的区别；