package cn.com.swain.baselib.log;

import java.io.File;

import cn.com.swain.baselib.log.impl.TlogImpl;
import cn.com.swain.baselib.log.logRecord.AbsLogRecord;

/**
 * author: Guoqiang_Sun
 * date : 2018/3/20 0020
 * desc :
 */

public class Tlog {

    private static final TlogImpl TlogImpl = new TlogImpl();

    private Tlog() {
    }

    //设置是否打印日志
    public static void setDebug(boolean flag) {
        TlogImpl.setDebug(flag);
    }

    //是否打印日志
    public static boolean isDebug() {
        return TlogImpl.isDebug();
    }


    //设置是否保存文件到日志
    public static void setLogRecordDebug(boolean flag) {
        TlogImpl.setLogRecordDebug(flag);
    }

    // 是否有录制实例
    public static boolean hasILogRecordImpl() {
        return TlogImpl.hasILogRecordImpl();
    }


    //是否开启了录制文件功能
    public static boolean isLogRecordDebug() {
        return TlogImpl.isLogRecordDebug();
    }

    //注册录制实例
    public static boolean set(AbsLogRecord recordMsg) {
        return TlogImpl.set(recordMsg);
    }

    //注册录制文件类
    public static boolean set(File logPath) {
        return TlogImpl.set(logPath);
    }

    //注销录制日志
    public static boolean remove(AbsLogRecord recordMsg) {
        return TlogImpl.remove(recordMsg);
    }

    //注销录制日志
    public static boolean remove() {
        return TlogImpl.remove();
    }

    //开始录制日志
    public static void startRecord() {
        TlogImpl.startRecord();
    }

    //停止录制日志
    public static void stopRecord() {
        TlogImpl.stopRecord();
    }

    // 强制停止录制
    public static void forceStopRecord() {
        TlogImpl.forceStopRecord();
    }

    //同步录制的日志到磁盘
    public static void syncRecordData() {
        TlogImpl.syncRecordData();
    }

    //是否正在录制log中
    public static boolean isRecording() {
        return TlogImpl.isRecording();
    }

    //设置是否打印堆栈消息
    public static void setPrintStackDebug(boolean flag) {
        TlogImpl.setPrintStackDebug(flag);
    }

    //是否开启打印堆栈消息功能
    public static boolean isPrintStackDebug() {
        return TlogImpl.isPrintStackDebug();
    }


    public static void v(String TAG, String msg) {
        TlogImpl.v(TAG, msg);
    }

    public static void v(String TAG, String msg, int l) {
        TlogImpl.v(TAG, msg, l);
    }

    public static void v(String TAG, String msg, Throwable e) {
        TlogImpl.v(TAG, msg, e);
    }

    public static void v(String TAG, Throwable e) {
        TlogImpl.v(TAG, e);
    }


    public static void d(String TAG, String msg) {
        TlogImpl.d(TAG, msg);
    }

    public static void d(String TAG, String msg, int l) {
        TlogImpl.d(TAG, msg, l);
    }

    public static void d(String TAG, String msg, Throwable e) {
        TlogImpl.d(TAG, msg, e);
    }

    public static void d(String TAG, Throwable e) {
        TlogImpl.d(TAG, e);
    }


    public static void i(String TAG, String msg) {
        TlogImpl.i(TAG, msg);
    }

    public static void i(String TAG, String msg, int l) {
        TlogImpl.i(TAG, msg, l);
    }

    public static void i(String TAG, String msg, Throwable e) {
        TlogImpl.i(TAG, msg, e);
    }

    public static void i(String TAG, Throwable e) {
        TlogImpl.i(TAG, e);
    }


    public static void w(String TAG, String msg) {
        TlogImpl.w(TAG, msg);
    }

    public static void w(String TAG, String msg, int l) {
        TlogImpl.w(TAG, msg, l);
    }

    public static void w(String TAG, String msg, Throwable e) {
        TlogImpl.w(TAG, msg, e);
    }

    public static void w(String TAG, Throwable e) {
        TlogImpl.w(TAG, e);
    }


    public static void e(String TAG, String msg) {
        TlogImpl.e(TAG, msg);
    }

    public static void e(String TAG, String msg, int l) {
        TlogImpl.e(TAG, msg, l);
    }

    public static void e(String TAG, String msg, Throwable e) {
        TlogImpl.e(TAG, msg, e);
    }

    public static void e(String TAG, Throwable e) {
        TlogImpl.e(TAG, e);
    }


    public static void a(String TAG, String msg) {
        TlogImpl.a(TAG, msg);
    }

    public static void a(String TAG, String msg, int l) {
        TlogImpl.a(TAG, msg, l);
    }

    public static void a(String TAG, String msg, Throwable e) {
        TlogImpl.a(TAG, msg, e);
    }

    public static void a(String TAG, Throwable e) {
        TlogImpl.a(TAG, e);
    }


    public static void p(String TAG, String msg) {
        TlogImpl.p(TAG, msg);
    }

    /********/

    public static void setGlobalTag(String TAG) {
        TlogImpl.setGlobalTag(TAG);
    }

    public static void v(String msg) {
        TlogImpl.v(msg);
    }

    public static void v(Throwable e) {
        TlogImpl.v(e);
    }

    public static void d(String msg) {
        TlogImpl.d(msg);
    }

    public static void d(Throwable e) {
        TlogImpl.d(e);
    }

    public static void i(String msg) {
        TlogImpl.i(msg);
    }

    public static void i(Throwable e) {
        TlogImpl.i(e);
    }

    public static void w(String msg) {
        TlogImpl.w(msg);
    }

    public static void w(Throwable e) {
        TlogImpl.w(e);
    }

    public static void e(String msg) {
        TlogImpl.e(msg);
    }

    public static void e(Throwable e) {
        TlogImpl.e(e);
    }

    public static void p(String msg) {
        TlogImpl.p(msg);
    }
}
