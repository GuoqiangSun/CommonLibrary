package cn.com.swain.comtrade.utils;

import java.io.File;

import cn.com.swain.comtrade.cfg.AnalogChannel;
import cn.com.swain.comtrade.exception.ComtradeNullException;
import cn.com.swain.comtrade.exception.WrongFormatException;

/**
 * author Guoqiang_Sun
 * date 2019/9/6
 * desc
 */
public class ComtradeUtils {

    public static String[] split(String line) throws ComtradeNullException {
        if (line == null || line.length() <= 0) {
            throw new ComtradeNullException();
        }
        return line.split(ComtradeInfo.CFG_LINE_SPLIT);
    }

    public static void checkLength(String[] split, int length) throws WrongFormatException {
        if (split == null || split.length < length) {
            throw new WrongFormatException("Wrong format");
        }
    }

    /**
     * null字符串 给 ""
     */
    public static String valueOfStr(String str) {
        return str == null ? "" : str;
    }

    /**
     * 系数转换
     */
    public static float ratioConv(AnalogChannel mChannel, short value) {
        return mChannel.a * value + mChannel.b;
    }

    public enum UI { // 电压电流
        U("u", "v"),
        I("i", "a"),
        UN("un");

        public final String[] simpleName;

        UI(String... simpleName) {
            this.simpleName = simpleName;
        }

        boolean eq(String s) {
            for (String name : simpleName) {
                if (s.contains(name)) {
                    return true;
                }
            }
            return false;
        }

        public static UI of(String channelName) {
            if (channelName == null
                    || channelName.equalsIgnoreCase("")) {
                return UI.UN;
            }
            String s = channelName.toLowerCase();

            if (UI.I.eq(s)) {
                return UI.I;
            } else if (UI.U.eq(s)) {
                return UI.U;
            }
            return UI.UN;
        }
    }

    public enum PL { // 相线
        A(0xFFFFFF00, "a"), // A相 // Color.YELLOW
        B(0xFF00FF00, "b"),// B相 // Color.GREEN
        C(0xFFFF0000, "c"),// C相 // Color.RED
        N(0xFF000000, "0", "n"),// 零线 // Color.BLACK //PS::simpleName 是数字0 不是字母o
        L(0xFFFF0000, "l"), // 火线 // Color.RED
        E(0xFF9ACD32, "e"),// 地线
        UN(0xFF0F0F0F, "un"); // unknown

        public final int color;
        public final String[] simpleName;

        PL(int color, String... simpleName) {
            this.color = color;
            this.simpleName = simpleName;
        }

        boolean eq(String s) {
            for (String name : simpleName) {
                if (s.contains(name)) {
                    return true;
                }
            }
            return false;
        }

        public static PL of(String channelName) {
            if (channelName == null
                    || channelName.equalsIgnoreCase("")) {
                return PL.UN;
            }
            String s = channelName.toLowerCase();
            if (PL.A.eq(s)) {
                return PL.A;
            } else if (PL.B.eq(s)) {
                return PL.B;
            } else if (PL.C.eq(s)) {
                return PL.C;
            } else if (PL.N.eq(s)) {
                return PL.N;
            }
            return PL.UN;
        }
    }

    /**
     * 已知配置文件路径,换算数据文件路径
     *
     * @param cfgPath 配置文件路径
     * @return 数据文件路径
     */
    public static File cfgFile2DatFile(String cfgPath) {
        if (cfgPath == null) {
            return null;
        }
        return cfgFile2DatFile(new File(cfgPath));
    }

    /**
     * 已知配置文件路径,换算数据文件路径
     *
     * @param cfgFile 配置文件路径
     * @return 数据文件路径
     */
    public static File cfgFile2DatFile(File cfgFile) {
        if (cfgFile == null || !cfgFile.exists()) {
            return null;
        }
        String parent = cfgFile.getParent();
        String name = cfgFile.getName();
        int i = name.lastIndexOf(".");
        if (i > 0) {
            String substring = name.substring(0, i);
            File dat = new File(parent, substring + "." + ComtradeInfo.DAT_SUFFIX);
            if (dat.exists()) {
                return dat;
            } else {
                dat = new File(parent, substring + "." + ComtradeInfo.DAT_SUFFIX_UPPER);
                if (dat.exists()) {
                    return dat;
                }
            }
        }
        return null;
    }

    /**
     * 已知数据文件路径,换算配置文件路径
     *
     * @param datPath 数据文件路径
     * @return 配置文件路径
     */
    public static File datFile2CfgFile(String datPath) {
        if (datPath == null) {
            return null;
        }
        return datFile2CfgFile(new File(datPath));
    }

    /**
     * 已知数据文件路径,换算配置文件路径
     *
     * @param datFile 数据文件路径
     * @return 配置文件路径
     */
    public static File datFile2CfgFile(File datFile) {
        if (datFile == null || !datFile.exists()) {
            return null;
        }
        String parent = datFile.getParent();
        String name = datFile.getName();
        int i = name.lastIndexOf(".");
        if (i > 0) {
            String substring = name.substring(0, i);
            File cfg = new File(parent, substring + "." + ComtradeInfo.CFG_SUFFIX);
            if (cfg.exists()) {
                return cfg;
            } else {
                cfg = new File(parent, substring + "." + ComtradeInfo.CFG_SUFFIX_UPPER);
                if (cfg.exists()) {
                    return cfg;
                }
            }
        }
        return null;
    }

}
