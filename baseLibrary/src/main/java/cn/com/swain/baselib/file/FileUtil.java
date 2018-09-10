package cn.com.swain.baselib.file;

import android.os.Build;
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

    public static boolean saveFileMsg(File logPath, String msg) {
        return saveFileMsg(logPath, msg, true);
    }


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
            if (ex != null) {
                ex.printStackTrace(pw);
            } else {
                pw.println(" Throwable is null ");
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

    public static boolean saveException(File logPath, String head, String msg, boolean append) {

        if (!createNullFile(logPath)) {
            return false;
        }

        FileWriter fw = null;
        try {
            fw = new FileWriter(logPath, append);
            fw.write("\n");
            if (head != null) {
                fw.write(head);
                fw.write("\n");
            }
            if (msg != null) {
                fw.write(msg);
            } else {
                fw.write(" Throwable is null ");
            }
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


    public static boolean isAppend(File mPath, int M) {

        boolean append = true;

        if (mPath != null && mPath.exists()) {
            if ((mPath.length() / 1024 / 1024) > M) {
                append = false;
            }
        }

        return append;

    }

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

}
