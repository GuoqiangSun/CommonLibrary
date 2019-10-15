package cn.com.swain.comtrade.test;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import cn.com.swain.comtrade.CfgWorker;
import cn.com.swain.comtrade.ComtradeWorker;
import cn.com.swain.comtrade.DatWorker;
import cn.com.swain.comtrade.cfg.ComtradeConfig;
import cn.com.swain.comtrade.dat.ComtradeChannelData;
import cn.com.swain.comtrade.exception.ComtradeNullException;
import cn.com.swain.comtrade.utils.ComtradeInfo;

/**
 * author Guoqiang_Sun
 * date 2019/9/4
 * desc
 */
public class JavaTest {

    public static void main(String[] args) {

//        readWriteCfg();

//        convertB2A();

//        readChannelDat();

//        analogDatToCsv();

        analogIndexDatToCsv();

//        strEnc();

        System.out.println("end");
    }

    private static String path = "D:\\国网电力\\5种典型波形";
    private static String name = "2017_03_12_16_27_20_000";
//    private static String name = "不接地系统-金属性接地-峰值时刻短路";
//    private static String name = "消弧线圈接地系统-200欧电阻接地-45°时刻短路";

    private static String fileNameCfg = name + ".cfg";
    private static String fileNameDat = name + ".dat";

    // 读写配置文件
    private static void readWriteCfg() {
        String fileNameABC = name + "_ASCII.cfg";
        try {
            CfgWorker worker = new CfgWorker();
            ComtradeConfig config = worker.read(new File(path, fileNameCfg));
            System.out.println(String.valueOf(config));
            config.ft = ComtradeInfo.ASCII;
            worker.write(config, new File(path, fileNameABC));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 二进制数据传ASCII数据
    private static void convertB2A() {
        String outNameDatAscii = name + "_ASCII.dat";
        try {
            ComtradeWorker worker = new ComtradeWorker();
            worker.Binary2ASCII(new File(path, fileNameCfg),
                    new File(path, fileNameDat)
                    , new File(path, outNameDatAscii));
        } catch (ComtradeNullException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 读通道数据
    private static void readChannelDat() {

        try {
            CfgWorker mCfgWorker = new CfgWorker();
            ComtradeConfig config = mCfgWorker.read(new File(path, fileNameCfg));

            DatWorker mDatWorker = new DatWorker();
            ComtradeChannelData mChannelData = mDatWorker.readBinaryChannelDat(config,
                    new File(path, fileNameDat));
            System.out.println(mChannelData);

        } catch (ComtradeNullException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 模拟通道数据传CSV
    private static void analogDatToCsv() {
        try {
            ComtradeWorker worker = new ComtradeWorker();
            String csvNameDat = name + ".csv";
            worker.binaryAnalogDatToCsv(path, fileNameCfg, fileNameDat, csvNameDat);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 模拟通道数据传CSV
    private static void analogIndexDatToCsv() {
        try {
            ComtradeWorker worker = new ComtradeWorker();
            ComtradeConfig config = worker.getConfigWorker().read(new File(path, fileNameCfg));
            System.out.print(config);
            ComtradeChannelData channelData =
                    worker.getDatWorker().readBinaryChannelDat(config, new File(path, fileNameDat));
            ByteArrayOutputStream byteArrayOutputStream = worker.analogDatRationToCsv(config, channelData, 0);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            String s = new String(bytes);
            System.out.println(s);

            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(path, "tmp.csv")));
            bw.write(s);
            bw.flush();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void strEnc() {
        String station = "1-交流电压180V 1#Ua";
        printEncStrBytes(station);
    }

    public static void printEncStrBytes(String station) {

        System.out.println(station);

        byte[] bytes = station.getBytes();
        System.out.print("getBytes::");
        for (byte b : bytes) {
            System.out.print(Integer.toHexString(b & 0xFF) + ",");
        }
        System.out.println();

        try {
            bytes = station.getBytes("UTF-8");
            System.out.print("getBytes(UTF-8)::");
            for (byte b : bytes) {
                System.out.print(Integer.toHexString(b & 0xFF) + ",");
            }
            System.out.println();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            bytes = station.getBytes("GBK");
            System.out.print("getBytes(GBK)::");
            for (byte b : bytes) {
                System.out.print(Integer.toHexString(b & 0xFF) + ",");
            }
            System.out.println();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            bytes = station.getBytes("ISO-8859-1");
            System.out.print("getBytes(ISO-8859-1)::");
            for (byte b : bytes) {
                System.out.print(Integer.toHexString(b & 0xFF) + ",");
            }
            System.out.println();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
