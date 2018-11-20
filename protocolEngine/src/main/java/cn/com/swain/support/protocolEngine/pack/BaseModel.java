package cn.com.swain.support.protocolEngine.pack;

import cn.com.swain.baselib.util.Bit;

/**
 * author: Guoqiang_Sun
 * date: 2018/11/13 0013
 * Desc:
 */
public class BaseModel {

    public static final int SEND_MODEL_CASUAL = 0x00;

    private final Bit mBit;

    public BaseModel() {
        this(SEND_MODEL_CASUAL);
    }

    public BaseModel(BaseModel model) {
        this(model != null ? model.getModel() : SEND_MODEL_CASUAL);
    }

    public BaseModel(int model) {
        this.mBit = new Bit(model);
    }

    /**
     * 设置随意模式
     */
    public void setModelCasual() {
        mBit.addDevice(SEND_MODEL_CASUAL);
    }

    /**
     * 是否随意通信模式
     */
    public boolean isModelCasual() {
        return getModel() == SEND_MODEL_CASUAL;
    }

    /**
     * 设置模式
     *
     * @param bitPoint bit位
     */
    public final void setModel(int bitPoint) {
        mBit.add(bitPoint);
    }

    /**
     * 设置模式
     *
     * @param mModelNew model
     */
    public void addDevice(int mModelNew) {

        mBit.addDevice(mModelNew);

    }

    /**
     * 设置模式
     *
     * @param mModelNew {@link BaseModel}
     */
    public void addDevice(BaseModel mModelNew) {

        int modelNew = 0;

        if (mModelNew != null) {
            modelNew = mModelNew.getModel();
        }

        mBit.addDevice(modelNew);

    }

    /**
     * 移除模式
     *
     * @param bitPoint bit位
     */
    public final void removeModel(int bitPoint) {
        mBit.remove(bitPoint);
    }

    /**
     * 设置模式
     *
     * @param mModelNew model
     */
    public void removeDevice(int mModelNew) {

        mBit.removeDevice(mModelNew);

    }

    /**
     * 设置模式
     *
     * @param mModelNew {@link BaseModel}
     */
    public void removeDevice(BaseModel mModelNew) {

        int modelNew = 0;

        if (mModelNew != null) {
            modelNew = mModelNew.getModel();
        }

        mBit.removeDevice(modelNew);

    }

    /**
     * 获取模式
     *
     * @return 模式
     */
    public final int getModel() {
        return mBit.getDevice();
    }


    /**
     * 重置模式
     */
    public final void fillEmpty() {
        mBit.fillEmpty();
    }

    @Override
    public String toString() {
        return Integer.toBinaryString(this.mBit.getDevice());
    }

}
