package cn.com.swain.support.protocolEngine.datagram;

import cn.com.swain.baselib.log.Tlog;
import cn.com.swain.support.protocolEngine.ProtocolBuild;
import cn.com.swain.support.protocolEngine.datagram.ProtocolDatagram.AbsProtocolDataPack;
import cn.com.swain.support.protocolEngine.datagram.ProtocolDatagram.XX.XXProtocolComData;
import cn.com.swain.support.protocolEngine.datagram.ProtocolDatagram.XX.XXProtocolComData2;
import cn.com.swain.support.protocolEngine.datagram.ProtocolException.DatagramStateException;
import cn.com.swain.support.protocolEngine.datagram.ProtocolException.EscapeIOException;
import cn.com.swain.support.protocolEngine.datagram.ProtocolException.UnknownVersionException;
import cn.com.swain.support.protocolEngine.datagram.escape.IEscapeDataArray;
import cn.com.swain.support.protocolEngine.datagram.escape.XX.XXEscapeDataArray;
import cn.com.swain.support.protocolEngine.pack.BaseModel;
import cn.com.swain.support.protocolEngine.pack.ComModel;
import cn.com.swain.support.protocolEngine.resolve.AbsProtocolProcessor;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/1 0016
 * desc :
 */
public class SocketDataArray extends AbsProtocolDataPack implements Cloneable, IEscapeDataArray {

    private final String TAG = AbsProtocolProcessor.TAG;

    private final IEscapeDataArray mEscapeDataArray;
    private final AbsProtocolDataPack mAbsProtocolDataPack;

    private static final int DATA_BODY = 12;

    private final int initVersion;

    /**
     * 初始化version
     */
    public final int getConstructionVersion() {
        return initVersion;
    }

    public SocketDataArray(int version) {
        this.initVersion = version;
        Tlog.v(TAG, " new SocketDataArray() version:" + version + " - 0x" + Integer.toHexString(version));

        int body = ProtocolBuild.VERSION.getVersion(version) <= ProtocolBuild.VERSION.VERSION_0
                ? DATA_BODY : (DATA_BODY + DATA_BODY / 2);

        if (ProtocolBuild.VERSION.isXXVersion(version)) {

            this.mEscapeDataArray = new XXEscapeDataArray(body);

            int realVersion = ProtocolBuild.VERSION.removeHighVersion(version);

            if (realVersion == ProtocolBuild.VERSION.VERSION_0) {
                this.mAbsProtocolDataPack = new XXProtocolComData(this.mEscapeDataArray);
            } else if (realVersion == ProtocolBuild.VERSION.VERSION_SEQ) {
                this.mAbsProtocolDataPack = new XXProtocolComData2(this.mEscapeDataArray);
            } else {
                throw new UnknownVersionException(" new SocketDataArray() unknown version:" + version);
            }

        } else {
            throw new UnknownVersionException(" new SocketDataArray() unknown company version:" + version);
        }
    }


    /**
     * 是否收到一包完整的数据包
     * 只要收到包头，包尾就算收到完整的数据包
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
        this.mModel.addModel(mModelNew);
    }

    public ComModel getComModel() {
        return mModel;
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
        return mAbsProtocolDataPack.getProtocolHead();
    }

    @Override
    public byte getProtocolTail() {
        checkStateIsReverse();
        return mAbsProtocolDataPack.getProtocolTail();
    }

    @Override
    public int getProtocolVersion() {
        checkStateIsReverse();
        return mAbsProtocolDataPack.getProtocolVersion();
    }

    @Override
    public int getProtocolSequence() {
        checkStateIsReverse();
        return mAbsProtocolDataPack.getProtocolSequence();
    }

    @Override
    public int getProtocolToken(int point) {
        checkStateIsReverse();
        return mAbsProtocolDataPack.getProtocolToken(point);
    }

    @Override
    public int getProtocolToken() {
        checkStateIsReverse();
        return mAbsProtocolDataPack.getProtocolToken();
    }

    @Override
    public int getProtocolCustom() {
        checkStateIsReverse();
        return mAbsProtocolDataPack.getProtocolCustom();
    }

    @Override
    public int getProtocolProduct() {
        checkStateIsReverse();
        return mAbsProtocolDataPack.getProtocolProduct();
    }


    @Override
    public int getProtocolValidLength() {
        checkStateIsReverse();
        return mAbsProtocolDataPack.getProtocolValidLength();
    }

    @Override
    public int getProtocolParamsLength() {
        checkStateIsReverse();
        return mAbsProtocolDataPack.getProtocolParamsLength();
    }

    @Override
    public byte getProtocolType() {
        checkStateIsReverse();
        return mAbsProtocolDataPack.getProtocolType();
    }

    @Override
    public byte getProtocolCmd() {
        checkStateIsReverse();
        return mAbsProtocolDataPack.getProtocolCmd();
    }

    @Override
    public byte[] getProtocolNeedCheckData() {
        checkStateIsReverse();
        return mAbsProtocolDataPack.getProtocolNeedCheckData();
    }

    @Override
    public byte getNeedCheckDataCrc() {
        checkStateIsReverse();
        return mAbsProtocolDataPack.getNeedCheckDataCrc();
    }

    @Override
    public byte[] getProtocolParams() {
        checkStateIsReverse();
        return mAbsProtocolDataPack.getProtocolParams();
    }

    @Override
    public byte getProtocolCrc8() {
        checkStateIsReverse();
        return mAbsProtocolDataPack.getProtocolCrc8();
    }


    @Override
    public boolean hasProtocolHead() {
        checkStateIsReverse();
        return mAbsProtocolDataPack.hasProtocolHead();
    }

    @Override
    public boolean checkProtocolCrc() {
        checkStateIsReverse();
        return mAbsProtocolDataPack.checkProtocolCrc();
    }

    @Override
    public boolean hasProtocolTail() {
        checkStateIsReverse();
        return mAbsProtocolDataPack.hasProtocolTail();
    }


    /****************/

    private void checkStateIsEscape() {
        if (!mEscapeDataArray.isEscapeState()) {
            throw new DatagramStateException(" ! mEscapeDataArray.isEscapeState() , state is " + mEscapeDataArray.getStateStr());
        }
    }

    @Override
    public void setHead(byte head) {
        mAbsProtocolDataPack.setHead(head);
    }

    @Override
    public byte getHead() {
        return mAbsProtocolDataPack.getHead();
    }

    @Override
    public void setLengthH(byte lengthH) {
        mAbsProtocolDataPack.setLengthH(lengthH);
    }

    @Override
    public byte getLengthH() {
        return mAbsProtocolDataPack.getLengthH();
    }

    @Override
    public void setLengthL(byte lengthL) {
        mAbsProtocolDataPack.setLengthL(lengthL);
    }

    @Override
    public byte getLengthL() {
        return mAbsProtocolDataPack.getLengthL();
    }

    @Override
    public void setLength(int effectiveLength) {
        mAbsProtocolDataPack.setLength(effectiveLength);
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
    public byte getVersion() {
        return mAbsProtocolDataPack.getVersion();
    }

    @Override
    public void setSeq(byte seq) {
        mAbsProtocolDataPack.setSeq(seq);
    }

    @Override
    public byte getSeq() {
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
    public void setCrc(byte crc) {
        mAbsProtocolDataPack.setCrc(crc);
    }

    @Override
    public byte getCrc() {
        return mAbsProtocolDataPack.getCrc();
    }

    @Override
    public void setTail(byte tail) {
        mAbsProtocolDataPack.setTail(tail);
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
    public boolean isHeadByte(byte b) {
        return mEscapeDataArray.isHeadByte(b);
    }

    @Override
    public void onAddTail(byte b) {

        mEscapeDataArray.onAddTail(b);
    }

    @Override
    public boolean isTailByte(byte b) {
        return mEscapeDataArray.isTailByte(b);
    }

    @Override
    public boolean isEscapeByte(byte b) {
        return mEscapeDataArray.isEscapeByte(b);
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

    @Override
    public boolean checkIsSpecialByte(byte b) {
        return mEscapeDataArray.checkIsSpecialByte(b);
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
        setParams(null);
        clearModel();
    }

    @Override
    public void release() {
        this.mEscapeDataArray.release();
        setParams(null);
        this.obj = null;
        this.mModel = null;
    }

    /****************/

    @Override
    public String toString() {
        return "SocketDataArray :" + hashCode()
                + "-"
                + String.valueOf(mAbsProtocolDataPack)
                + " \n "
                + String.valueOf(mEscapeDataArray)
                + " \n "
                + String.valueOf(mModel);
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

        if (!mSocketDataArray.hasProtocolHead()) {
            throw new EscapeIOException(" not has head ["
                    + ProtocolBuild.VERSION.getSTX(mSocketDataArray.getConstructionVersion()) + "]");
        }
        if (!mSocketDataArray.checkProtocolCrc()) {
            throw new EscapeIOException(" crc error my crc:["
                    + mSocketDataArray.getNeedCheckDataCrc() + "] protocolCrc:" + mSocketDataArray.getProtocolCrc8());
        }
        if (!mSocketDataArray.hasProtocolTail()) {
            throw new EscapeIOException(" not has tail ["
                    + ProtocolBuild.VERSION.getETX(mSocketDataArray.getConstructionVersion()) + "]");
        }
        return mSocketDataArray;
    }

}
