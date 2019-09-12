package cn.com.swain.comtrade;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import cn.com.swain.comtrade.cfg.ComtradeConfig;
import cn.com.swain.comtrade.dat.ComtradeChannelData;
import cn.com.swain.comtrade.dat.ComtradeDatBinary;
import cn.com.swain.comtrade.exception.ComtradeNullException;
import cn.com.swain.comtrade.exception.WrongFormatException;
import cn.com.swain.comtrade.utils.ComtradeInfo;

/**
 * author Guoqiang_Sun
 * date 2019/9/6
 * desc
 */
public class DatWorker {

    public DatWorker() {
    }

    /**
     * 读取二进制通道数据
     *
     * @param config  配置属性
     * @param datPath dat文件路径
     * @return ComtradeChannelData
     * @throws IOException e
     */
    public ComtradeChannelData readBinaryChannelDat(ComtradeConfig config, File datPath) throws IOException {
        if (config == null) {
            throw new ComtradeNullException("ComtradeConfig must not be null");
        }
        if (!config.ft.equalsIgnoreCase(ComtradeInfo.BINARY)) {
            throw new WrongFormatException("config.ft must be " + ComtradeInfo.BINARY);
        }
        return readBinaryChannelDat(config, new BufferedInputStream(new FileInputStream(datPath)));
    }

    /**
     * 读取二进制通道数据
     *
     * @param config 配置属性
     * @param bis    dat输入流
     * @return ComtradeChannelData
     * @throws IOException e
     */
    public ComtradeChannelData readBinaryChannelDat(ComtradeConfig config,
                                                    BufferedInputStream bis) throws IOException {
        if (config == null) {
            throw new ComtradeNullException("ComtradeConfig must not be null");
        }
        if (!config.ft.equalsIgnoreCase(ComtradeInfo.BINARY)) {
            throw new WrongFormatException("config.ft must be " + ComtradeInfo.BINARY);
        }
        ComtradeChannelData mChannelData;

        try {
            int analogShortCount = config.mChannelType.analogShortCount();
            int stateShortCount = config.mChannelType.stateShortCount();
            int count = analogShortCount + stateShortCount;
            mChannelData = new ComtradeChannelData(config.mSampRateInfo.endsamp, count);

            ComtradeDatBinary binary = new ComtradeDatBinary(config.mChannelType.analog_channel_A_count,
                    config.mChannelType.state_channel_D_count);

            while (binary.read(bis) != -1) {
                mChannelData.setTimestamp(binary.timestamp);
                short[] analogChannelValue = binary.analogChannelValue;
                for (int i = 0; i < analogShortCount; i++) {
                    mChannelData.setValue(i, analogChannelValue[i]);
                }
                short[] stateChannelValue = binary.stateChannelValue;
                for (int i = 0; i < stateShortCount; i++) {
                    mChannelData.setValue(analogShortCount + i, stateChannelValue[i]);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return mChannelData;
    }


}
