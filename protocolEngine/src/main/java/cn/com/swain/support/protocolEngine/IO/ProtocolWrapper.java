package cn.com.swain.support.protocolEngine.IO;

import cn.com.swain.baselib.app.IApp.IService;
import cn.com.swain.support.protocolEngine.pack.ReceivesData;
import cn.com.swain.support.protocolEngine.pack.ResponseData;
import cn.com.swain.baselib.log.Tlog;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/9 0009
 * desc :
 */

public class ProtocolWrapper implements IService, IDataProtocolOutput, IDataProtocolInput {

    public static final String TAG = "ProtocolWrapper";

    public ProtocolWrapper() {

    }

    @Override
    public void onSCreate() {
        Tlog.v(TAG, " ProtocolWrapper onSCreate()");
    }

    @Override
    public void onSResume() {
        Tlog.v(TAG, " ProtocolWrapper onSResume()");
    }

    @Override
    public void onSPause() {
        Tlog.v(TAG, " ProtocolWrapper onSPause()");
    }

    @Override
    public void onSDestroy() {
        Tlog.v(TAG, " ProtocolWrapper onSDestroy()");
    }

    @Override
    public void onSFinish() {
        Tlog.v(TAG, " ProtocolWrapper onSFinish()");
    }

    private IDataProtocolOutput mDataOutputBase;

    public void regOutputBase(IDataOutputRegInput mHardware) {
        this.mDataOutputBase = mHardware;
        if (mHardware != null) {
            mHardware.regIProtocolInput(this);
        } else {
            Tlog.e(TAG, " AbsProtocolWrapper regOutputBase()  AbsHardware==null ");
        }
    }

    private IDataProtocolInput mDataInputBase;

    public void regInputBase(IDataInputRegOutput mVirtualScm) {
        this.mDataInputBase = mVirtualScm;
        if (mVirtualScm != null) {
            mVirtualScm.regIProtocolOutput(this);
        } else {
            Tlog.e(TAG, " AbsProtocolWrapper regInputBase()  AbsProtocolManager==null ");
        }
    }

    @Override
    public void onInputServerData(ReceivesData mReceiverData) {

        if (mDataInputBase != null) {
            mDataInputBase.onInputServerData(mReceiverData);
        } else {
            Tlog.e(TAG, " onInputClientData  mProtocolInputBase==null . ");
        }


    }

    @Override
    public void onOutputDataToServer(ResponseData mResponseData) {

        if (mDataOutputBase != null) {
            mDataOutputBase.onOutputDataToServer(mResponseData);
        } else {
            Tlog.e(TAG, " onOutputDataToClient  mProtocolOutputBase==null . ");
        }

    }

    @Override
    public void onBroadcastDataToServer(ResponseData mResponseData) {
        if (mDataOutputBase != null) {
            mDataOutputBase.onBroadcastDataToServer(mResponseData);
        } else {
            Tlog.e(TAG, " onOutputDataToClient  mProtocolOutputBase==null . ");
        }
    }

}
