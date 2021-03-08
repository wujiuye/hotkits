package com.wujiuye.hotkit.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author wujiuye
 * @version 1.0 on 2019/9/25 {描述：
 * BitMap实现的整数容器，可用于过滤
 * (数据量少且数据比较分散的情况下不建议使用)
 * 8*4=32byte  1个Map.Entry=4(value)+4(key)+4=12
 * }
 */
public class Bitmap {

    private Map<Integer, Byte> byteMap = new HashMap<>();
    private volatile int realSize;
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * 获取当前元素总数
     *
     * @return
     */
    public int getRealSize() {
        return realSize;
    }

    /**
     * 写入一个元素
     *
     * @param obj
     * @return 存在返回false，不存在返回true
     */
    public boolean set(Integer obj) {
        lock.writeLock().lock();
        try {
            int byteIndex = obj >> 3;
            if (!byteMap.containsKey(byteIndex)) {
                byteMap.put(byteIndex, (byte) 0x00);
            }
            byte map = byteMap.get(byteIndex);
            int inByteIndex = obj % 8;
            // 0x0000 0000
            byte target = (byte) (0x01 << inByteIndex);
            boolean isExist = (map & target) == target;
            map |= target;
            byteMap.put(byteIndex, map);
            if (!isExist) {
                ++realSize;
            }
            return !isExist;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 判断元素是否存在（时间复杂度比HashMap更优）
     *
     * @param obj
     * @return
     */
    public boolean contains(Integer obj) {
        lock.readLock().lock();
        try {
            int byteIndex = obj >> 3;
            if (!byteMap.containsKey(byteIndex)) {
                return false;
            }
            byte map = byteMap.get(byteIndex);
            int inByteIndex = obj % 8;
            // 0x0000 0000
            byte target = (byte) (0x01 << inByteIndex);
            return (map & target) == target;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * 移除一个元素
     *
     * @param obj
     * @return 存在返回true，否则返回false
     */
    public boolean remove(Integer obj) {
        lock.writeLock().lock();
        try {
            int byteIndex = obj >> 3;
            if (!byteMap.containsKey(byteIndex)) {
                return false;
            }
            byte map = byteMap.get(byteIndex);
            int inByteIndex = obj % 8;
            // 0x0000 0000
            byte target = (byte) (0x01 << inByteIndex);
            boolean isExist = (map & target) == target;
            if (isExist) {
                map ^= target;
                byteMap.put(byteIndex, map);
                --realSize;
            }
            return isExist;
        } finally {
            lock.writeLock().unlock();
        }
    }

}
