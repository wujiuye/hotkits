package com.wujiuye.hotkit.redis.util;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;

/**
 * @author wujiuye 2020/09/14
 */
public class HostUtils {

    private final static String HOSTNAME;
    private final static int PID;

    static {
        String hostName;
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            hostName = "";
        }
        HOSTNAME = hostName;
        PID = getProcessId();
    }

    private static Integer getProcessId() {
        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        System.out.println(runtimeMxBean.getName());
        return Integer.valueOf(runtimeMxBean.getName().split("@")[0]);
    }

    public static String getHostNamePid() {
        return HOSTNAME + ":" + PID;
    }

}
