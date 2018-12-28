package cn.com.common.test.java;

import java.io.File;

/**
 * author: Guoqiang_Sun
 * date: 2018/12/28 0028
 * Desc:
 */
public class Test {


    public static void main(String[] args) {

        File f = new File("D:\\AS_project\\startaiSocketProject\\blesocket\\build\\intermediates\\transforms\\dexBuilder");

        delete(f);

    }

    private static void delete(File f) {

        if (f.isDirectory()) {

            File[] files = f.listFiles();
            for (File file : files) {
                delete(file);
            }

        } else {
            if (f.exists()) {
                boolean delete = f.delete();
                if (delete) {
                    System.out.println(f.getAbsoluteFile() + " delete success");
                } else {
                    System.err.println(f.getAbsoluteFile() + " delete fail");
                }
            } else {
                System.err.println(f.getAbsoluteFile() + " not exit ");
            }
        }
    }

}
