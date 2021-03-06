package cn.com.swain.support.protocolEngine.pack;

import cn.com.swain.baselib.util.StrUtil;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/2 0002
 * desc :
 */

public class ReceivesData {

    public ReceivesData() {
    }

    public ReceivesData(String ID, byte[] data) {
        this.fromID = ID;
        this.data = data;
    }

    public byte[] data;

    public String fromID;

    public int arg;

    public Object obj;

    /**
     * 接收模式
     */
    private final SecondModel mReceiveModel = new SecondModel();

    public SecondModel getReceiveModel() {
        return mReceiveModel;
    }


    @Override
    public String toString() {

        return baseDataToString()
                + " ReceiveModel:" + String.valueOf(mReceiveModel);
    }

    private String baseDataToString() {
        return " obj:" + String.valueOf(obj) + " arg:" + arg + " fromID: " + fromID + "; " + StrUtil.toString(data);
    }

}
