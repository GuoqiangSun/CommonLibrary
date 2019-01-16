package cn.com.swain.baselib.log;

import java.io.File;

import cn.com.swain.baselib.log.impl.TFlogImpl;
import cn.com.swain.baselib.log.logRecord.AbsLogRecord;

/**
 * author: Guoqiang_Sun
 * date : 2018/9/12 0012
 * desc : 文件录制
 */
public class TFlog {

    private static final TFlogImpl TFlogImpl = new TFlogImpl();

    private TFlog() {
    }

    // 设置录制实例
    public static boolean set(File logPath) {
        return TFlogImpl.set(logPath);
    }

    // 设置录制实例
    public static boolean set(AbsLogRecord recordMsg) {
        return TFlogImpl.set(recordMsg);
    }

    // 移除录制实例
    public static boolean remove(AbsLogRecord recordMsg) {
        return TFlogImpl.remove(recordMsg);
    }

    // 移除录制实例
    public static boolean remove() {
        return TFlogImpl.remove();
    }

    // 是否有录制实例
    public static boolean hasILogRecordImpl() {
        return TFlogImpl.hasILogRecordImpl();
    }

    // 是否正在录制log中
    public static boolean isRecording() {
        return TFlogImpl.isRecording();
    }

    // 开始录制
    public static void startRecord() {
        TFlogImpl.startRecord();
    }

    // 开启录制调用的次数
    public static int getStartTimes() {
        return TFlogImpl.getStartTimes();
    }

    //同步数据到磁盘
    public static void syncRecordData() {
        TFlogImpl.syncRecordData();
    }

    // 停止录制
    public static void stopRecord() {
        TFlogImpl.stopRecord();
    }

    public static void v(String TAG, String msg) {
        TFlogImpl.v(TAG, msg);
    }

    public static void v(String TAG, String msg, Throwable e) {
        TFlogImpl.v(TAG, msg, e);
    }

    public static void v(String TAG, Throwable e) {
        TFlogImpl.v(TAG, e);
    }

    public static void d(String TAG, String msg) {
        TFlogImpl.d(TAG, msg);
    }

    public static void d(String TAG, String msg, Throwable e) {
        TFlogImpl.d(TAG, msg, e);
    }

    public static void d(String TAG, Throwable e) {
        TFlogImpl.d(TAG, e);
    }

    public static void i(String TAG, String msg) {
        TFlogImpl.i(TAG, msg);
    }

    public static void i(String TAG, String msg, Throwable e) {
        TFlogImpl.i(TAG, msg, e);
    }

    public static void i(String TAG, Throwable e) {
        TFlogImpl.i(TAG, e);
    }

    public static void w(String TAG, String msg) {
        TFlogImpl.w(TAG, msg);
    }

    public static void w(String TAG, String msg, Throwable e) {
        TFlogImpl.w(TAG, msg, e);
    }

    public static void w(String TAG, Throwable e) {
        TFlogImpl.w(TAG, e);
    }

    public static void e(String TAG, String msg) {
        TFlogImpl.e(TAG, msg);
    }

    public static void e(String TAG, String msg, Throwable e) {
        TFlogImpl.e(TAG, msg, e);
    }

    public static void e(String TAG, Throwable e) {
        TFlogImpl.e(TAG, e);
    }

    public static void a(String TAG, String msg) {
        TFlogImpl.a(TAG, msg);
    }

    public static void a(String TAG, Throwable e) {
        TFlogImpl.a(TAG, e);
    }

    public static void a(String TAG, String msg, Throwable e) {
        TFlogImpl.a(TAG, msg, e);
    }
}
