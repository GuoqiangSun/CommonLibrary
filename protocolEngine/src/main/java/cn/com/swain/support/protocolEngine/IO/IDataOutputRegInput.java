package cn.com.swain.support.protocolEngine.IO;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/28 0028
 * desc :
 * <p>
 * * 接受协议数据输出并注册协议数据转义输入
 */
public interface IDataOutputRegInput extends IDataProtocolOutput {

    public abstract void regIProtocolInput(IDataProtocolInput mReceives);

}
