package cn.com.swain.baselib.app.utils;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;

import java.io.File;

import cn.com.swain.baselib.file.FileUtil;

/**
 * author: Guoqiang_Sun
 * date: 2018/11/27 0027
 * Desc: 主要功能有 :
 * *        清除内/外缓存，
 * *        清除数据库，
 * *       清除sharedPreference，
 * *       清除files,
 * *      清除自定义目录.
 */
@SuppressLint("SdCardPath")
public class DataCleanManager {

    /**
     * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache)
     */
    public static void cleanInternalCache(Context context) {
        FileUtil.deleteDirectoryFiles(context.getCacheDir());
    }

    /**
     * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases)
     */
    public static void cleanDatabases(Context context) {
        FileUtil.deleteDirectoryFiles(new File("/data/data/" + context.getPackageName() + "/databases"));
    }

    /**
     * * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs)
     * context
     */
    public static void cleanSharedPreference(Context context) {
        FileUtil.deleteDirectoryFiles(new File("/data/data/" + context.getPackageName() + "/shared_prefs"));
    }

    /**
     * 按名字清除本应用数据库
     */
    public static void cleanDatabaseByName(Context context, String dbName) {
        context.deleteDatabase(dbName);
    }

    /**
     * 清除/data/data/com.xxx.xxx/files下的内容
     */
    public static void cleanFiles(Context context) {
        FileUtil.deleteDirectoryFiles(context.getFilesDir());
    }

    /**
     * * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
     * context
     */
    public static void cleanExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            FileUtil.deleteDirectoryFiles(context.getExternalCacheDir());
        }
    }

    /**
     * 清除自定义路径下的文件，使用需小心，请不要误删。
     */
    public static void cleanCustomCache(String filePath) {
        FileUtil.deleteDirectoryFiles(new File(filePath));
    }

    /**
     * 清除本应用所有的数据
     */
    public static void cleanApplicationData(Context context) {
        cleanInternalCache(context);
        cleanExternalCache(context);
        cleanDatabases(context);
        cleanSharedPreference(context);
        cleanFiles(context);
    }

    /**
     * 清除本应用所有的数据
     */
    public static void cleanApplicationData(Context context, String... filepath) {
        cleanApplicationData(context);
        for (String filePath : filepath) {
            cleanCustomCache(filePath);
        }
    }

}
