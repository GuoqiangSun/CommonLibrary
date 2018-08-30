package cn.com.common.support.protocolEngine.pack;

import cn.com.common.baselib.util.StrUtil;
import cn.com.common.support.protocolEngine.Repeat.RepeatMsgModel;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/2 0002
 * desc :
 */

public class ResponseData {

    public ResponseData() {
    }

    public ResponseData(String ID, byte[] data) {
        this.toID = ID;
        this.data = data;
    }

    /**
     * 基础数据结构
     */

    public byte[] data;

    public String toID;

    public Object obj;

    public int arg;

    /**
     * 发送模式
     */

    private final SendModel mSendModel = new SendModel();

    public SendModel getSendModel() {
        return mSendModel;
    }


    /**
     * 重发的数据结构
     */

    private final RepeatMsgModel mRepeatMsgModel = new RepeatMsgModel();

    public RepeatMsgModel getRepeatMsgModel() {
        return mRepeatMsgModel;
    }

    @Override
    public String toString() {

        return baseDataToString()
                + " sendModel:" + mSendModel.toString()
                + "; RepeatMsgModel:" + mRepeatMsgModel.toString();

    }

    private String baseDataToString() {
        return " obj:" + String.valueOf(obj) + " arg:" + arg + " toID: " + toID + "; " + StrUtil.toString(data);
    }

}
