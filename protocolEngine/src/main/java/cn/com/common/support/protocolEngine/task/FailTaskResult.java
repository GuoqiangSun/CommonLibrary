package cn.com.common.support.protocolEngine.task;

import cn.com.common.baselib.util.StrUtil;

/**
 * author: Guoqiang_Sun
 * date : 2018/5/17 0017
 * desc :
 */
public class FailTaskResult {

    public int errorCode;
    public String mac;
    public int type;
    public int cmd;
    public byte[] data;
    public String description;

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder(128);
        sb.append("errorCode:");
        sb.append(String.valueOf(errorCode));
        sb.append(", mac:");
        sb.append(String.valueOf(mac));
        sb.append(", type:");
        sb.append(String.valueOf(type));
        sb.append(", cmd:");
        sb.append(String.valueOf(cmd));
        sb.append(", description:");
        sb.append(String.valueOf(description));
        sb.append(", data:");
        sb.append(StrUtil.toString(data));
        return sb.toString();
    }
}
