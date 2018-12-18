package cn.com.swain.support.protocolEngine.pack;

import cn.com.swain.baselib.util.Bit;

/**
 * author: Guoqiang_Sun
 * date: 2018/11/13 0013
 * Desc:
 */
public class BaseModel {

    public static final int EMPTY = 0x00000000;

    private final Bit mBit;

    public BaseModel() {
        this(EMPTY);
    }

    public BaseModel(BaseModel model) {
        this(model != null ? model.getModel() : EMPTY);
    }

    public BaseModel(int model) {
        this.mBit = new Bit(model);
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
     * @param mModelNew {@link BaseModel}
     */
    public final void addModel(BaseModel mModelNew) {

        int modelNew = 0;

        if (mModelNew != null) {
            modelNew = mModelNew.getModel();
        }

        addDevice(modelNew);

    }

    /**
     * 设置模式
     *
     * @param mModelNew model
     */
    public final void addDevice(int mModelNew) {

        mBit.addDevice(mModelNew);

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
     * @param mModelNew {@link BaseModel}
     */
    public final void removeModel(BaseModel mModelNew) {

        int modelNew = 0;

        if (mModelNew != null) {
            modelNew = mModelNew.getModel();
        }

        removeDevice(modelNew);

    }

    /**
     * 设置模式
     *
     * @param mModelNew model
     */
    public final void removeDevice(int mModelNew) {

        mBit.removeDevice(mModelNew);

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
