package com.skw.integralsys.utils;

import android.app.Activity;
import android.os.Handler;

/**
 * @创建人 weishukai
 * @创建时间 18/1/21 下午2:05
 * @类描述 一句话说明这个类是干什么的
 */

public class Utils {
    public static void delayFinish(final Activity activity, long time) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                activity.finish();
            }
        }, time);
    }
}
