package cn.com.swain.baselib.log.logRecord;

/**
 * author: Guoqiang_Sun
 * date : 2018/10/12 0012
 * desc :
 */
public interface ILogFile {

    /**
     * 检测是否正在录制
     *
     * @return true or false
     */
    boolean isRecording();

    /**
     * 开启录制
     */
    void startRecord();

    /**
     * 同步录制的数据到磁盘
     */
    void syncRecordData();

    /**
     * 停止录制
     */
    void stopRecord();

    /**
     * 是否已经初始化了
     *
     * @return true or false
     */
    boolean isInit();

    /**
     * 初始化
     */
    void initLogFile();

    /**
     * 释放资源
     */
    void releaseLogFile();

}
