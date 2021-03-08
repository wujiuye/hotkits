package com.wujiuye.hotkit.util.concurrent;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wujiuye 2020/07/27
 */
public class NameThreadFactory implements ThreadFactory {

    /**
     * 所属组
     */
    private final ThreadGroup group;
    /**
     * 是否守护线程
     */
    private boolean daemon;
    /**
     * 线程名称前缀
     */
    private String threadNamePrefix;
    private AtomicInteger threadCount = new AtomicInteger(0);

    public NameThreadFactory(String prefix, boolean daemon) {
        this.daemon = daemon;
        this.threadNamePrefix = prefix;
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() :
                Thread.currentThread().getThreadGroup();
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(group, r, threadNamePrefix + threadCount.getAndIncrement());
        thread.setDaemon(daemon);
        return thread;
    }

}
