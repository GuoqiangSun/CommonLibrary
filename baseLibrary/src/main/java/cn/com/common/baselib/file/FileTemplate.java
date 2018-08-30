package cn.com.common.baselib.file;

import android.app.Application;
import android.os.Environment;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.com.common.baselib.app.IApp.IApp;
import cn.com.common.baselib.app.Tlog;

/**
 * author: Guoqiang_Sun
 * date : 2018/5/15 0015
 * desc :
 */
public abstract class FileTemplate implements IApp {

    protected FileTemplate() {
    }

    protected boolean exit = false;

    private WeakReference<Application> wr;

    @Override
    public void init(Application app) {

        if (exit) {
            return;
        }

        wr = new WeakReference<>(app);

        exit = mkdirs(getAppRootPath());

        if (!exit) {
            Tlog.e(" appRootPath mkdirs false ");
            return;
        }

        exit = mkdirs(getProjectPath());

        if (!exit) {
            Tlog.e(" projectPath mkdirs false ");
            return;
        }

        exit = mkdirs(getDBPath());

        if (!exit) {
            Tlog.e(" dbPath mkdirs false ");
            return;
        }

        exit = mkdirs(getDebugPath());

        if (!exit) {
            Tlog.e(" debugPath mkdirs false ");
            return;
        }

        exit = mkdirs(getTlogPath());

        if (!exit) {
            Tlog.e(" TlogPath mkdirs false ");
            return;
        }

        exit = mkdirs(getResourcePath());

        if (!exit) {
            Tlog.e(" resourcePath mkdirs false ");
            return;
        }

        exit = mkdirs(getFilePath());

        if (!exit) {
            Tlog.e(" filePath mkdirs false ");
        }

    }

    private boolean mkdirs(File resourcePath) {

        return resourcePath != null && (resourcePath.exists() || resourcePath.mkdirs());

    }

    public String saveProductDetectionLog(String msg) {
        File debugPath = getDebugPath();
        SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        String child = mDateFormat.format(new Date(System.currentTimeMillis())) + ".log";
        File logPath = new File(debugPath, child);

        if (FileUtil.saveFileMsg(logPath, msg)) {
            return logPath.getAbsolutePath();
        }
        return null;
    }

    /**
     * 保存H5错误日志
     *
     * @param msg
     */
    public void saveH5Exception(String msg) {
        File debugPath = getDebugPath();
        File logPath = new File(debugPath, "H5Error.log");
        FileUtil.saveException(logPath, generalHeadMsg(null).toString(), msg, FileUtil.isAppend(logPath, 60));
    }

    /**
     * 保存错误日志
     *
     * @param ex
     */
    public void saveAppException(Throwable ex) {
        saveAppException(null, ex);
    }

    /**
     * 保存错误日志
     *
     * @param ex
     */
    public void saveAppException(Thread t, Throwable ex) {
        File debugPath = getDebugPath();
        File logPath = new File(debugPath, "AppError.log");
        FileUtil.saveException(logPath, generalHeadMsg(t).toString(), ex, FileUtil.isAppend(logPath, 60));
    }

    private StringBuilder generalHeadMsg(Thread t) {
        SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        StringBuilder head = new StringBuilder(10);
        head.append("---Application catch Exception on ");
        head.append(mDateFormat.format(new Date(System.currentTimeMillis())));
        head.append(";\n*** In [");
        head.append((t != null ? t.getName() : "null"));
        head.append("] Thread ;");
        head.append(" pid:");
        head.append(String.valueOf(android.os.Process.myPid()));
        head.append(" ; packageName:");
        String pkgName = null;
        Application app;
        if (wr != null && (app = wr.get()) != null) {
            pkgName = app.getPackageName();
        }
        head.append(String.valueOf(pkgName));
        head.append(" ***");
        return head;
    }

    protected static final String APP_FILE_PATH_NAME = "file";

    /**
     * 本地资源
     *
     * @return
     */
    public File getFilePath() {
        return new File(getProjectPath(), APP_FILE_PATH_NAME);
    }

    protected static final String APP_RES_PATH_NAME = "res";

    /**
     * 本地资源
     *
     * @return
     */
    public File getResourcePath() {
        return new File(getProjectPath(), APP_RES_PATH_NAME);
    }

    protected static final String APP_DEBUG_PATH_NAME = "debug";

    /**
     * debug 目录
     *
     * @return
     */
    public File getDebugPath() {
        return new File(getProjectPath(), APP_DEBUG_PATH_NAME);
    }

    protected static final String APP_TLOG_PATH_NAME = "Tlog";

    /**
     * Tlog 目录
     *
     * @return
     */
    public File getTlogPath() {
        return new File(getDebugPath(), APP_TLOG_PATH_NAME);
    }

    protected static final String APP_DB_PATH_NAME = "db";

    /**
     * DB 目录
     *
     * @return
     */
    public File getDBPath() {
        return new File(getProjectPath(), APP_DB_PATH_NAME);
    }

    private static final String APP_PROJECT_PATH_NAME = "app";

    /**
     * 获取app缓存数据的目录
     *
     * @return
     */
    public File getProjectPath() {
        File file = initMyProjectPath();
        if (file != null) {
            return file;
        }
        return new File(getAppRootPath(), APP_PROJECT_PATH_NAME);
    }

    protected abstract File initMyProjectPath();

    protected static final String APP_ROOT_PATH_NAME = "startai";

    /**
     * 获取公司域名缓存数据的目录
     *
     * @return
     */
    public File getAppRootPath() {

        return new File(getStoragePath(), APP_ROOT_PATH_NAME);

    }


    /**
     * 获取系统存储目录
     *
     * @return
     */
    public File getStoragePath() {
        File directory = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            directory = Environment.getExternalStorageDirectory().getAbsoluteFile();
        }
        if (directory == null || !directory.exists()) {
//            Environment.getExternalStorageDirectory().getPath()
            directory = new File(File.separator + "sdcard");
        }
        return directory;
    }


}
