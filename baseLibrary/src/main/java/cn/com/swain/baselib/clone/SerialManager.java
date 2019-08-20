package cn.com.swain.baselib.clone;

import android.content.Context;
import android.os.Build;

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
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

import cn.com.swain.baselib.file.FileUtil;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/21 0021
 * desc :
 */
public class SerialManager {

    // 序列化对象为String字符串
    // 如果是内部类，需要是静态内部类public static class
    public static String writeObject(Object o) throws IOException {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(o);
        oos.flush();
        oos.close();
        bos.close();
        // return new BASE64Encoder().encode(bos.toByteArray());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return new String(bos.toByteArray(), StandardCharsets.ISO_8859_1);
        } else {
            return new String(bos.toByteArray(), charsetName);
        }
    }

    private static final String charsetName = "ISO-8859-1";

    // 反序列化String字符串为对象
    public static Object readObject(String object)
            throws IOException, ClassNotFoundException {
        // ByteArrayInputStream bis = new ByteArrayInputStream(new
        // BASE64Decoder().decodeBuffer(object));

        byte[] bytes;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            bytes = object.getBytes(StandardCharsets.ISO_8859_1);
        } else {
            bytes = object.getBytes(charsetName);
        }

        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bis);
        Object o = null;
        try {
            o = ois.readObject();
        } catch (EOFException e) {
            e.printStackTrace();
        }
        bis.close();
        ois.close();
        return o;
    }

    /**
     * 对象流保存在context.cache 目录下
     *
     * @param o        对象
     * @param mContext Context
     * @param fileName 文件名称
     * @return 是否保存成功
     */
    public static boolean saveObj(Object o, Context mContext, String fileName) {
        return saveObj(o, FileUtil.getCacheFile(mContext, fileName));
    }

    /**
     * 对象流保存在path文件
     *
     * @param o    对象
     * @param path 文件名称
     * @return 是否保持成功
     */
    public static boolean saveObj(Object o, String path) {

        if (o == null || path == null) {
            return false;
        }

        return saveObj(o, new File(path));

    }

    /**
     * 对象流保存在path文件
     *
     * @param o          对象
     * @param serialPath 文件
     * @return 是否保持成功
     */
    public static boolean saveObj(Object o, File serialPath) {

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(serialPath);
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

    /**
     * context.cache 目录下的 fileName 获取对象
     *
     * @param mContext Context
     * @param fileName 文件名称
     * @return Object
     */
    public static Object getObj(Context mContext, String fileName) {
        return getObj(FileUtil.getCacheFile(mContext, fileName));
    }

    /**
     * path文件下获取对象
     *
     * @param path 文件名称
     * @return Object
     */
    public static Object getObj(String path) {

        if (path == null) {
            return null;
        }

        return getObj(new File(path));
    }

    /**
     * serialPath文件下获取对象
     *
     * @param serialPath 文件
     * @return Object
     */
    public static Object getObj(File serialPath) {

        if (serialPath == null || !serialPath.exists()) {
            return null;
        }

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(serialPath);
            int available = fis.available();
            byte[] buf = new byte[available];
            int read = fis.read(buf);
            if (read > 0) {
                return readObject(new String(buf));
            }
            return null;
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

    /****************TEST*******************/

    public static void main(String[] args) {

        String s = "123";
        writeObjReadObj(s);

        Integer sI = 123;
        writeObjReadObj(sI);

        int si = 123;
        writeObjReadObj(si);

        People p = new People("xiaosan", 23);
        writeObjReadObj(p);

        Singleton singleton = Singleton.getSingleton();
        singleton.setName("swain");
        writeObjReadObj(singleton);

    }

    private static void writeObjReadObj(Object o) {
        try {
            System.out.println();
            System.out.println(" before serial : " + String.valueOf(o));
            String s1 = writeObject(o);
            Object o1 = readObject(s1);
            System.out.println(" after  serial : " + String.valueOf(o1));

            System.out.println(" before serial Object == after serial Object ? " + (o == o1));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static class People implements Serializable {
        public People() {
        }

        public People(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public transient String name;
        public int age;

        private void writeObject(java.io.ObjectOutputStream out)
                throws IOException {
            System.out.println(" Serializable writeObject ");
//            out.defaultWriteObject();
        }

        private void readObject(java.io.ObjectInputStream in)
                throws IOException, ClassNotFoundException {
            System.out.println(" Serializable readObject ");
//            in.defaultReadObject();
        }

        private void readObjectNoData()
                throws ObjectStreamException {
            System.out.println(" Serializable readObjectNoData ");
        }


        @Override
        public String toString() {
            return "people--name:" + name + " age:" + age + " h:" + hashCode();
        }
    }

    // 单例序列化
    private static class Singleton implements Serializable {

        /**
         * 去掉以后,反序列化又会生成一个实例
         */
        private Object readResolve() {
            System.out.println(" Serializable readResolve ");
            return singleton;
        }

        private volatile static Singleton singleton;

        private Singleton() {
        }

        private static Singleton getSingleton() {
            if (singleton == null) {
                synchronized (Singleton.class) {
                    if (singleton == null) {
                        singleton = new Singleton();
                    }
                }
            }
            return singleton;
        }

        private String name;

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Singleton--name:" + name;
        }
    }

}

