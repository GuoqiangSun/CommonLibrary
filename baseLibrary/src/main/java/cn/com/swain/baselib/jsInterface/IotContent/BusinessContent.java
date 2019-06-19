package cn.com.swain.baselib.jsInterface.IotContent;

/**
 * author Guoqiang_Sun
 * date 2019/5/15
 * desc
 */
public class BusinessContent {

    /**
     * 两个字节
     * 客户
     */
    private int custom;

    /**
     * 两个字节
     * 产品
     */
    private int product;

    /**
     * 四个字节
     * 指令
     */
    private long cmd;

    /**
     * 两个字节
     * 结果
     */
    private int result;

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

    public long getCmd() {
        return cmd;
    }

    public void setCmd(long cmd) {
        this.cmd = cmd;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "BusinessContent{" +
                "custom=" + custom +
                ", product=" + product +
                ", cmd=" + cmd +
                ", result=" + result +
                '}';
    }
}
