package cn.com.common.support.protocolEngine.IO;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/28 0028
 * desc :
 * <p>
 * 接受硬件信息输入数据实现转义并注册协议数据输出
 */
public interface IDataInputRegOutput extends IDataProtocolInput {

    public abstract void regIProtocolOutput(IDataProtocolOutput mResponse);

}
