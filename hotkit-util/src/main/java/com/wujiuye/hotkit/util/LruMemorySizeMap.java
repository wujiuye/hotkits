package com.wujiuye.hotkit.util;

import org.openjdk.jol.info.ClassLayout;

import java.util.Collection;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 实现LRU缓存淘汰策略的Map
 *
 * @author wujiuye 2021/01/12
 */
public class LruMemorySizeMap<K, V> {

    protected final transient int maxMemoryBytes;
    protected final transient ConcurrentHashMap<K, V> map;
    private final transient Queue<K> lruQueue;
    protected transient AtomicLong curUseMemorySize = new AtomicLong(0);

    private static long ITEM_SIZE;

    static {
        ClassLayout classLayout = ClassLayout.parseClass(Map.Entry.class);
        ITEM_SIZE = classLayout.instanceSize();
    }

    /**
     * @param maxMemoryMb 指定MAP最大占用的内存
     */
    public LruMemorySizeMap(int maxMemoryMb) {
        this.map = new ConcurrentHashMap<>(16, 0.8F, 4);
        this.maxMemoryBytes = maxMemoryMb * 1024 * 1024;
        this.lruQueue = new ConcurrentLinkedQueue<>();
    }

    private void incUseMemorySize(K key, V value) {
        long size = ITEM_SIZE + sizeOf(key) + sizeOf(value);
        curUseMemorySize.addAndGet(size);
    }

    private void decUseMemorySize(K key, V value) {
        long size = ITEM_SIZE + sizeOf(key) + sizeOf(value);
        curUseMemorySize.getAndAdd(-size);
    }

    public V put(K key, V value) {
        if (this.curUseMemorySize.get() >= this.maxMemoryBytes) {
            synchronized (this) {
                if (this.curUseMemorySize.get() >= this.maxMemoryBytes) {
                    this.lruRemove();
                }
            }
        }
        putKeyToQueue(key);
        V old = this.map.put(key, value);
        if (old == null) {
            incUseMemorySize(key, value);
        }
        return old;
    }

    public void remove(K key) {
        V value = this.map.get(key);
        this.map.remove(key);
        if (value != null) {
            decUseMemorySize(key, value);
        }
    }

    public V get(K key) {
        putKeyToQueue(key);
        return this.map.get(key);
    }

    private void putKeyToQueue(K key) {
        lruQueue.remove(key);
        lruQueue.add(key);
    }

    private void lruRemove() {
        K key;
        while (lruQueue.size() > 0 && (key = lruQueue.poll()) != null) {
            V value = this.map.get(key);
            this.map.remove(key);
            if (value != null) {
                decUseMemorySize(key, value);
            }
            if (curUseMemorySize.get() < maxMemoryBytes) {
                break;
            }
        }
    }

    private static long sizeOf(Object object) {
        if (object instanceof Collection) {
            return sizeOf((Collection<?>) object);
        } else if (object instanceof String) {
            ClassLayout classLayout = ClassLayout.parseClass(char.class);
            return ((String) object).length() * classLayout.instanceSize() + sizeOf(String.class);
        } else {
            return sizeOf(object.getClass());
        }
    }

    private static long sizeOf(Class<?> objectClass) {
        ClassLayout classLayout = ClassLayout.parseClass(objectClass);
        return classLayout.instanceSize();
    }

    private static long sizeOf(Collection<?> collection) {
        if (collection.isEmpty()) {
            return sizeOf(collection.getClass());
        }
        return sizeOf(collection.getClass())
                + collection.parallelStream().map(LruMemorySizeMap::sizeOf).reduce(Long::sum).get();
    }

}
