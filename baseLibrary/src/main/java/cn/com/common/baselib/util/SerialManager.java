package cn.com.common.baselib.util;

import android.content.Context;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/21 0021
 * desc :
 */
public class SerialManager {

    // 序列化对象为String字符串，先对序列化后的结果进行BASE64编码，否则不能直接进行反序列化
    public static String writeObject(Object o) throws IOException {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(o);
        oos.flush();
        oos.close();
        bos.close();
        // return new BASE64Encoder().encode(bos.toByteArray());

        return new String(bos.toByteArray(), "ISO-8859-1");
    }

    // 反序列化String字符串为对象

    public static Object readObject(String object)
            throws StreamCorruptedException, IOException, ClassNotFoundException {
        // ByteArrayInputStream bis = new ByteArrayInputStream(new
        // BASE64Decoder().decodeBuffer(object));
        ByteArrayInputStream bis = new ByteArrayInputStream(object.getBytes("ISO-8859-1"));
        ObjectInputStream ois = new ObjectInputStream(bis);
        Object o = null;
        try {
            o = ois.readObject();
        } catch (EOFException e) {
        }
        bis.close();
        ois.close();
        return o;
    }

    private static final String OBJ_PATH = "serialObj";

    public static void saveObj(Context mContext, Object o) {

        saveObj(mContext, o, OBJ_PATH);

    }

    public static Object getObj(Context mContext) {

        return getObj(mContext, OBJ_PATH);

    }

    public static void saveObj(Context mContext, Object o, String path) {

        File cacheDir = mContext.getCacheDir();
        File serialDir = new File(cacheDir, path);

        saveObj(o, serialDir);

    }

    public static Object getObj(Context mContext, String path) {

        File cacheDir = mContext.getCacheDir();
        File serialDir = new File(cacheDir, path);

        return getObj(serialDir);

    }

    public static boolean saveObj(Object o, String path) {

        if (o == null || path == null) {
            return false;
        }

        return saveObj(o, new File(path));

    }

    public static boolean saveObj(Object o, File serialDir) {

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(serialDir);
            byte[] bytes = writeObject(o).getBytes();

            fos.write(bytes);

            fos.flush();

            return true;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (fos != null) {

                try {
                    fos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }

        return false;
    }

    public static Object getObj(String path) {

        if (path == null) {
            return null;
        }

        return getObj(new File(path));
    }

    public static Object getObj(File serialDir) {

        if (serialDir == null || !serialDir.exists()) {
            return null;
        }

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(serialDir);
            int available = fis.available();
            byte[] buf = new byte[available];
            fis.read(buf);

            return readObject(new String(buf));

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return null;

    }

}

