package com.wujiuye.hotkit.rpc.playback;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 文件锁
 *
 * @author wujiuye 2020/07/31
 */
public class FileLockUtils {

    private final static Map<String, ReentrantLock> FILE_LOCK_MAP = new ConcurrentHashMap<>();

    public static void lock(File file) {
        ReentrantLock lock = FILE_LOCK_MAP.get(file.getAbsolutePath());
        if (lock == null) {
            lock = FILE_LOCK_MAP.computeIfAbsent(file.getAbsolutePath(), s -> new ReentrantLock());
        }
        lock.lock();
    }

    public static void unlock(File file) {
        ReentrantLock lock = FILE_LOCK_MAP.remove(file.getAbsolutePath());
        if (lock != null) {
            lock.unlock();
        }
    }

}
