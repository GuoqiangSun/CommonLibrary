package cn.com.swain.support.protocolEngine.resolve;

import cn.com.swain.support.protocolEngine.IO.IDataProtocolInput;

/**
 * author: Guoqiang_Sun
 * date: 2018/10/29 0029
 * Desc:
 */
public abstract class AbsProtocolProcessor implements IDataProtocolInput {

    public static final String TAG = "ProtocolProcessor";

    public abstract void release();

}
