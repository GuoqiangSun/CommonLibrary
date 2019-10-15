package cn.com.swain.comtrade;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.com.swain.comtrade.cfg.AnalogChannel;
import cn.com.swain.comtrade.cfg.ComtradeConfig;
import cn.com.swain.comtrade.dat.ComtradeChannelData;
import cn.com.swain.comtrade.dat.ComtradeDatBinary;
import cn.com.swain.comtrade.exception.ComtradeNullException;
import cn.com.swain.comtrade.exception.WrongFormatException;
import cn.com.swain.comtrade.utils.ComtradeInfo;

/**
 * author Guoqiang_Sun
 * date 2019/9/10
 * desc
 */
public class ComtradeWorker {

    public CfgWorker getConfigWorker() {
        return mConfigWorker;
    }

    public DatWorker getDatWorker() {
        return mDatWorker;
    }

    private final CfgWorker mConfigWorker;
    private final DatWorker mDatWorker;

    public ComtradeWorker() {
        this.mConfigWorker = new CfgWorker();
        this.mDatWorker = new DatWorker();
    }

    public ComtradeWorker(CfgWorker mConfigWorker, DatWorker mDatWorker) {
        this.mConfigWorker = mConfigWorker;
        this.mDatWorker = mDatWorker;
    }

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
            "yyyy-MM-dd hh:mm:ss.SSSSSS", Locale.getDefault());

    /**
     * 二进制数据文件转ASCII文件
     *
     * @param inputCfgPath  配置文件路径
     * @param inputDatPath  数据文件路径
     * @param outputDatPath 输出路径
     * @throws IOException e
     */
    public void Binary2ASCII(File inputCfgPath, File inputDatPath, File outputDatPath) throws IOException {
        ComtradeConfig config = mConfigWorker.read(inputCfgPath);
        Binary2ASCII(config, inputDatPath, outputDatPath);
    }

    /**
     * 二进制数据文件转ASCII文件
     *
     * @param config        配置属性
     * @param inputDatPath  输入数据路径
     * @param outputDatPath 输出数据路径
     * @throws ComtradeNullException e
     */
    public void Binary2ASCII(ComtradeConfig config, File inputDatPath, File outputDatPath) throws ComtradeNullException {
        if (config == null) {
            throw new ComtradeNullException("ComtradeConfig must not be null");
        }

        if (config.ft.equalsIgnoreCase(ComtradeInfo.ASCII)) {
            return;
        }

        BufferedWriter bw = null;
        BufferedInputStream bis = null;
        try {

            bis = new BufferedInputStream(new FileInputStream(inputDatPath));
            bw = new BufferedWriter(new FileWriter(outputDatPath));

//            System.out.println("length:" + bis.available());

            int analog_channel_a_count = config.mChannelType.analogShortCount();
            int state_channel_d_count = config.mChannelType.stateShortCount();

//            System.out.println("analog_count:" + config.mChannelType.analog_channel_A_count);
//            System.out.println("state_count:" + config.mChannelType.state_channel_D_count);
//            System.out.println("lines:" + bis.available() / config.mChannelType.lineBytesLength());
//            System.out.println("bytes:" + bis.available() / 202000);

            ComtradeDatBinary binary = new ComtradeDatBinary(config.mChannelType.analog_channel_A_count
                    , config.mChannelType.state_channel_D_count);

            while (binary.read(bis) != -1) {
                bw.write(binary.seq + ComtradeInfo.CFG_LINE_SPLIT);
                bw.write(binary.timestamp + ComtradeInfo.CFG_LINE_SPLIT);

                short[] analogChannelValue = binary.analogChannelValue;
                for (int i = 0; i < analog_channel_a_count; i++) {
                    if (i == analog_channel_a_count - 1) {
                        if (state_channel_d_count > 0) {
                            // 有状态通道数据 + "，"
                            bw.write(analogChannelValue[i] + ComtradeInfo.CFG_LINE_SPLIT);
                        } else {
                            bw.write(String.valueOf(analogChannelValue[i]));
                        }
                    } else {
                        bw.write(analogChannelValue[i] + ComtradeInfo.CFG_LINE_SPLIT);
                    }
                }
                short[] stateChannelValue = binary.stateChannelValue;
                for (int i = 0; i < state_channel_d_count; i++) {
                    if (i == state_channel_d_count - 1) {
                        bw.write(String.valueOf(stateChannelValue[i]));
                    } else {
                        bw.write(stateChannelValue[i] + ComtradeInfo.CFG_LINE_SPLIT);
                    }
                }
                bw.newLine();
            }
            bw.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 二进制模拟数据传csv
     *
     * @param fileNameCfg 配置文件路径
     * @param fileNameDat 数据文件路径
     * @param csvDatDir   输出csv父类路径
     * @param csvDatName  输出csv名称
     * @throws IOException e
     */
    public void binaryAnalogDatToCsv(File fileNameCfg, File fileNameDat,
                                     File csvDatDir, String csvDatName) throws IOException {
        ComtradeConfig config = mConfigWorker.read(fileNameCfg);
        if (config == null) {
            throw new ComtradeNullException("ComtradeConfig must not be null");
        }
        if (!config.ft.equalsIgnoreCase(ComtradeInfo.BINARY)) {
            throw new WrongFormatException("config.ft must be " + ComtradeInfo.BINARY);
        }
        ComtradeChannelData channelData =
                mDatWorker.readBinaryChannelDat(config, fileNameDat);
        if (channelData == null) {
            throw new ComtradeNullException("ComtradeChannelData must not be null");
        }
        analogDatToCsv(config, channelData, csvDatDir, csvDatName);
    }


    /**
     * 二进制模拟数据传csv
     *
     * @param parentPath  父类路径
     * @param fileNameCfg 配置文件路径
     * @param fileNameDat 数据文件路径
     * @param csvDatName  输出csv名称
     * @throws IOException e
     */
    public void binaryAnalogDatToCsv(String parentPath, String fileNameCfg,
                                     String fileNameDat, String csvDatName) throws IOException {
        ComtradeConfig config = mConfigWorker.read(new File(parentPath, fileNameCfg));
        if (config == null) {
            throw new ComtradeNullException("ComtradeConfig must not be null");
        }
        if (!config.ft.equalsIgnoreCase(ComtradeInfo.BINARY)) {
            throw new WrongFormatException("config.ft must be " + ComtradeInfo.BINARY);
        }
        ComtradeChannelData channelData =
                mDatWorker.readBinaryChannelDat(config, new File(parentPath, fileNameDat));
        analogDatToCsv(config, channelData, parentPath, csvDatName);
    }

    /**
     * 二进制模拟数据传csv
     *
     * @param config      配置属性
     * @param parentPath  父类路径
     * @param channelData 数据
     * @param csvDatName  输出csv名称
     * @throws IOException e
     */
    public void binaryAnalogDatToCsv(ComtradeConfig config, ComtradeChannelData channelData,
                                     String parentPath, String csvDatName) throws IOException {
        if (config == null) {
            throw new ComtradeNullException("ComtradeConfig must not be null");
        }
        if (!config.ft.equalsIgnoreCase(ComtradeInfo.BINARY)) {
            throw new WrongFormatException("config.ft must be " + ComtradeInfo.BINARY);
        }
        if (channelData == null) {
            throw new ComtradeNullException("ComtradeChannelData must not be null");
        }
        analogDatToCsv(config, channelData, parentPath, csvDatName);
    }

    /**
     * 二进制模拟数据传csv
     *
     * @param config      配置属性
     * @param parentPath  父类路径
     * @param channelData 数据
     * @param csvDatName  输出csv名称
     * @throws IOException e
     */
    public void binaryAnalogDatToCsv(ComtradeConfig config, ComtradeChannelData channelData,
                                     File parentPath, String csvDatName) throws IOException {
        if (config == null) {
            throw new ComtradeNullException("ComtradeConfig must not be null");
        }
        if (!config.ft.equalsIgnoreCase(ComtradeInfo.BINARY)) {
            throw new WrongFormatException("config.ft must be " + ComtradeInfo.BINARY);
        }
        if (channelData == null) {
            throw new ComtradeNullException("ComtradeChannelData must not be null");
        }
        analogDatToCsv(config, channelData, parentPath, csvDatName);
    }

    /**
     * 模拟数据传csv
     *
     * @param config      配置属性
     * @param channelData 数据内容
     * @param parentPath  父类路径
     * @param csvDatName  csv文件名
     * @throws IOException e
     */
    public void analogDatToCsv(ComtradeConfig config,
                               ComtradeChannelData channelData,
                               String parentPath,
                               String csvDatName) throws IOException {
        analogDatToCsv(config, channelData, new File(parentPath), csvDatName);
    }

    /**
     * 模拟数据传csv
     *
     * @param config      配置属性
     * @param channelData 数据内容
     * @param parentPath  父类路径
     * @param csvDatName  csv文件名
     * @throws IOException e
     */
    public void analogDatToCsv(ComtradeConfig config,
                               ComtradeChannelData channelData,
                               File parentPath,
                               String csvDatName) throws IOException {
        AnalogChannel[] mAnalogChannels = config.mAnalogChannels;
        for (int i = 0; i < mAnalogChannels.length; i++) {
            String ch_id = mAnalogChannels[i].ch_id;
            File csvPath = new File(parentPath,
                    csvDatName + "_" + mAnalogChannels[i].An + "_" + ch_id + ".csv");
            analogDatToCsv(config, channelData, i, new BufferedWriter(new FileWriter(csvPath)));
        }
    }

    /**
     * 模拟通道数据传csv
     *
     * @param config      配置属性
     * @param channelData 数据内容
     * @param i           通道下标
     * @throws IOException e
     */
    public ByteArrayOutputStream analogDatToCsv(ComtradeConfig config,
                                                ComtradeChannelData channelData,
                                                int i) throws IOException {
        if (i >= config.mAnalogChannels.length) {
            throw new ComtradeNullException();
        }
        short[] channleValue = channelData.getChannleValue(config.mAnalogChannels[i].An - 1);
        int length;
        if (channleValue != null) {
            String format = simpleDateFormat.format(new Date());
            int length1 = format.getBytes().length;
            length = channleValue.length * (length1 + 1 + 2);
        } else {
            length = 1024 * 8;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream(length);
        analogDatToCsv(config, channelData, i, new BufferedWriter(new OutputStreamWriter(bos)));
        return bos;
    }

    /**
     * 模拟通道数据传csv
     *
     * @param config      配置属性
     * @param channelData 数据内容
     * @param i           通道下标
     * @param bos         内存流
     * @throws IOException e
     */
    public void analogDatToCsv(ComtradeConfig config,
                               ComtradeChannelData channelData,
                               int i,
                               ByteArrayOutputStream bos) throws IOException {
        analogDatToCsv(config, channelData, i, new BufferedWriter(new OutputStreamWriter(bos)));
    }

    /**
     * 模拟通道数据传csv
     *
     * @param config      配置属性
     * @param channelData 数据内容
     * @param i           通道下标
     * @param bw          输出流
     * @throws IOException e
     */
    public void analogDatToCsv(ComtradeConfig config,
                               ComtradeChannelData channelData,
                               int i,
                               BufferedWriter bw) throws IOException {
        long firstTimeL = config.mTimeDates.firstTimeL;
        AnalogChannel[] mAnalogChannels = config.mAnalogChannels;
        short[][] values = channelData.getValues();
        long[] ts = channelData.getTs();

        if (i >= mAnalogChannels.length) {
            throw new ComtradeNullException();
        }
        AnalogChannel mAnalogChannel = mAnalogChannels[i];
        int an = mAnalogChannel.An - 1;

        try {
            bw.write("timestamp,value");
            bw.newLine();

            short[] value = values[an];

            for (int j = 0; j < value.length; j++) {
                bw.write(simpleDateFormat.format(new Date(firstTimeL + ts[j])));
//                    bw.write("" + ts[j]);
//                bw.write("," +mAnalogChannel.ration(value[j]));
                bw.write("," + value[j]);
                bw.newLine();
            }

            bw.flush();

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 二进制模拟数据转换后转csv
     *
     * @param fileNameCfg 配置文件路径
     * @param fileNameDat 数据文件路径
     * @param csvDatDir   输出csv父类路径
     * @param csvDatName  输出csv名称
     * @throws IOException e
     */
    public void binaryAnalogRationDatToCsv(File fileNameCfg, File fileNameDat,
                                     File csvDatDir, String csvDatName) throws IOException {
        ComtradeConfig config = mConfigWorker.read(fileNameCfg);
        if (config == null) {
            throw new ComtradeNullException("ComtradeConfig must not be null");
        }
        if (!config.ft.equalsIgnoreCase(ComtradeInfo.BINARY)) {
            throw new WrongFormatException("config.ft must be " + ComtradeInfo.BINARY);
        }
        ComtradeChannelData channelData =
                mDatWorker.readBinaryChannelDat(config, fileNameDat);
        if (channelData == null) {
            throw new ComtradeNullException("ComtradeChannelData must not be null");
        }
        analogDatRationToCsv(config, channelData, csvDatDir, csvDatName);
    }


    /**
     * 二进制模拟数据传csv
     *
     * @param parentPath  父类路径
     * @param fileNameCfg 配置文件路径
     * @param fileNameDat 数据文件路径
     * @param csvDatName  输出csv名称
     * @throws IOException e
     */
    public void binaryAnalogDatRationToCsv(String parentPath, String fileNameCfg,
                                     String fileNameDat, String csvDatName) throws IOException {
        ComtradeConfig config = mConfigWorker.read(new File(parentPath, fileNameCfg));
        if (config == null) {
            throw new ComtradeNullException("ComtradeConfig must not be null");
        }
        if (!config.ft.equalsIgnoreCase(ComtradeInfo.BINARY)) {
            throw new WrongFormatException("config.ft must be " + ComtradeInfo.BINARY);
        }
        ComtradeChannelData channelData =
                mDatWorker.readBinaryChannelDat(config, new File(parentPath, fileNameDat));
        analogDatRationToCsv(config, channelData, parentPath, csvDatName);
    }

    /**
     * 二进制模拟数据传csv
     *
     * @param config      配置属性
     * @param parentPath  父类路径
     * @param channelData 数据
     * @param csvDatName  输出csv名称
     * @throws IOException e
     */
    public void binaryAnalogDatRationToCsv(ComtradeConfig config, ComtradeChannelData channelData,
                                     String parentPath, String csvDatName) throws IOException {
        if (config == null) {
            throw new ComtradeNullException("ComtradeConfig must not be null");
        }
        if (!config.ft.equalsIgnoreCase(ComtradeInfo.BINARY)) {
            throw new WrongFormatException("config.ft must be " + ComtradeInfo.BINARY);
        }
        if (channelData == null) {
            throw new ComtradeNullException("ComtradeChannelData must not be null");
        }
        analogDatRationToCsv(config, channelData, parentPath, csvDatName);
    }

    /**
     * 二进制模拟数据传csv
     *
     * @param config      配置属性
     * @param parentPath  父类路径
     * @param channelData 数据
     * @param csvDatName  输出csv名称
     * @throws IOException e
     */
    public void binaryAnalogDatRationToCsv(ComtradeConfig config, ComtradeChannelData channelData,
                                     File parentPath, String csvDatName) throws IOException {
        if (config == null) {
            throw new ComtradeNullException("ComtradeConfig must not be null");
        }
        if (!config.ft.equalsIgnoreCase(ComtradeInfo.BINARY)) {
            throw new WrongFormatException("config.ft must be " + ComtradeInfo.BINARY);
        }
        if (channelData == null) {
            throw new ComtradeNullException("ComtradeChannelData must not be null");
        }
        analogDatRationToCsv(config, channelData, parentPath, csvDatName);
    }

    /**
     * 模拟数据转换后转csv
     *
     * @param config      配置属性
     * @param channelData 数据内容
     * @param parentPath  父类路径
     * @param csvDatName  csv文件名
     * @throws IOException e
     */
    public void analogDatRationToCsv(ComtradeConfig config,
                               ComtradeChannelData channelData,
                               String parentPath,
                               String csvDatName) throws IOException {
        analogDatRationToCsv(config, channelData, new File(parentPath), csvDatName);
    }

    /**
     * 模拟数据转换后转csv
     *
     * @param config      配置属性
     * @param channelData 数据内容
     * @param parentPath  父类路径
     * @param csvDatName  csv文件名
     * @throws IOException e
     */
    public void analogDatRationToCsv(ComtradeConfig config,
                               ComtradeChannelData channelData,
                               File parentPath,
                               String csvDatName) throws IOException {
        AnalogChannel[] mAnalogChannels = config.mAnalogChannels;
        for (int i = 0; i < mAnalogChannels.length; i++) {
            String ch_id = mAnalogChannels[i].ch_id;
            File csvPath = new File(parentPath,
                    csvDatName + "_" + mAnalogChannels[i].An + "_" + ch_id + ".csv");
            analogDatRationToCsv(config, channelData, i, new BufferedWriter(new FileWriter(csvPath)));
        }
    }

    /**
     * 模拟通道数据转换后转csv
     *
     * @param config      配置属性
     * @param channelData 数据内容
     * @param i           通道下标
     * @throws IOException e
     */
    public ByteArrayOutputStream analogDatRationToCsv(ComtradeConfig config,
                                                ComtradeChannelData channelData,
                                                int i) throws IOException {
        if (i >= config.mAnalogChannels.length) {
            throw new ComtradeNullException();
        }
        short[] channleValue = channelData.getChannleValue(config.mAnalogChannels[i].An - 1);
        int length;
        if (channleValue != null) {
            String format = simpleDateFormat.format(new Date());
            int length1 = format.getBytes().length;
            length = channleValue.length * (length1 + 1 + 2);
        } else {
            length = 1024 * 8;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream(length);
        analogDatRationToCsv(config, channelData, i, new BufferedWriter(new OutputStreamWriter(bos)));
        return bos;
    }

    /**
     * 模拟通道数据转换后转csv
     *
     * @param config      配置属性
     * @param channelData 数据内容
     * @param i           通道下标
     * @param bos         内存流
     * @throws IOException e
     */
    public void analogDatRationToCsv(ComtradeConfig config,
                               ComtradeChannelData channelData,
                               int i,
                               ByteArrayOutputStream bos) throws IOException {
        analogDatRationToCsv(config, channelData, i, new BufferedWriter(new OutputStreamWriter(bos)));
    }

    /**
     * 模拟通道数据转换后转csv
     *
     * @param config      配置属性
     * @param channelData 数据内容
     * @param i           通道下标
     * @param bw          输出流
     * @throws IOException e
     */
    public void analogDatRationToCsv(ComtradeConfig config,
                               ComtradeChannelData channelData,
                               int i,
                               BufferedWriter bw) throws IOException {
        long firstTimeL = config.mTimeDates.firstTimeL;
        AnalogChannel[] mAnalogChannels = config.mAnalogChannels;
        short[][] values = channelData.getValues();
        long[] ts = channelData.getTs();

        if (i >= mAnalogChannels.length) {
            throw new ComtradeNullException();
        }
        AnalogChannel mAnalogChannel = mAnalogChannels[i];
        int an = mAnalogChannel.An - 1;

        try {
            bw.write("timestamp,value");
            bw.newLine();

            short[] value = values[an];

            for (int j = 0; j < value.length; j++) {
                bw.write(simpleDateFormat.format(new Date(firstTimeL + ts[j])));
                bw.write("," + mAnalogChannel.ration(value[j]));
                bw.newLine();
            }

            bw.flush();

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
