package cn.com.swain.baselib.jsInterface.CommonContent.qx;

/**
 * author Guoqiang_Sun
 * date 2019/5/15
 * desc
 */
public class QXBusinessContent {

    /**
     * 两个字节
     * 客户
     */
    public int custom;

    /**
     * 两个字节
     * 产品
     */
    public int product;

    /**
     * 四个字节
     * 指令
     */
    public int cmd;

    public int getCustom() {
        return custom;
    }

    public void setCustom(int custom) {
        this.custom = custom;
    }

    public int getProduct() {
        return product;
    }

    public void setProduct(int product) {
        this.product = product;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    @Override
    public String toString() {
        return "QXBusinessContent{" +
                "custom=" + custom +
                ", product=" + product +
                ", cmd=" + cmd +
                '}';
    }
}
