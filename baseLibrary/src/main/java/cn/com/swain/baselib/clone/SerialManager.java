package cn.com.swain.baselib.clone;

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
import java.io.Serializable;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/21 0021
 * desc :
 */
public class SerialManager {

    // 序列化对象为String字符串，先对序列化后的结果进行BASE64编码，否则不能直接进行反序列化
    // 如果是内部类，需要是静态内部类public static class
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
            throws IOException, ClassNotFoundException {
        // ByteArrayInputStream bis = new ByteArrayInputStream(new
        // BASE64Decoder().decodeBuffer(object));
        ByteArrayInputStream bis = new ByteArrayInputStream(object.getBytes("ISO-8859-1"));
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


    public static void saveObj(Context mContext, Object o, String path) {

        File cacheDir = mContext.getCacheDir();
        File serialDir = new File(cacheDir, path);

        saveObj(o, serialDir);

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

    public static Object getObj(Context mContext, String path) {

        File cacheDir = mContext.getCacheDir();
        File serialDir = new File(cacheDir, path);

        return getObj(serialDir);

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

        public String name;
        public int age;

        @Override
        public String toString() {
            return "people--name:" + name + " age:" + age;
        }
    }

    // 单例序列化
    private static class Singleton implements Serializable {

        /**
         * 去掉以后,反序列化又会生成一个实例
         */
        private Object readResolve() {
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

