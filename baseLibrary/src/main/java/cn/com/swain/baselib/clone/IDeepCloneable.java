package cn.com.swain.baselib.clone;

import java.io.Serializable;

/**
 * author: Guoqiang_Sun
 * date: 2019/3/8 0008
 * desc:
 */
public interface IDeepCloneable extends Serializable {

    /**
     * 深度copy
     *
     * @return Object
     */
    Object deepClone() throws DeepCloneException;


}
