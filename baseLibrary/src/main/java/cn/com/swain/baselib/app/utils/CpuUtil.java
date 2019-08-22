package cn.com.swain.baselib.app.utils;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import cn.com.swain.baselib.log.Tlog;

/**
 * author: Guoqiang_Sun
 * date: 2018/11/27 0027
 * Desc:
 */
public class CpuUtil {


    /**
     * 获取当前进程pid
     */
    public static long getCurUseCpuTime() {
        return getPidUseCpuTime(-1);
    }

    /**
     * 获取指定pid 应用占用的CPU时间
     */
    public static long getPidUseCpuTime(int pid) {
        if (pid < 0) {
            pid = android.os.Process.myPid();
        }

        String[] cpuInfos;
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream("/proc/" + pid + "/stat")), 1000);
            String load = reader.readLine();
            // Loger.d(" getPidUseCpu : " + load);
            reader.close();
            cpuInfos = load.split(" ");
        } catch (IOException e) {
            return 0;
        }

        try {
            return Long.parseLong(cpuInfos[13]) + Long.parseLong(cpuInfos[14]) + Long.parseLong(cpuInfos[15])
                    + Long.parseLong(cpuInfos[16]);
        } catch (ArrayIndexOutOfBoundsException e) {
            Tlog.w("getPidUseCpuTime()", e);
            return 0;
        }

    }

    /**
     * 获取系统总CPU使用时间
     */
    public static long getTotalCpuTime() {
        String[] cpuInfos = getTotalCpuStr().split(" ");
        long totalCpu = 0;
        if (cpuInfos.length > 8) {
            totalCpu = Long.parseLong(cpuInfos[2]) + Long.parseLong(cpuInfos[3]) + Long.parseLong(cpuInfos[4])
                    + Long.parseLong(cpuInfos[6]) + Long.parseLong(cpuInfos[5]) + Long.parseLong(cpuInfos[7])
                    + Long.parseLong(cpuInfos[8]);
        }
        return totalCpu;
    }

    /**
     * 获取系统总CPU使用时间
     */

    public static String getTotalCpuStr() {
        String load;
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream("/proc/stat")),
                    1000);
            load = reader.readLine();

            reader.close();
        } catch (IOException ex) {
            load = " ";
            Tlog.w("getTotalCpuStr()", ex);
        }

        return load;

    }

    /**
     * 获取当前进程cpu使用率
     */
    public static float getCurProcessCpuRate() {
        return getCurProcessCpuRate(1000);
    }

    /**
     * 获取当前进程cpu使用率 %
     */
    public static float getCurProcessCpuRate(long sleep) {
        long curUseCpu = CpuUtil.getCurUseCpuTime();
        long totalCpuTime = CpuUtil.getTotalCpuTime();

        if (sleep <= 0) {
            sleep = 1000L;
        }
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long totalCpuTime1 = CpuUtil.getTotalCpuTime();

        long totalCpuDiff = (totalCpuTime1 - totalCpuTime);
        if (totalCpuDiff <= 0) {
            return 0;
        }

        long curUseCpu1 = CpuUtil.getCurUseCpuTime();

        if (curUseCpu1 <= 0) {
            curUseCpu1 = curUseCpu;
        }

        long curCpuDiff = (curUseCpu1 - curUseCpu);

        if (curCpuDiff < 0) {
            curCpuDiff = 0;
        }

        return (curCpuDiff * 100f) / totalCpuDiff;
    }

}
