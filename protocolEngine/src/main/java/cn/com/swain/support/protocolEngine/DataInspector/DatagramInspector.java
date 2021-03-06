package cn.com.swain.support.protocolEngine.DataInspector;

import cn.com.swain.baselib.util.CrcUtil;
import cn.com.swain.support.protocolEngine.ProtocolBuild;
import cn.com.swain.support.protocolEngine.datagram.SocketDataArray;
import cn.com.swain.support.protocolEngine.resolve.AbsProtocolProcessor;
import cn.com.swain.baselib.log.Tlog;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/2 0002
 * desc :数据包质检员
 */

public class DatagramInspector {


    public DatagramInspector() {

    }

    public DatagramInspector(SocketDataArray mSocketDataArray) {
        this.mSocketDataArray = mSocketDataArray;
    }

    private SocketDataArray mSocketDataArray;

    public void setSocketDataArray(SocketDataArray mSocketDataArray) {
        this.mSocketDataArray = mSocketDataArray;
    }

    public void clearCache() {
        this.mSocketDataArray = null;
    }

    /****************************/

    public boolean isNull() {
        return (mSocketDataArray == null);
    }


    // 1 . 校验头

    public boolean hasHead() {
        byte protocolHead = mSocketDataArray.getProtocolHead();

        return (protocolHead == ProtocolBuild.XX.STX);

    }

    // 校验crc
    public boolean checkCrc() {

        byte[] checkField;
        try {
            checkField = mSocketDataArray.getProtocolNeedCheckData();

        } catch (Exception e) {

            Tlog.e(AbsProtocolProcessor.TAG, " checkProtocolCrc() Exception:", e);

            return false;
        }

        if (checkField == null) {
            return false;
        }

        byte b = CrcUtil.CRC8(checkField);

        byte protocolCrc8 = mSocketDataArray.getProtocolCrc8();

//        Tlog.e(ProtocolProcessor.TAG, Integer.toHexString(protocolCrc8) + " " + Integer.toHexString(b) + " " + StrUtil.toString(checkField));

        return (b == protocolCrc8);
    }

    // 校验尾
    public boolean hasTail() {

        byte protocolTail = mSocketDataArray.getProtocolTail();

        return (protocolTail == ProtocolBuild.XX.ETX);

    }

}
