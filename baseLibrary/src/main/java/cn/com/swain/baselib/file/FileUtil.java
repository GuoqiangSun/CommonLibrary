package cn.com.swain.baselib.file;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.system.Os;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;

/**
 * author: Guoqiang_Sun
 * date : 2018/5/31 0031
 * desc :
 */
public class FileUtil {

    /**
     * 扫描指定文件
     * <p>
     * can use {@link android.media.MediaScannerConnection#scanFile(Context, String[], String[], MediaScannerConnection.OnScanCompletedListener)}
     *
     * @param app      application
     * @param filePath 文件路径
     */
    public static void scanFileAsync(Application app, String filePath) {
        scanFileAsync(app, new File(filePath));
    }

    /**
     * 扫描指定文件
     * <p>
     * can use {@link android.media.MediaScannerConnection#scanFile(Context, String[], String[], MediaScannerConnection.OnScanCompletedListener)}
     *
     * @param app      application
     * @param filePath 文件路径
     */
    public static void scanFileAsync(Application app, File filePath) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(filePath);
        intent.setData(uri);
        app.sendBroadcast(intent);
    }

    /**
     * 扫描指定目录
     *
     * @param app application
     * @param dir 目录路径
     */
    public static void scanDirAsync(Application app, String dir) {
        scanDirAsync(app, new File(dir));
    }

    public static final String ACTION_MEDIA_SCANNER_SCAN_DIR = "android.intent.action.MEDIA_SCANNER_SCAN_DIR";

    /**
     * 扫描指定目录
     *
     * @param app application
     * @param dir 目录路径
     */
    public static void scanDirAsync(Application app, File dir) {
        Intent intent = new Intent(ACTION_MEDIA_SCANNER_SCAN_DIR);
        Uri uri = fromFile(app, dir);
        intent.setData(uri);
        app.sendBroadcast(intent);
    }

    /**
     * 路径转uri
     *
     * @param app  {@link Application}
     * @param path 路径
     * @return {@link Uri}
     */
    public static Uri fromFile(Application app, File path) {
        Uri mUri;
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            // 从文件中创建uri
            mUri = Uri.fromFile(path);
        } else {
            String authority = app.getPackageName() + ".photo.provider";
            mUri = FileProvider.getUriForFile(app, authority, path);
        }
        return mUri;
    }

    /**
     * 扫描指定路径
     *
     * @param app  application
     * @param path 路径
     */
    public static void notifySystemToScan(Application app, String path) {
        notifySystemToScan(app, new File(path));
    }

    /**
     * 扫描指定路径
     *
     * @param app  application
     * @param path 路径
     */
    public static void notifySystemToScan(Application app, File path) {
        if (path.exists()) {
            if (path.isFile()) {
                scanFileAsync(app, path);
            } else if (path.isDirectory()) {
                scanDirAsync(app, path);
            } else {
                File parentFile = path.getParentFile();
                scanDirAsync(app, parentFile);
            }
        } else {
            File parentFile = path.getParentFile();
            if (parentFile.exists()) {
                if (path.isFile()) {
                    scanFileAsync(app, path);
                } else if (path.isDirectory()) {
                    scanDirAsync(app, path);
                }
            }
        }
    }

    /**
     * 获取文本内容
     *
     * @param msgFile 文件路径
     * @return 文本
     */
    public static String getFileContent(File msgFile) {

        if (msgFile == null || !msgFile.exists()) {
            return "";
        }
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(msgFile));

            StringBuilder sb = new StringBuilder();

            String s;
            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    /**
     * 保存文本,没有则创建,有则追加
     *
     * @param logPath 文件路径
     * @param msg     文本内容
     * @return 是否保存成功
     */
    public static boolean saveFileMsg(File logPath, String msg) {
        return saveFileMsg(logPath, msg, true);
    }

    /**
     * @param logPath 文件路径
     * @param msg     文本内容
     * @param append  是否追加
     * @return 是否保存成功
     */
    public static boolean saveFileMsg(File logPath, String msg, boolean append) {
        if (!createNullFile(logPath)) {
            return false;
        }

        FileWriter fw = null;
        try {
            fw = new FileWriter(logPath, append);
            fw.write(msg);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (fw != null) {
                try {
                    fw.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }


    public static boolean saveException(File logPath, String head, Throwable ex, boolean append) {
        return saveException(logPath, head, null, ex, append);
    }

    public static boolean saveException(File logPath, String head, String msg, boolean append) {
        return saveException(logPath, head, msg, null, append);
    }

    public static boolean saveException(File logPath, String head, String msg, Throwable ex, boolean append) {
        if (!createNullFile(logPath)) {
            return false;
        }
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileOutputStream(logPath, append));
            pw.println();
            if (head != null) {
                pw.println(head);
            }
            if (msg != null) {
                pw.println(msg);
            }
            if (ex != null) {
                ex.printStackTrace(pw);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (pw != null) {
                pw.flush();
                pw.close();
            }
        }
        return true;
    }

    /**
     * 创建空文件,上次目录没有的则也创建上级目录
     *
     * @param mPath 文件路径
     * @return 是否创建成功
     */
    public static boolean createNullFile(File mPath) {
        if (mPath == null) {
            return false;
        }
        boolean exit = mPath.exists();
        if (!exit) {
            File parentFile = mPath.getParentFile();
            boolean create = true;
            if (!parentFile.exists()) {
                create = parentFile.mkdirs();
            }
            if (create) {
                try {
                    exit = mPath.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return exit;
    }

    /**
     * 判断该文件是否超过指定大小
     *
     * @param mPath 文件路径
     * @param M     兆
     * @return 是否超过, 炒作指定大小则不可以追加.
     */
    public static boolean isAppend(File mPath, int M) {

        boolean append = true;

        if (mPath != null && mPath.exists()) {
            if ((mPath.length() / 1024 / 1024) > M) {
                append = false;
            }
        }

        return append;

    }

    /**
     * 同步文件到磁盘
     *
     * @param fd 文件描述
     * @return 是否同步成功
     */
    public static boolean syncFile(FileDescriptor fd) {
        if (fd == null) {
            return false;
        }

        try {
            if (fd.valid()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Os.fsync(fd);
                } else {
                    fd.sync();
                }
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 文件copy
     *
     * @param source 源文件
     * @param dest   目标文件
     */
    public static void copyFileUsingFileChannels(File source, File dest) {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputChannel != null) {
                try {
                    inputChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputChannel != null) {
                try {
                    outputChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 系统缓存文件路径
     *
     * @param mContext Context
     * @param fileName 文件名
     * @return 缓存文件路径
     */
    public static File getCacheFile(Context mContext, String fileName) {
        return new File(mContext.getCacheDir(), fileName);
    }

    /**
     * 清楚文件夹下所有文件
     */
    public static void deleteDirectoryFiles(File directory) {
        if (directory != null && directory.exists()) {
            if (directory.isDirectory()) {
                File[] files = directory.listFiles();
                for (File item : files) {
                    if (item.isDirectory()) {
                        deleteDirectoryFiles(item);
                    } else {
                        item.delete();
                    }
                }
            } else {
                directory.delete();
            }
        }
    }

    /**
     * 删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理
     */
    public static void deleteFiles(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            for (File item : files) {
                item.delete();
            }
        }
    }

}
