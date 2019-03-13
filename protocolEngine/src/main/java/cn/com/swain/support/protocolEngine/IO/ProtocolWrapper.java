package cn.com.swain.support.protocolEngine.IO;

import cn.com.swain.baselib.log.Tlog;
import cn.com.swain.support.protocolEngine.pack.ReceivesData;
import cn.com.swain.support.protocolEngine.pack.ResponseData;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/9 0009
 * desc :
 */

public class ProtocolWrapper implements IDataProtocolOutput, IDataProtocolInput {

    public static final String TAG = "ProtocolWrapper";

    public ProtocolWrapper() {

    }

    private IDataProtocolOutput mDataOutputBase;

    public void regOutputBase(IDataOutputRegInput mHardware) {
        this.mDataOutputBase = mHardware;
        if (mHardware != null) {
            mHardware.regIProtocolInput(this);
        } else {
            Tlog.e(TAG, " AbsProtocolWrapper regOutputBase() AbsHardware==null");
        }
    }

    public void unregOutputBase(IDataOutputRegInput mHardware) {
        if (mDataOutputBase != null && mDataOutputBase == mHardware) {
            Tlog.e(TAG, " AbsProtocolWrapper unregOutputBase() success");
            mDataOutputBase = null;
        }
    }

    public void releaseOutputBase(){
        mDataOutputBase = null;
    }

    private IDataProtocolInput mDataInputBase;

    public void regInputBase(IDataInputRegOutput mVirtualScm) {
        this.mDataInputBase = mVirtualScm;
        if (mVirtualScm != null) {
            mVirtualScm.regIProtocolOutput(this);
        } else {
            Tlog.e(TAG, " AbsProtocolWrapper regInputBase() AbsProtocolManager==null");
        }
    }

    public void unregInputBase(IDataInputRegOutput mVirtualScm) {
        if (mDataInputBase != null && mDataInputBase == mVirtualScm) {
            Tlog.e(TAG, " AbsProtocolWrapper unregInputBase() success");
            mDataInputBase = null;
        }
    }

    public void releaseInputBase(){
        mDataInputBase = null;
    }

    @Override
    public void onInputProtocolData(ReceivesData mReceiverData) {

        if (mDataInputBase != null) {
            mDataInputBase.onInputProtocolData(mReceiverData);
        } else {
            Tlog.e(TAG, " onInputClientData  mProtocolInputBase==null . ");
        }


    }

    @Override
    public void onOutputProtocolData(ResponseData mResponseData) {

        if (mDataOutputBase != null) {
            mDataOutputBase.onOutputProtocolData(mResponseData);
        } else {
            Tlog.e(TAG, " onOutputDataToClient  mProtocolOutputBase==null . ");
        }

    }

    @Override
    public void onBroadcastProtocolData(ResponseData mResponseData) {
        if (mDataOutputBase != null) {
            mDataOutputBase.onBroadcastProtocolData(mResponseData);
        } else {
            Tlog.e(TAG, " onOutputDataToClient  mProtocolOutputBase==null . ");
        }
    }

}
