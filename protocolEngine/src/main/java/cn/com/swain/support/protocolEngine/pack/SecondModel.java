package cn.com.swain.support.protocolEngine.pack;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/14 0014
 * desc : 发送类型的数据结构
 */
public class SecondModel extends ComModel {

    public SecondModel() {
        super();
    }

    public SecondModel(BaseModel model) {
        super(model);
    }

    public SecondModel(int model) {
        super(model);
    }

    /**
     * 二级接收模式
     * <p>
     * 比如说 bit0 is port = 1000 ,bit1 is port = 10001
     */
    private BaseModel mSecondModel;

    /**
     * 设置二级接收模式
     */
    public void setSecondMOdel(BaseModel mSecondModel) {
        this.mSecondModel = mSecondModel;
    }

    /**
     * 获取二级接收模式
     */
    public BaseModel getSecondModel() {
        return mSecondModel;
    }

    @Override
    public String toString() {
        return super.toString() + " SecondModel:" + String.valueOf(mSecondModel);
    }
}
