package cn.com.swain.support.protocolEngine.datagram.escape.XX;

import cn.com.swain.support.protocolEngine.ProtocolBuild;
import cn.com.swain.support.protocolEngine.ProtocolCode;
import cn.com.swain.support.protocolEngine.datagram.escape.EscapeDataArray;

/**
 * author: Guoqiang_Sun
 * date: 2019/1/24 0024
 * Desc:
 * <p>
 * * * 转义前	           转义后
 * STX - {@link ProtocolBuild.XX#STX}（帧头） 	STX 转成 ESC 和 STX_ESC{@link ProtocolBuild.XX#STX_ESC}
 * ETX - {@link ProtocolBuild.XX#ETX}（帧尾）	ETX 转成 ESC 和 ETX_ESC{@link ProtocolBuild.XX#ETX_ESC}
 * ESC - {@link ProtocolBuild.XX#ESC}（转义符）	ESC 转成 ESC 和 ESC_ESC{@link ProtocolBuild.XX#ESC_ESC}
 */
public class XXEscapeDataArray extends EscapeDataArray {


    private static final byte STX = ProtocolBuild.XX.STX;
    private static final byte ETX = ProtocolBuild.XX.ETX;
    private static final byte ESC = ProtocolBuild.XX.ESC;

    private static final byte STX_ESC = ProtocolBuild.XX.STX_ESC;
    private static final byte ETX_ESC = ProtocolBuild.XX.ETX_ESC;
    private static final byte ESC_ESC = ProtocolBuild.XX.ESC_ESC;


    public XXEscapeDataArray(int size) {
        super(size);
    }

    @Override
    public boolean checkIsSpecialByte(byte b) {
        return b == STX || b == ETX || b == ESC;
    }

    @Override
    public boolean isHeadByte(byte b) {
        return b == STX;
    }

    @Override
    public boolean isTailByte(byte b) {
        return b == ETX;
    }

    @Override
    public boolean isEscapeByte(byte b) {
        return b == ESC;
    }

    private byte lastByte = EMPTY; // 记录上一个字节

    @Override
    public void onAddDataReverse(byte b) {

        if (!isEscapeByte(b)) {

            // 当前不是ESC
            // 看上一个字节是否是 ESC

            if (isEscapeByte(lastByte)) {

                switch (b) {
                    case STX_ESC:

                        // Tlog.e(TAG, "reverse " + STX_ESC);
                        onDataAddNoER(STX);

                        break;
                    case ETX_ESC:

                        // Tlog.e(TAG, "reverse " + ETX_ESC);
                        onDataAddNoER(ETX);

                        break;
                    case ESC_ESC:

                        // Tlog.e(TAG, "reverse " + ESC_ESC);
                        onDataAddNoER(ESC);

                        break;
                    default:
                        // 当前字节不是STX_ESC ETX_ESC ESC_ESC 记得把lastByte也加进来
                        onDataAddNoER(lastByte);
                        onDataAddNoER(b);
                        break;
                }
            } else {
                onDataAddNoER(b);
            }

        } else {

            // ignore

            // 当前是ESC

            // 看下一个字节

            // 这里不加到集合里面去，等下一次判断再加

        }

        lastByte = b;

    }

    @Override
    public void onAddDataEscape(byte b) {
        // Tlog.v(TAG, "--- > escape b : " + b);

        switch (b) {
            case STX:

                // Tlog.e(TAG, "escape STX ");

                onDataAddNoER(ESC);
                onDataAddNoER(STX_ESC);

                break;

            case ETX:

                // Tlog.e(TAG, "escape ETX ");

                onDataAddNoER(ESC);
                onDataAddNoER(ETX_ESC);

                break;
            case ESC:

                // Tlog.e(TAG, "escape ESC ");

                onDataAddNoER(ESC);
                onDataAddNoER(ESC_ESC);

                break;

            default:

                onDataAddNoER(b);

                break;
        }

    }

}
