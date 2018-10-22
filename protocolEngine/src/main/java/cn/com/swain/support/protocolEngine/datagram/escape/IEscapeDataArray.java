package cn.com.swain.support.protocolEngine.datagram.escape;

/**
 * author: Guoqiang_Sun
 * date : 2018/3/30 0030
 * desc :
 */

public interface IEscapeDataArray extends ITransformInputData, ITransformOutputData {


    /**
     * 获取下标
     */
    int getPoint();

    /**
     * 容量
     *
     */
    int getCapacity();

    /**
     * 是转义状态
     *
     */
    boolean isEscapeState();

    /**
     * 是反转义状态
     *
     */
    boolean isReverseState();

    /**
     * 得到当前数据转义状态的字符串
     */
    String getStateStr();

    /**
     * 改变状态为转义状态
     * <p>
     * 当前内部数据正在转义数据
     */
    void changeStateToEscape();

    /**
     * 改变状态为反转义状态
     * <p>
     * 当前内部数据正在反转义数据
     */
    void changeStateToReverse();

    /**
     * 重置Point
     */
    void reset();

    /**
     * 数据置为0x00
     */
    void fillEmpty();

    /**
     * 清空数据
     */
    void release();

}
