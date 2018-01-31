package com.skw.integralsys.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

/**
 * @创建人 weishukai
 * @创建时间 18/1/21 下午2:05
 * @类描述 一句话说明这个类是干什么的
 */

public class Utils {
    private static long lastTime;

    public static void delayFinish(final Activity activity, long time) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                activity.finish();
            }
        }, time);
    }

    public static void sysExit(Context context, long time) {
        long value = time - lastTime;
        Log.d("aa", time + " - " + lastTime + " = " + value);
        if (lastTime != 0 && value / 1000 <= 2) {
            lastTime = 0;
            System.exit(0);
        } else {
            lastTime = time;
            Toast.makeText(context, "再按一下退出", Toast.LENGTH_LONG).show();
        }
    }

    public static boolean isGrantExternalRW(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);
            return false;
        }
        return true;
    }


}
