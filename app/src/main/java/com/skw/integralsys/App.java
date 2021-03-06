package com.skw.integralsys;

import android.app.Application;

import com.skw.integralsys.db.MyObjectBox;
import com.skw.integralsys.utils.FileUtil;

import io.objectbox.BoxStore;

/**
 * @创建人 weishukai
 * @创建时间 2018/1/15 11:37
 * @类描述 一句话描述 你的类
 */

public class App extends Application {
    private BoxStore boxStore;

    @Override
    public void onCreate() {
        super.onCreate();
//        boxStore = MyObjectBox.builder().androidContext(App.this).build();
        boxStore = MyObjectBox.builder().androidContext(App.this).baseDirectory(FileUtil.getBaseDir(App.this)).name(FileUtil.DBNAME).build();
    }

    public BoxStore getBoxStore() {
        return boxStore;
    }
}
