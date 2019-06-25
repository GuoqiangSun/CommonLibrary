package cn.com.common.test.java;

import java.io.File;

import cn.com.swain.baselib.util.StrUtil;

/**
 * author: Guoqiang_Sun
 * date: 2018/12/28 0028
 * Desc:
 */
public class Test {


    public static void main(String[] args) {

//        File f = new File("D:\\AS_project\\startaiSocketProject\\blesocket\\build\\intermediates\\transforms\\dexBuilder");
//
//        delete(f);

        int l = 25;
        byte[] buf = new byte[l];
        for (int i = 0; i < l; i++) {
            buf[i] = (byte) i;
        }
        resolveData(buf, 1);

    }

    private static final int mResolveLength = 18;

    private static void resolveData(byte[] mData, long delay) {

        if (mData == null) {
            return;
        }

        final int length = mData.length;

        // Tlog.d(TAG, " resolveData length : " + length);

        if (length > mResolveLength) {

            int l = length % mResolveLength;
            int d = length / mResolveLength;
            if (l != 0) {
                d += 1;
            }
            int onePkgLength = length / d;
            int end = d-1;

            for (int i = 0; i < d; i++) {

                byte[] buf;

                if (i == end) {
                    int len = i * onePkgLength;
                    int realLen = length - len;

                    buf = new byte[realLen];
                    System.arraycopy(mData, i * onePkgLength, buf, 0, realLen);

                } else {
                    buf = new byte[onePkgLength];
                    System.arraycopy(mData, i * onePkgLength, buf, 0, onePkgLength);

                }

                writeData(buf);

                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {

            writeData(mData);
        }
    }

    private static void writeData(byte[] data) {
        System.out.println(" writeData : " + StrUtil.toHexString(data));
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
