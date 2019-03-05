package cn.com.swain.support.protocolEngine.DataInspector;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.com.swain.baselib.log.Tlog;
import cn.com.swain.support.protocolEngine.datagram.SocketDataArray;
import cn.com.swain.support.protocolEngine.resolve.AbsProtocolProcessor;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/11 0011
 * desc :
 */

public class DataInspectorPool extends AbsDataInspector {

    private final AbsDataInspector mCallBack;
    private final Map<String, ExecutorService> mPoolMap;
    private final int poolSize;

    public DataInspectorPool(AbsDataInspector mCallBack, int poolSize) {
        this.mCallBack = mCallBack;
        if (poolSize <= 0) {
            poolSize = 1;
        }
        this.poolSize = poolSize;
        this.mPoolMap = Collections.synchronizedMap(new HashMap<String, ExecutorService>());
    }

    private final Object synObj = new byte[1];

    private ExecutorService getPool(String id) {
        ExecutorService executorService = mPoolMap.get(id);
        if (executorService == null) {
            synchronized (synObj) {
                executorService = mPoolMap.get(id);
                if (executorService == null) {
                    executorService = Executors.newFixedThreadPool(poolSize);
                    mPoolMap.put(id, executorService);
                }
            }
        }
        return executorService;
    }

    @Override
    public void release() {
        Tlog.e(AbsProtocolProcessor.TAG, " DataInspectorPool.release()");
        synchronized (synObj) {
            if (mPoolMap == null) {
                return;
            }
            for (Map.Entry<String, ExecutorService> entries : mPoolMap.entrySet()) {
                ExecutorService value = entries.getValue();
                if (value != null) {
                    value.shutdownNow();
                }
            }
            mPoolMap.clear();
        }
        if (mCallBack != null) {
            mCallBack.release();
        }
    }


    @Override
    public void inspectData(int code, SocketDataArray mSocketDataArray) {

        String id = mSocketDataArray != null ? mSocketDataArray.getID() : null;
        ExecutorService pool = getPool(id);
        if (pool != null) {
            pool.execute(new DataResolveRun(mCallBack, code, mSocketDataArray));
        } else {
            Tlog.e(AbsProtocolProcessor.TAG, " DataInspectorPool inspectData pool=null");
        }
    }

    private final class DataResolveRun implements Runnable {

        private AbsDataInspector mCallBack;
        private int code;
        private SocketDataArray mSocketDataArray;

        DataResolveRun(AbsDataInspector mCallBack, int code, SocketDataArray mSocketDataArray) {
            this.mCallBack = mCallBack;
            this.code = code;
            this.mSocketDataArray = mSocketDataArray;
        }

        @Override
        public void run() {
            mCallBack.inspectData(code, mSocketDataArray);
        }
    }

}
