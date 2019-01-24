package cn.com.swain.support.protocolEngine.datagram.ProtocolDatagram;


import cn.com.swain.support.protocolEngine.ProtocolBuild;
import cn.com.swain.support.protocolEngine.datagram.escape.IEscapeDataArray;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/1 0001
 * desc :
 */
public class ProtocolDataPackFactory {

    public static AbsProtocolDataPack generalQXSecureDataPack(int version) {

        if (version <= ProtocolBuild.VERSION.VERSION_0) {
            return generalQXNormalPack();
        } else if (version <= ProtocolBuild.VERSION.VERSION_SEQ) {
            return generalQXSeqPack();
        } else {
            return generalQXNormalPack();
        }

    }

    public static AbsProtocolDataPack generalQXSecureDataPack(int version, IEscapeDataArray mComDataArray) {

        if (version <= ProtocolBuild.VERSION.VERSION_0) {
            return generalQXNormalPack(mComDataArray);
        } else if (version <= ProtocolBuild.VERSION.VERSION_SEQ) {
            return generalQXSeqPack(mComDataArray);
        } else {
            return generalQXNormalPack(mComDataArray);
        }

    }

    /**
     * 默认包
     *
     * @return {@link AbsProtocolDataPack}
     */
    public static AbsProtocolDataPack generalQXNormalPack() {
        return generalQXNormalPack(null);
    }

    /**
     * 增加了序列号，版本号，保留了四个字节
     *
     * @return {@link AbsProtocolDataPack}
     */
    public static AbsProtocolDataPack generalQXSeqPack() {
        return generalQXSeqPack(null);
    }

    /**
     * 默认包
     *
     * @return {@link AbsProtocolDataPack}
     */
    public static AbsProtocolDataPack generalQXNormalPack(IEscapeDataArray mComDataArray) {
        return new QXProtocolComData(mComDataArray);
    }

    /**
     * 增加了序列号，版本号，保留了四个字节
     *
     * @return {@link AbsProtocolDataPack}
     */
    public static AbsProtocolDataPack generalQXSeqPack(IEscapeDataArray mComDataArray) {
        return new QXProtocolComData2(mComDataArray);
    }
}
