package cn.com.swain.support.protocolEngine.datagram.ProtocolDatagram;


import cn.com.swain.support.protocolEngine.ProtocolBuild;
import cn.com.swain.support.protocolEngine.datagram.escape.IEscapeDataArray;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/1 0001
 * desc :
 */
public class ProtocolDataPackFactory {

    public static AbsProtocolDataPack generalSecureDataPack(int version) {

        if (version <= ProtocolBuild.VERSION.VERSION_0) {
            return generalNormalPack();
        } else if (version <= ProtocolBuild.VERSION.VERSION_SEQ) {
            return generalSeqPack();
        } else {
            return generalNormalPack();
        }

    }

    public static AbsProtocolDataPack generalSecureDataPack(int version, IEscapeDataArray mComDataArray) {

        if (version <= ProtocolBuild.VERSION.VERSION_0) {
            return generalNormalPack(mComDataArray);
        } else if (version <= ProtocolBuild.VERSION.VERSION_SEQ) {
            return generalSeqPack(mComDataArray);
        } else {
            return generalNormalPack(mComDataArray);
        }

    }

    /**
     * 默认包
     *
     * @return
     */
    public static AbsProtocolDataPack generalNormalPack() {
        return generalNormalPack(null);
    }

    /**
     * 增加了序列号，版本号，保留了四个字节
     *
     * @return
     */
    public static AbsProtocolDataPack generalSeqPack() {
        return generalSeqPack(null);
    }

    /**
     * 默认包
     *
     * @return
     */
    public static AbsProtocolDataPack generalNormalPack(IEscapeDataArray mComDataArray) {
        return new ProtocolComData(mComDataArray);
    }

    /**
     * 增加了序列号，版本号，保留了四个字节
     *
     * @return
     */
    public static AbsProtocolDataPack generalSeqPack(IEscapeDataArray mComDataArray) {
        return new ProtocolComData2(mComDataArray);
    }
}
