package com.wujiuye.hotkit.util.system;

import java.lang.management.ManagementFactory;

/**
 * 当前进程ID工具类
 *
 * @author wujiuye
 * @version 1.0 2019/9/7
 */
public class ProcessIdUtils {

    /**
     * 当前进程的id
     */
    public final static int PID;

    static {
        PID = getCurrentPid();
    }

    /**
     * 获取当前的进程id
     *
     * @return
     */
    private static int getCurrentPid() {
        try {
            String name = ManagementFactory.getRuntimeMXBean().getName();
            String[] pidAndInfo = name.split("@");
            return Integer.valueOf(pidAndInfo[0]);
        } catch (Exception e) {
            return -1;
        }
    }

}
