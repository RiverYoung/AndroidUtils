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

