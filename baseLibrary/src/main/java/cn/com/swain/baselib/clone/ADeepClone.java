package cn.com.swain.baselib.clone;

import java.io.IOException;

/**
 * author: Guoqiang_Sun
 * date: 2019/3/8 0008
 * desc:
 */
public class ADeepClone implements IDeepCloneable {


    public ADeepClone() {

    }


    @Override
    public Object deepClone() throws DeepCloneException {

        try {
            String s = SerialManager.writeObject(this);
            return SerialManager.readObject(s);
        } catch (IOException e) {
            e.printStackTrace();
            throw new DeepCloneException(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new DeepCloneException(e);
        }
    }


}
