package com.riveryoung.utils;

import android.app.AlarmManager;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import static androidx.core.content.ContextCompat.getSystemService;

public class AndroidUtils {
    /**
     * 打开指定包名的APP
     */
    public void openApp(Context context, String packageName){
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageInfo.packageName);

        List<ResolveInfo> apps = context.getPackageManager().queryIntentActivities(resolveIntent, 0);

        ResolveInfo resolveInfo = apps.iterator().next();

        if (resolveInfo != null){
            String localPackageName = resolveInfo.activityInfo.packageName;
            String localClassName = resolveInfo.activityInfo.name;

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            Log.d("openApp", "localPackageName = " + localPackageName + "localClassName" + localClassName);
            ComponentName componentName = new ComponentName(localPackageName, localClassName);
            intent.setComponent(componentName);
            context.startActivity(intent);
        }
    }

    /**
     *  Find File On USB function
     * */
    public String FindFileOnUSB(String filename) {
        String filepath = "";
        File usbroot = new File("/mnt/usb/");
        File targetfile;
        if (usbroot != null && usbroot.exists()) {
            File[] usbitems = usbroot.listFiles();
            int sdx = 0;
            for (; sdx < usbitems.length; sdx++) {
                if (usbitems[sdx].isDirectory()) {
                    targetfile = new File(usbitems[sdx].getPath() + "/" + filename);
                    if (targetfile != null && targetfile.exists()) {
                        filepath = usbitems[sdx].getPath() + "/" + filename;
                        break;
                    }
                }
            }
        }
        return filepath;
    }

    /**
     *  发送指定键值给 Android 系统
     * */
    public void sendKeyCode(final int keycode) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Instrumentation instrument = new Instrumentation();
                    instrument.sendKeyDownUpSync(keycode);
//                    Log.d(TAG, "sendKey " + keycode);
                }catch(Exception e){
//                    Log.e(TAG, "error when sendKeyDownUpSync" + e.toString());
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     *  设置 Android 系统日期
     * */
    public void setSysDate(Context context, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        long when = calendar.getTimeInMillis();

        if (when / 1000 < Integer.MAX_VALUE) {
            try {
                ((AlarmManager) context.getSystemService(Context.ALARM_SERVICE)).setTime(when);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("setSysDate : ", "Neither user 10096 nor current process has android.permission.SET_TIME");
            }
        }
    }

    /**
     *  设置 Android 系统时间
     * */
    public void setSysTime(Context context,int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, 0);

        long when = calendar.getTimeInMillis();

        if (when / 1000 < Integer.MAX_VALUE) {
            try {
                ((AlarmManager) context.getSystemService(Context.ALARM_SERVICE)).setTime(when);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("setSysTime: ", "Neither user 10096 nor current process has android.permission.SET_TIME");
            }
        }
    }
}
