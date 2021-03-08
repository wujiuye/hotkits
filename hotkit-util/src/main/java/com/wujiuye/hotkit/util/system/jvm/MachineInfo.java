package com.wujiuye.hotkit.util.system.jvm;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wujiuye
 * @version 1.0 on 2019/7/29 机器当前状态信息，使用navtive方法获取
 * }
 */
public class MachineInfo {

    private LocalDateTime dateTime;
    private MemoryInfo memoryInfo;
    private CpuInfo cpuInfo;
    private List<DiskInfo> diskInfos;
    private JvmInfo jvmInfo;
    private VmThreadStack threadStack;
    private GcInfo gcInfo;

    public MachineInfo() {
        this.dateTime = LocalDateTime.now();
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public static class MemoryInfo {
        /**
         * 最大内存
         */
        private String maxCapacit;
        /**
         * 当前使用的内存
         */
        private String currentUse;
        /**
         * 当前内存使用率
         */
        private Double useRate;

        public String getMaxCapacit() {
            return maxCapacit;
        }

        public void setMaxCapacit(String maxCapacit) {
            this.maxCapacit = maxCapacit;
        }

        public String getCurrentUse() {
            return currentUse;
        }

        public void setCurrentUse(String currentUse) {
            this.currentUse = currentUse;
        }

        public Double getUseRate() {
            return useRate;
        }

        public void setUseRate(Double useRate) {
            this.useRate = useRate;
        }

        @Override
        public String toString() {
            return "MemoryInfo{" +
                    "maxCapacit='" + maxCapacit + '\'' +
                    ", currentUse='" + currentUse + '\'' +
                    ", useRate=" + useRate +
                    '}';
        }

    }

    public static class CpuInfo {
        /**
         * 处理器
         */
        private String cpu;
        /**
         * cpu核心数
         */
        private Integer coreNumber;
        /**
         * cpu使用率
         */
        private Double useRate;

        public String getCpu() {
            return cpu;
        }

        public void setCpu(String cpu) {
            this.cpu = cpu;
        }

        public Integer getCoreNumber() {
            return coreNumber;
        }

        public void setCoreNumber(Integer coreNumber) {
            this.coreNumber = coreNumber;
        }

        public Double getUseRate() {
            return useRate;
        }

        public void setUseRate(Double useRate) {
            this.useRate = useRate;
        }

        @Override
        public String toString() {
            return "CpuInfo{" +
                    "cpu='" + cpu + '\'' +
                    ", coreNumber=" + coreNumber +
                    ", useRate=" + useRate +
                    '}';
        }

    }

    /**
     * 系统硬盘分区信息
     */
    public static class DiskInfo {
        /**
         * 根目录
         */
        private String dirName;
        /**
         * 分区名
         */
        private String name;
        /**
         * 分区类型
         */
        private String type;
        /**
         * 磁盘最大容量
         */
        private String total;
        /**
         * 可用
         */
        private String free;
        /**
         * 已使用
         */
        private String used;
        /**
         * 当前使用率
         */
        private Double useRate;

        public String getDirName() {
            return dirName;
        }

        public void setDirName(String dirName) {
            this.dirName = dirName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getFree() {
            return free;
        }

        public void setFree(String free) {
            this.free = free;
        }

        public String getUsed() {
            return used;
        }

        public void setUsed(String used) {
            this.used = used;
        }

        public Double getUseRate() {
            return useRate;
        }

        public void setUseRate(Double useRate) {
            this.useRate = useRate;
        }

        @Override
        public String toString() {
            return "DiskInfo{" +
                    "dirName='" + dirName + '\'' +
                    ", name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    ", total='" + total + '\'' +
                    ", free='" + free + '\'' +
                    ", used='" + used + '\'' +
                    ", useRate=" + useRate +
                    '}';
        }

    }

    public static class JvmInfo {
        /**
         * JDK路径
         */
        private String home;
        /**
         * JDK版本
         */
        private String version;
        /**
         * 当前JVM占用的内存总数
         */
        private String total;
        /**
         * JVM最大可申请内存
         */
        private String max;
        /**
         * JVM空闲内存
         */
        private String free;
        /**
         * 内存使用率
         */
        private Double useRate;

        public String getHome() {
            return home;
        }

        public void setHome(String home) {
            this.home = home;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getMax() {
            return max;
        }

        public void setMax(String max) {
            this.max = max;
        }

        public String getFree() {
            return free;
        }

        public void setFree(String free) {
            this.free = free;
        }

        public Double getUseRate() {
            return useRate;
        }

        public void setUseRate(Double useRate) {
            this.useRate = useRate;
        }

        @Override
        public String toString() {
            return "JvmInfo{" +
                    "home='" + home + '\'' +
                    ", version='" + version + '\'' +
                    ", total='" + total + '\'' +
                    ", max='" + max + '\'' +
                    ", free='" + free + '\'' +
                    ", useRate=" + useRate +
                    '}';
        }

    }

    public static class VmThreadStack {

        /**
         * 线程总数
         */
        private Integer total;
        /**
         * 线程详情
         */
        private List<ThreadDetailInfo> detailInfos;

        public static class ThreadDetailInfo {
            /**
             * 线程id
             */
            private long threadId;
            /**
             * 线程名称
             */
            private String threadName;
            /**
             * 线程状态
             */
            private String threadStatus;

            public long getThreadId() {
                return threadId;
            }

            public void setThreadId(long threadId) {
                this.threadId = threadId;
            }

            public String getThreadName() {
                return threadName;
            }

            public void setThreadName(String threadName) {
                this.threadName = threadName;
            }

            public String getThreadStatus() {
                return threadStatus;
            }

            public void setThreadStatus(String threadStatus) {
                this.threadStatus = threadStatus;
            }

            @Override
            public String toString() {
                return "ThreadDetailInfo{" +
                        "threadId=" + threadId +
                        ", threadName='" + threadName + '\'' +
                        ", threadStatus='" + threadStatus + '\'' +
                        '}';
            }

        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public List<ThreadDetailInfo> getDetailInfos() {
            return detailInfos;
        }

        public void setDetailInfos(List<ThreadDetailInfo> detailInfos) {
            this.detailInfos = detailInfos;
        }

        @Override
        public String toString() {
            return "VmThreadStack{" +
                    "total=" + total +
                    ", detailInfos=" + detailInfos +
                    '}';
        }

    }

    public static class GcInfo {
        private Double S0, S1, E, O, M, CCS, YGC, YGCT, FGC, FGCT, GCT;

        public Double getS0() {
            return S0;
        }

        public void setS0(Double s0) {
            S0 = s0;
        }

        public Double getS1() {
            return S1;
        }

        public void setS1(Double s1) {
            S1 = s1;
        }

        public Double getE() {
            return E;
        }

        public void setE(Double e) {
            E = e;
        }

        public Double getO() {
            return O;
        }

        public void setO(Double o) {
            O = o;
        }

        public Double getM() {
            return M;
        }

        public void setM(Double m) {
            M = m;
        }

        public Double getCCS() {
            return CCS;
        }

        public void setCCS(Double CCS) {
            this.CCS = CCS;
        }

        public Double getYGC() {
            return YGC;
        }

        public void setYGC(Double YGC) {
            this.YGC = YGC;
        }

        public Double getYGCT() {
            return YGCT;
        }

        public void setYGCT(Double YGCT) {
            this.YGCT = YGCT;
        }

        public Double getFGC() {
            return FGC;
        }

        public void setFGC(Double FGC) {
            this.FGC = FGC;
        }

        public Double getFGCT() {
            return FGCT;
        }

        public void setFGCT(Double FGCT) {
            this.FGCT = FGCT;
        }

        public Double getGCT() {
            return GCT;
        }

        public void setGCT(Double GCT) {
            this.GCT = GCT;
        }

        @Override
        public String toString() {
            return "GcInfo{" +
                    "S0=" + S0 +
                    ", S1=" + S1 +
                    ", E=" + E +
                    ", O=" + O +
                    ", M=" + M +
                    ", CCS=" + CCS +
                    ", YGC=" + YGC +
                    ", YGCT=" + YGCT +
                    ", FGC=" + FGC +
                    ", FGCT=" + FGCT +
                    ", GCT=" + GCT +
                    '}';
        }

    }

    public MemoryInfo getMemoryInfo() {
        return memoryInfo;
    }

    public void setMemoryInfo(MemoryInfo memoryInfo) {
        this.memoryInfo = memoryInfo;
    }

    public CpuInfo getCpuInfo() {
        return cpuInfo;
    }

    public void setCpuInfo(CpuInfo cpuInfo) {
        this.cpuInfo = cpuInfo;
    }

    public List<DiskInfo> getDiskInfos() {
        return diskInfos;
    }

    public void setDiskInfos(List<DiskInfo> diskInfos) {
        this.diskInfos = diskInfos;
    }

    public JvmInfo getJvmInfo() {
        return jvmInfo;
    }

    public void setJvmInfo(JvmInfo jvmInfo) {
        this.jvmInfo = jvmInfo;
    }

    public VmThreadStack getThreadStack() {
        return threadStack;
    }

    public void setThreadStack(VmThreadStack threadStack) {
        this.threadStack = threadStack;
    }

    public GcInfo getGcInfo() {
        return gcInfo;
    }

    public void setGcInfo(GcInfo gcInfo) {
        this.gcInfo = gcInfo;
    }

    @Override
    public String toString() {
        return "MachineInfo{" +
                "dateTime=" + dateTime +
                ", memoryInfo=" + memoryInfo +
                ", cpuInfo=" + cpuInfo +
                ", diskInfos=" + diskInfos +
                ", jvmInfo=" + jvmInfo +
                ", threadStack=" + threadStack +
                ", gcInfo=" + gcInfo +
                '}';
    }

}
