package com.wujiuye.hotkit.util.system.jvm;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.util.Util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * wujiuye
 *
 * @author wujiuye 2020/06/18
 */
class OsInfoHelper {

    /**
     * 获取cpu信息
     *
     * @param systemInfo
     * @return
     */
    public static MachineInfo.CpuInfo printCpuInfo(SystemInfo systemInfo) {
        CentralProcessor cpu = systemInfo.getHardware().getProcessor();
        MachineInfo.CpuInfo cpuInfo = new MachineInfo.CpuInfo();
        long[] prevTicks = cpu.getSystemCpuLoadTicks();
        // 需要休眠点时间，等待CentralProcessor获取到ticks信息。
        Util.sleep(1000);
        long[] ticks = cpu.getSystemCpuLoadTicks();
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        // cpu系统使用率
        long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        // cpu用户使用率
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        // cpu当前等待率
        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        // cpu当前总的使用率
        long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
        cpuInfo.setCoreNumber(cpu.getLogicalProcessorCount());
        cpuInfo.setCpu(cpu.getName());
        cpuInfo.setUseRate(new BigDecimal(1.0 - (idle * 1.0 / totalCpu))
                .setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
        return cpuInfo;
    }

    /**
     * 获取内存信息
     *
     * @param systemInfo
     * @return
     */
    public static MachineInfo.MemoryInfo printMemoryInfo(SystemInfo systemInfo) {
        GlobalMemory mem = systemInfo.getHardware().getMemory();
        MachineInfo.MemoryInfo memoryInfo = new MachineInfo.MemoryInfo();
        memoryInfo.setMaxCapacit( ByteFormatUtils.formatByte(mem.getTotal()));
        memoryInfo.setCurrentUse(ByteFormatUtils.formatByte(mem.getTotal() - mem.getAvailable()));
        memoryInfo.setUseRate(new BigDecimal(mem.getTotal() - mem.getAvailable())
                .divide(new BigDecimal(mem.getTotal()),
                        2, BigDecimal.ROUND_DOWN).doubleValue());
        return memoryInfo;
    }

    /**
     * 获取linux磁盘分区使用信息
     */
    public static List<MachineInfo.DiskInfo> printDiskUsage(SystemInfo systemInfo) {
        List<MachineInfo.DiskInfo> distInfoList = new ArrayList<>();
        FileSystem fileSystem = systemInfo.getOperatingSystem().getFileSystem();
        OSFileStore[] fsArray = fileSystem.getFileStores();
        for (OSFileStore fs : fsArray) {
            long free = fs.getUsableSpace();
            long total = fs.getTotalSpace();
            long used = total - free;
            MachineInfo.DiskInfo diskInfo = new MachineInfo.DiskInfo();
            diskInfo.setDirName(fs.getMount());
            diskInfo.setType(fs.getType());
            diskInfo.setName(fs.getName());
            diskInfo.setFree(ByteFormatUtils.formatByte(free));
            diskInfo.setUsed(ByteFormatUtils.formatByte(used));
            diskInfo.setTotal(ByteFormatUtils.formatByte(total));
            diskInfo.setUseRate(new BigDecimal(used)
                    .divide(new BigDecimal(total),
                            2, BigDecimal.ROUND_DOWN).doubleValue());
            distInfoList.add(diskInfo);
        }
        return distInfoList;
    }

}
