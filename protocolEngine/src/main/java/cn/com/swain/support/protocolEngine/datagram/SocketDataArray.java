package cn.com.swain.support.protocolEngine.datagram;

import cn.com.swain.support.protocolEngine.ProtocolBuild;
import cn.com.swain.support.protocolEngine.ProtocolCode;
import cn.com.swain.support.protocolEngine.ProtocolProcessor;
import cn.com.swain.support.protocolEngine.datagram.ProtocolDatagram.AbsProtocolDataPack;
import cn.com.swain.support.protocolEngine.datagram.ProtocolDatagram.IProtocolComData;
import cn.com.swain.support.protocolEngine.datagram.ProtocolDatagram.ProtocolDataPackFactory;
import cn.com.swain.support.protocolEngine.datagram.ProtocolException.DatagramStateException;
import cn.com.swain.support.protocolEngine.datagram.ProtocolException.EscapeIOException;
import cn.com.swain.support.protocolEngine.datagram.escape.EscapeDataArray;
import cn.com.swain.support.protocolEngine.datagram.escape.IEscapeDataArray;
import cn.com.swain.support.protocolEngine.pack.BaseModel;
import cn.com.swain.support.protocolEngine.pack.ComModel;
import cn.com.swain169.log.Tlog;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/1 0016
 * desc :
 */
public class SocketDataArray extends AbsProtocolDataPack implements Cloneable, IProtocolComData, IEscapeDataArray {

    private final IEscapeDataArray mEscapeDataArray;
    private final IProtocolComData mProtocolComData;
    private final AbsProtocolDataPack mAbsProtocolDataPack;

    private final String TAG = ProtocolProcessor.TAG;

    private static final int DATA_BODY = 12;


    public SocketDataArray(int version) {

        Tlog.v(TAG, " new SocketDataArray() version:" + version);
        int body = version <= ProtocolBuild.VERSION.VERSION_0 ? DATA_BODY : DATA_BODY + 6;
        this.mEscapeDataArray = new EscapeDataArray(body);
        this.mAbsProtocolDataPack = ProtocolDataPackFactory.generalSecureDataPack(version, this.mEscapeDataArray);
        this.mProtocolComData = this.mAbsProtocolDataPack;
    }


    /**
     * 是否收到一包完整的数据包
     * 只要收到包头，包尾就算受到完整的数据包
     */
    private boolean isCompletePkg;

    public boolean isCompletePkg() {
        return isCompletePkg;
    }

    public void setIsCompletePkg() {
        this.isCompletePkg = true;
    }

    public void resetIsCompletePkg() {
        this.isCompletePkg = false;
    }

    /**
     * 是否正在被使用
     */
    private volatile boolean used;

    public final void setISUsed() {
        this.used = true;
    }

    public final void setISUnUsed() {
        this.used = false;
    }

    public final boolean isUsed() {
        return this.used;
    }

    /**
     * 哪个客户端调用的
     */
    private String ID;

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return this.ID;
    }


    /**
     * cache
     */
    private Object obj;

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public Object getObj() {
        return this.obj;
    }

    /**
     * arg
     */
    private int arg;

    public void setArg(int arg) {
        this.arg = arg;
    }

    public int getArg() {
        return this.arg;
    }

    /**
     *
     */
    private ComModel mModel;

    public void clearModel() {
        if (mModel != null) {
            mModel.fillEmpty();
        }
    }

    public void setModel(BaseModel mModelNew) {
        if (mModel == null) {
            mModel = new ComModel(mModelNew);
        }
        this.mModel.addDevice(mModelNew);
    }

    /****************/

    private void checkStateIsReverse() {
        if (!mEscapeDataArray.isReverseState()) {
            throw new DatagramStateException(" ! mEscapeDataArray.isReverseState() , state is " + mEscapeDataArray.getStateStr());
        }
    }


    @Override
    public byte getProtocolHead() {
        checkStateIsReverse();
        return mProtocolComData.getProtocolHead();
    }

    @Override
    public byte getProtocolTail() {
        checkStateIsReverse();
        return mProtocolComData.getProtocolTail();
    }

    @Override
    public int getProtocolVersion() {
        checkStateIsReverse();
        return mProtocolComData.getProtocolVersion();
    }

    @Override
    public int getProtocolSequence() {
        checkStateIsReverse();
        return mProtocolComData.getProtocolSequence();
    }

    @Override
    public int getProtocolToken(int point) {
        checkStateIsReverse();
        return mProtocolComData.getProtocolToken(point);
    }

    @Override
    public int getProtocolToken() {
        checkStateIsReverse();
        return mProtocolComData.getProtocolToken();
    }

    @Override
    public int getProtocolCustom() {
        checkStateIsReverse();
        return mProtocolComData.getProtocolCustom();
    }

    @Override
    public int getProtocolProduct() {
        checkStateIsReverse();
        return mProtocolComData.getProtocolProduct();
    }


    @Override
    public int getProtocolValidLength() {
        checkStateIsReverse();
        return mProtocolComData.getProtocolValidLength();
    }

    @Override
    public int getProtocolParamsLength() {
        checkStateIsReverse();
        return mProtocolComData.getProtocolParamsLength();
    }

    @Override
    public byte getProtocolType() {
        checkStateIsReverse();
        return mProtocolComData.getProtocolType();
    }

    @Override
    public byte getProtocolCmd() {
        checkStateIsReverse();
        return mProtocolComData.getProtocolCmd();
    }

    @Override
    public byte[] getProtocolNeedCheckData() {
        checkStateIsReverse();
        return mProtocolComData.getProtocolNeedCheckData();
    }

    @Override
    public byte[] getProtocolParams() {
        checkStateIsReverse();
        return mProtocolComData.getProtocolParams();
    }

    @Override
    public byte getProtocolCrc8() {
        checkStateIsReverse();
        return mProtocolComData.getProtocolCrc8();
    }


    @Override
    public boolean hasHead() {
        return mProtocolComData.hasHead();
    }

    @Override
    public boolean checkCrc() {
        return mProtocolComData.checkCrc();
    }

    @Override
    public boolean hasTail() {
        return mProtocolComData.hasTail();
    }


    /****************/

    private void checkStateIsEscape() {
        if (!mEscapeDataArray.isEscapeState()) {
            throw new DatagramStateException(" ! mEscapeDataArray.isEscapeState() , state is " + mEscapeDataArray.getStateStr());
        }
    }

    @Override
    public byte getHead() {
        return mAbsProtocolDataPack.getHead();
    }

    @Override
    public byte getLengthH() {
        return mAbsProtocolDataPack.getLengthH();
    }

    @Override
    public byte getLengthL() {
        return mAbsProtocolDataPack.getLengthL();
    }

    @Override
    public int getLength() {
        return mAbsProtocolDataPack.getLength();
    }

    @Override
    public void setToken(int token) {
        mAbsProtocolDataPack.setToken(token);
    }

    @Override
    public int getToken() {
        return mAbsProtocolDataPack.getToken();
    }

    @Override
    public void setVersion(byte version) {
        mAbsProtocolDataPack.setVersion(version);
    }

    @Override
    public int getVersion() {
        return mAbsProtocolDataPack.getVersion();
    }

    @Override
    public void setSeq(byte seq) {
        mAbsProtocolDataPack.setSeq(seq);
    }

    @Override
    public int getSeq() {
        return mAbsProtocolDataPack.getSeq();
    }

    @Override
    public void setCustom(byte custom) {
        mAbsProtocolDataPack.setCustom(custom);
    }

    @Override
    public byte getCustom() {
        return mAbsProtocolDataPack.getCustom();
    }

    @Override
    public void setProduct(byte product) {
        mAbsProtocolDataPack.setProduct(product);
    }

    @Override
    public byte getProduct() {
        return mAbsProtocolDataPack.getProduct();
    }

    @Override
    public void setType(byte type) {
        mAbsProtocolDataPack.setType(type);
    }

    @Override
    public byte getType() {
        return mAbsProtocolDataPack.getType();
    }

    @Override
    public void setCmd(byte cmd) {
        mAbsProtocolDataPack.setCmd(cmd);
    }

    @Override
    public byte getCmd() {
        return mAbsProtocolDataPack.getCmd();
    }

    @Override
    public void setParams(byte[] params) {
        mAbsProtocolDataPack.setParams(params);
    }

    @Override
    public byte[] getParams() {
        return mAbsProtocolDataPack.getParams();
    }

    @Override
    public byte getCrc() {
        return mAbsProtocolDataPack.getCrc();
    }

    @Override
    public byte getTail() {
        return mAbsProtocolDataPack.getTail();
    }

    @Override
    public byte[] organizeProtocolData() {
        checkStateIsEscape();
        onAddPackageEscape(mAbsProtocolDataPack.organizeProtocolData());
        return toArray();
    }

    /****************/
    @Override
    public void onAddHead(byte b) {

        mEscapeDataArray.onAddHead(b);
    }

    @Override
    public void onAddTail(byte b) {

        mEscapeDataArray.onAddTail(b);
    }

    @Override
    public void onAddDataReverse(byte b) {
        mEscapeDataArray.onAddDataReverse(b);
    }

    @Override
    public void onAddPackageReverse(byte[] data) {

        if (data == null) {
            Tlog.e(TAG, " onAddPackageReverse() data == null ");
            return;
        }

        changeStateToReverse();
        mEscapeDataArray.onAddPackageReverse(data);
    }

    @Override
    public void onAddDataEscape(byte b) {
        mEscapeDataArray.onAddDataEscape(b);
    }

    @Override
    public void onAddPackageEscape(byte[] data) {
        if (data == null) {
            Tlog.e(TAG, " onAddPackageEscape() data == null ");
            return;
        }
        changeStateToEscape();
        mEscapeDataArray.onAddPackageEscape(data);
    }

    /****************/
    @Override
    public byte getByte(int index) {
        return mEscapeDataArray.getByte(index);
    }

    @Override
    public byte[] toArray() {
        return mEscapeDataArray.toArray();
    }

    @Override
    public byte[] toArray(int srcPoint, int length) {
        if (mEscapeDataArray.getPoint() < (srcPoint + length)) {
            Tlog.e(TAG, " toArray(int int) srcPoint: " + srcPoint + " length: " + length + " mDataPoint: " + mEscapeDataArray.getPoint());
            return null;
        }
        return mEscapeDataArray.toArray(srcPoint, length);
    }

    @Override
    public boolean copyArray(byte[] data) {

        if (data == null) {
            Tlog.e(TAG, " copyArray(byte[]) data == null ");
            return false;
        }

        if (data.length < mEscapeDataArray.getPoint()) {
            Tlog.e(TAG, " copyArray(byte[])  data.length(" + data.length + ")<point(" + mEscapeDataArray.getPoint() + ")");
            return false;
        }

        return mEscapeDataArray.copyArray(data);
    }

    @Override
    public boolean copyArray(byte[] data, int srcPoint, int length) {

        if (data == null) {
            Tlog.e(TAG, "  readArray(byte[] int int) data is null ");
            return false;
        }

        if (mEscapeDataArray.getPoint() < (srcPoint + length)) {
            Tlog.e(TAG, "  copyArray(byte[] int int) srcPoint: " + srcPoint + " length: " + length + " point: " + mEscapeDataArray.getPoint());
            return false;
        }

        return mEscapeDataArray.copyArray(data, srcPoint, length);
    }

    /****************/
    @Override
    public int getPoint() {
        return mEscapeDataArray.getPoint();
    }

    @Override
    public int getCapacity() {
        return mEscapeDataArray.getCapacity();
    }

    @Override
    public boolean isEscapeState() {
        return mEscapeDataArray.isEscapeState();
    }

    @Override
    public boolean isReverseState() {
        return mEscapeDataArray.isReverseState();
    }

    @Override
    public String getStateStr() {
        return mEscapeDataArray.getStateStr();
    }

    @Override
    public void changeStateToEscape() {
        mEscapeDataArray.changeStateToEscape();
    }

    @Override
    public void changeStateToReverse() {
        mEscapeDataArray.changeStateToReverse();
    }

    @Override
    public void fillEmpty() {
        mEscapeDataArray.fillEmpty();
    }

    @Override
    public void reset() {
        this.mEscapeDataArray.reset();
        this.mAbsProtocolDataPack.setParams(null);
        clearModel();
    }

    @Override
    public void release() {
        this.mEscapeDataArray.release();
        this.obj = null;
        this.mModel = null;
    }

    /****************/

    @Override
    public String toString() {
        return ("SocketDataArray :" + hashCode()
                + "-"
                + String.valueOf(mProtocolComData)
                + " \n "
                + String.valueOf(mEscapeDataArray)
                + String.valueOf(mModel));
    }

    @Override
    protected Object clone() {

        SocketDataArray mSIComDataArray;
        try {

            mSIComDataArray = (SocketDataArray) super.clone();

        } catch (CloneNotSupportedException e) {

            mSIComDataArray = null;

        }
        return mSIComDataArray;
    }

    /**
     * 解析数据
     *
     * @param pkgData 一包数据
     * @param version 版本号
     * @return SocketDataArray
     */
    public static SocketDataArray parseSocketData(byte[] pkgData, int version) throws EscapeIOException {
        SocketDataArray mSocketDataArray = new SocketDataArray(version);
        return parseSocketData(pkgData, mSocketDataArray);
    }

    public static SocketDataArray parseSocketData(byte[] pkgData, SocketDataArray mSocketDataArray) throws EscapeIOException {
        mSocketDataArray.reset();
        mSocketDataArray.changeStateToReverse();
        mSocketDataArray.onAddPackageReverse(pkgData);

        if (!mSocketDataArray.hasHead()) {
            throw new EscapeIOException(" not has head [" + Integer.toHexString(ProtocolCode.STX) + "]");
        }
        if (!mSocketDataArray.checkCrc()) {
            throw new EscapeIOException(" crc error ");
        }
        if (!mSocketDataArray.hasTail()) {
            throw new EscapeIOException(" not has tail [" + Integer.toHexString(ProtocolCode.ETX) + "]");
        }
        return mSocketDataArray;
    }

}
