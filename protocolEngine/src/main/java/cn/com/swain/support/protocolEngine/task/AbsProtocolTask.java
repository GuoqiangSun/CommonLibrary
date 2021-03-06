package cn.com.swain.support.protocolEngine.task;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/9 0009
 * desc :
 */

public abstract class AbsProtocolTask<param, result> {


    /**
     * 执行任务
     *
     * @param mParam param
     */
    public void execute(param mParam) {

        result mResult = onBackgroundTask(mParam);

        onPostExecute(mResult);
    }

    /**
     * @param mParam 参数
     * @return result 结果
     */
    protected abstract result onBackgroundTask(param mParam);

    protected void onPostExecute(result mResult) {

    }

}
