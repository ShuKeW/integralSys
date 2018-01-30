package com.skw.integralsys.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;

/**
 * @创建人 weishukai
 * @创建时间 2018/1/30 14:11
 * @类描述 一句话描述 你的类
 */

public class FileUtil {
    public static final String DBNAME = "zhongyouxiyingvip";

    public static File getBaseDir(Context context) {
        File baseDir = getAndroidBaseDir(context);
        if (!baseDir.exists()) {
            baseDir.mkdir();
            if (!baseDir.exists()) { // check baseDir.exists() because of potential concurrent processes
                throw new RuntimeException("Could not init Android base dir at " + baseDir.getAbsolutePath());
            }
        }
        if (!baseDir.isDirectory()) {
            throw new RuntimeException("Android base dir is not a dir: " + baseDir.getAbsolutePath());
        }
        return baseDir;
    }

    private static File getAndroidBaseDir(Context context) {
        return new File(getAndroidFilesDir(context), "objectbox");
    }

    private static File getAndroidFilesDir(Context context) {
        File filesDir;
        try {
            Method getFilesDir = context.getClass().getMethod("getFilesDir");
            filesDir = (File) getFilesDir.invoke(context);
            if (filesDir == null) {
                // Race condition in Android before 4.4: https://issuetracker.google.com/issues/36918154 ?
                System.err.println("getFilesDir() returned null - retrying once...");
                filesDir = (File) getFilesDir.invoke(context);
            }
        } catch (Exception e) {
            throw new RuntimeException(
                    "Could not init with given Android context (must be sub class of android.content.Context)", e);
        }
        if (filesDir == null) {
            throw new IllegalStateException("Android files dir is null");
        }
        if (!filesDir.exists()) {
            throw new IllegalStateException("Android files dir does not exist");
        }
        return filesDir;
    }

    public static void importDb(Context context) {
        File file = new File(getBaseDir(context), DBNAME);
        if (file.exists() && file.isDirectory()) {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File fileDestDir = new File(Environment.getExternalStorageDirectory(), DBNAME);
                if (!fileDestDir.exists()) {
                    boolean result = fileDestDir.mkdirs();
                    Log.d("aa","目标创建结果："+result);
                }
                File[] files = file.listFiles();
                for (File fileDb : files) {
                    copyFile(fileDb, new File(fileDestDir, fileDb.getName()));
                }
            } else {
                Toast.makeText(context, "sd卡不可用", Toast.LENGTH_LONG).show();
            }


        }
    }

    private static void copyFile(File source, File dest) {
        Log.d("aa", "原地址：" + source.getAbsolutePath());
        Log.d("aa", "目标地址：" + dest.getAbsolutePath());
        if (dest.exists()) {
            dest.delete();
        } else {
            try {
                dest.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(source);
            output = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
