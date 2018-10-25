package cn.com.swain.support.protocolEngine.datagram.escape;

/**
 * author: Guoqiang_Sun
 * date : 2018/3/30 0030
 * desc :
 */

public interface ITransformOutputData {

    /**
     * 获取指定下标的数据
     *
     * @param index
     */
    byte getByte(int index);

    /**
     * copy一份添加进来后经过转义的数据
     *
     */
    byte[] toArray();

    /**
     * copy一份添加进来后经过转义的数据
     *
     */
    byte[] toArray(int srcPoint, int length) ;

    /**
     * copy一份添加进来后经过转义的数据
     */
    boolean copyArray(byte[] data);

    /**
     * copy一份添加进来后经过转义的数据
     */
    boolean copyArray(byte[] data, int srcPoint, int length);


}
