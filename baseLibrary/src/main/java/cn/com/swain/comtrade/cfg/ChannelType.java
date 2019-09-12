package cn.com.swain.comtrade.cfg;

import java.io.BufferedWriter;
import java.io.IOException;

import cn.com.swain.comtrade.exception.ComtradeNullException;
import cn.com.swain.comtrade.exception.WrongFormatException;
import cn.com.swain.comtrade.utils.ComtradeInfo;
import cn.com.swain.comtrade.utils.ComtradeUtils;


/**
 * author Guoqiang_Sun
 * date 2019/9/6
 * desc 通道的数量和类型
 */
public class ChannelType {

    public ChannelType() {
    }

    public ChannelType(String readLine) throws WrongFormatException, ComtradeNullException {
        String[] split = ComtradeUtils.split(readLine);
        ComtradeUtils.checkLength(split, 3);
        if (!split[0].equalsIgnoreCase("")) {
            TT = Integer.parseInt(split[0]);
        }

        analog_channel_A = split[1];
        analog_channel_A_count = Integer.parseInt(analog_channel_A.substring(0, analog_channel_A.length() - 1));

        state_channel_D = split[2];
        state_channel_D_count = Integer.parseInt(state_channel_D.substring(0, state_channel_D.length() - 1));
    }

    public void write(BufferedWriter mWriter) throws IOException {
        mWriter.write(String.valueOf(TT));
        mWriter.write(ComtradeInfo.CFG_LINE_SPLIT);
        mWriter.write(ComtradeUtils.valueOfStr(analog_channel_A));
        mWriter.write(ComtradeInfo.CFG_LINE_SPLIT);
        mWriter.write(ComtradeUtils.valueOfStr(state_channel_D));
    }


    public String toLineStr() {
        return String.valueOf(TT)
                + ComtradeInfo.CFG_LINE_SPLIT
                + ComtradeUtils.valueOfStr(analog_channel_A)
                + ComtradeInfo.CFG_LINE_SPLIT
                + ComtradeUtils.valueOfStr(state_channel_D);
    }

    /**
     * 通道的总数量。必须，数字，整数，
     * 最小长度=1 字符，最大长度=7 字符，最小值=1，最大值=999999，
     * TT 必须等于##A 和##D 的总和。
     */
    public int TT;

    /**
     * 带有特征字母 A 的模拟通道的数量。必需，字母数字，
     * 最小长度=2 字符，最大长度=7 字符，最小值=OA，最大值=999999A。
     */
    public String analog_channel_A;

    /**
     * 模拟通道的数量
     */
    public int analog_channel_A_count;

    public int analogShortCount() {
        return analog_channel_A_count;
    }

    /**
     * 带有识别字母 D 的状态通道数量。必须，字母数字，
     * 最小长度=2 字符，最大长度=7 字符，最小值=OD，最大值=999999D。
     */
    public String state_channel_D;

    /**
     * 状态通道数量
     */
    public int state_channel_D_count;

    public int stateShortCount() {
        return state_channel_D_count / 16;
    }

    public int stateByteCount() {
        return state_channel_D_count / 8;
    }

    public int lineBytesLength() {
        return lineBytesLength(analog_channel_A_count, state_channel_D_count);
    }

    /**
     * 前面 4 个字节 表示序号， 后面4个字节表示 时间,
     * analogChannel 表示模拟通道个数，* 2 表示字长，
     * stateChannel 表示状态通道个数， / 16 表示字长
     *
     * @param analogChannelShort 模拟通道个数
     * @param stateChannelBit    状态通道个数
     * @return 一行字长
     */
    public static int lineBytesLength(int analogChannelShort, int stateChannelBit) {
        return 4 + 4 + analogChannelShort * 2 + stateChannelBit / 8;
    }

    @Override
    public String toString() {
        return "ChannelType{" +
                "TT=" + TT +
                ", analog_channel_A='" + analog_channel_A + '\'' +
                ", analog_channel_A_count=" + analog_channel_A_count +
                ", state_channel_D='" + state_channel_D + '\'' +
                ", state_channel_D_count=" + state_channel_D_count +
                '}';
    }
}
