package com.skw.integralsys.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
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

    public static void importDb(Context context, Uri uri) {
        File fileDest = new File(getBaseDir(context), DBNAME);
        if (fileDest.exists() && fileDest.isDirectory()) {
            File fileSource = new File(uri.getPath());
            copyFile(fileSource, new File(fileDest, fileSource.getName()));
        }
    }

    public static void exportDb(Context context) {
        File file = new File(getBaseDir(context), DBNAME);
        if (file.exists() && file.isDirectory()) {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File fileDestDir = new File(Environment.getExternalStorageDirectory(), DBNAME);
                if (!fileDestDir.exists()) {
                    boolean result = fileDestDir.mkdirs();
                    Log.d("aa", "目标创建结果：" + result);
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
        if (!dest.exists()) {
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


    /**
     * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     */
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

//        // DocumentProvider
//        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
//            // ExternalStorageProvider
//            if (isExternalStorageDocument(uri)) {
//                final String docId = DocumentsContract.getDocumentId(uri);
//                final String[] split = docId.split(":");
//                final String type = split[0];
//
//                if ("primary".equalsIgnoreCase(type)) {
//                    return Environment.getExternalStorageDirectory() + "/" + split[1];
//                }
//            }
//            // DownloadsProvider
//            else if (isDownloadsDocument(uri)) {
//
//                final String id = DocumentsContract.getDocumentId(uri);
//                final Uri contentUri = ContentUris.withAppendedId(
//                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
//
//                return getDataColumn(context, contentUri, null, null);
//            }
//            // MediaProvider
//            else if (isMediaDocument(uri)) {
//                final String docId = DocumentsContract.getDocumentId(uri);
//                final String[] split = docId.split(":");
//                final String type = split[0];
//
//                Uri contentUri = null;
//                if ("image".equals(type)) {
//                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//                } else if ("video".equals(type)) {
//                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//                } else if ("audio".equals(type)) {
//                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//                }
//
//                final String selection = "_id=?";
//                final String[] selectionArgs = new String[]{split[1]};
//
//                return getDataColumn(context, contentUri, selection, selectionArgs);
//            }
//        }
//        // MediaStore (and general)
//        else if ("content".equalsIgnoreCase(uri.getScheme())) {
//            return getDataColumn(context, uri, null, null);
//        }
//        // File
//        else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            return uri.getPath();
//        }
        return null;
    }

}
