package com.wujiuye.hotkit.util.system.jvm;

import com.wujiuye.hotkit.util.system.ProcessIdUtils;
import com.wujiuye.hotkit.util.StringUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @author wujiuye
 * @version 1.0 on 2019/9/8 {描述：}
 */
class JvmInfoHelper {

    /**
     * 获取jvm信息
     *
     * @return
     */
    public static MachineInfo.JvmInfo printJvmInfo() {
        Properties props = System.getProperties();
        Runtime runtime = Runtime.getRuntime();
        // jvm总内存
        long jvmTotalMemoryByte = runtime.totalMemory();
        // jvm最大可申请内存
        long jvmMaxMoryByte = runtime.maxMemory();
        // 可用内存（空闲）
        long freeMemoryByte = runtime.freeMemory();
        // jdk版本
        String jdkVersion = props.getProperty("java.version");
        // jdk路径
        String jdkHome = props.getProperty("java.home");
        MachineInfo.JvmInfo jvmInfo = new MachineInfo.JvmInfo();
        jvmInfo.setHome(jdkHome);
        jvmInfo.setVersion(jdkVersion);
        jvmInfo.setTotal(ByteFormatUtils.formatByte(jvmTotalMemoryByte));
        jvmInfo.setMax(ByteFormatUtils.formatByte(jvmMaxMoryByte));
        jvmInfo.setFree(ByteFormatUtils.formatByte(freeMemoryByte));
        jvmInfo.setUseRate(new BigDecimal((jvmTotalMemoryByte - freeMemoryByte) * 1.0 / jvmTotalMemoryByte)
                .setScale(2, RoundingMode.DOWN).doubleValue());
        return jvmInfo;
    }


    /**
     * 打印线程信息
     *
     * @return
     */
    public static MachineInfo.VmThreadStack printThreadInfo() {
        MachineInfo.VmThreadStack threadInfo = new MachineInfo.VmThreadStack();
        ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();
        while (currentGroup.getParent() != null) {
            // 返回此线程组的父线程组
            currentGroup = currentGroup.getParent();
        }
        //此线程组中活动线程的估计数
        int noThreads = currentGroup.activeCount();
        threadInfo.setTotal(noThreads);
        threadInfo.setDetailInfos(new ArrayList<>());
        Thread[] lstThreads = new Thread[noThreads];
        //把对此线程组中的所有活动子组的引用复制到指定数组中。
        currentGroup.enumerate(lstThreads);
        for (Thread thread : lstThreads) {
            MachineInfo.VmThreadStack.ThreadDetailInfo detailInfo = new MachineInfo.VmThreadStack.ThreadDetailInfo();
            detailInfo.setThreadId(thread.getId());
            detailInfo.setThreadName(thread.getName());
            detailInfo.setThreadStatus(thread.getState().name());
            threadInfo.getDetailInfos().add(detailInfo);
        }
        return threadInfo;
    }


    /**
     * 打印GC信息
     *
     * @return
     */
    public static MachineInfo.GcInfo printGcUtilInfo() {
        BufferedReader bufferedReader;
        try {
            String javaHome = System.getProperty("java.home");
            if (javaHome.contains("/jre")) {
                javaHome = javaHome.substring(0, javaHome.lastIndexOf("/jre"));
            }
            Process process = Runtime.getRuntime().exec(javaHome + "/bin/jstat -gcutil " + ProcessIdUtils.PID);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(process.getInputStream());
            bufferedReader = new BufferedReader(new InputStreamReader(bufferedInputStream));

            List<String> lineList = new ArrayList<>();
            // command log
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lineList.add(line);
            }
            // command exit
            process.waitFor();
            if (process.exitValue() == 0 && lineList.size() == 2) {
                MachineInfo.GcInfo gcInfo = new MachineInfo.GcInfo();
                String[] info = Arrays.stream(lineList.get(1).split(" "))
                        .filter(s -> !StringUtils.isNullOrEmpty(s))
                        .toArray(String[]::new);
                // S0, S1, E, O, M, CCS, YGC, YGCT, FGC, FGCT, GCT;
                gcInfo.setS0(Double.parseDouble(info[0]));
                gcInfo.setS1(Double.parseDouble(info[1]));
                gcInfo.setE(Double.parseDouble(info[2]));
                gcInfo.setO(Double.parseDouble(info[3]));
                gcInfo.setM(Double.parseDouble(info[4]));
                gcInfo.setCCS(Double.parseDouble(info[5]));
                gcInfo.setYGC(Double.parseDouble(info[6]));
                gcInfo.setYGCT(Double.parseDouble(info[7]));
                gcInfo.setFGC(Double.parseDouble(info[8]));
                gcInfo.setFGCT(Double.parseDouble(info[9]));
                gcInfo.setGCT(Double.parseDouble(info[10]));
                return gcInfo;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
