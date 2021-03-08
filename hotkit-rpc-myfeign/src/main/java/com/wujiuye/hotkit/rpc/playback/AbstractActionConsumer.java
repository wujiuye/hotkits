package com.wujiuye.hotkit.rpc.playback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 操作重复消费
 *
 * @author wujiuye 2020/07/20
 */
abstract class AbstractActionConsumer implements Iterable<ActionRecord> {

    private final static Logger logger = LoggerFactory.getLogger("playback");

    /**
     * 处理回放
     */
    private Playback playback;
    /**
     * 执行action的线程池
     */
    private ExecutorService executorService;
    /**
     * 监听器
     */
    private List<ActionConsumListener> consumListeners;

    public AbstractActionConsumer(ExecutorService executorService,
                                  List<ActionConsumListener> consumListeners) {
        this.playback = new ReflectPlayback();
        this.executorService = executorService;
        this.consumListeners = consumListeners;
    }

    /**
     * 开始消费日记
     */
    public void consumLog() {
        logger.debug("====> 开始消费日记");
        init();
        AtomicInteger count = new AtomicInteger(0);
        AtomicInteger realCount = new AtomicInteger(0);
        try {
            // 获取迭代器
            Iterator<ActionRecord> iterator = this.iterator();
            while (iterator.hasNext()) {
                count.incrementAndGet();
                ActionRecord record;
                try {
                    record = iterator.next();
                } catch (Exception e) {
                    count.decrementAndGet();
                    logger.error("====> 消费日记读取一行失败：{}", e.getLocalizedMessage());
                    continue;
                }
                // 重试够次数直接抛弃
                if (record.getPlaybackCount() >= record.getMaxPlaybackCount()) {
                    count.decrementAndGet();
                    continue;
                }
                record.setPlaybackCount(record.getPlaybackCount() + 1);
                submitAction(record, realCount, count);
            }
        } catch (Throwable e) {
            logger.error("消费日记异常结束：" + e.getLocalizedMessage(), e);
        } finally {
            this.finish();
            logger.info("====> 消费日记结束，实际消费日记总数：{}", realCount.get());
        }
    }

    /**
     * 提交日记到线程池回放
     *
     * @param record
     * @param realCount
     * @param count
     */
    private void submitAction(final ActionRecord record, AtomicInteger realCount, AtomicInteger count) {
        executorService.submit(() -> {
            try {
                realCount.incrementAndGet();
                Object result = playback.playback(record);
                if (result != null) {
                    this.success(record, result);
                } else {
                    this.fail(record, result, null);
                }
            } catch (Throwable e) {
                this.fail(record, null, e);
            } finally {
                count.decrementAndGet();
            }
        });
    }

    private void fail(ActionRecord record, Object result, Throwable e) {
        this.onFail(record, e);
        // 回调监听器
        if (consumListeners != null && !consumListeners.isEmpty()) {
            for (ActionConsumListener listener : consumListeners) {
                if (listener.suppor(record)) {
                    listener.onFail(record, null, result);
                }
            }
        }
    }

    /**
     * 开始消费之前调用
     */
    protected abstract void init();

    /**
     * 重放操作失败
     *
     * @param record
     * @param e
     */
    protected abstract void onFail(ActionRecord record, Throwable e);

    private void success(ActionRecord record, Object result) {
        record.setPlaybackCount(record.getPlaybackCount() + 1);
        this.onSuccess(record);
        // 回调监听器
        if (consumListeners != null && !consumListeners.isEmpty()) {
            for (ActionConsumListener listener : consumListeners) {
                if (listener.suppor(record)) {
                    listener.onSuccess(record, result);
                }
            }
        }
    }

    /**
     * 重放操作成功
     *
     * @param record
     */
    protected abstract void onSuccess(ActionRecord record);

    private void finish() {
        this.onFinish();
        // 回调监听器
        if (consumListeners != null && !consumListeners.isEmpty()) {
            for (ActionConsumListener listener : consumListeners) {
                listener.onFinish();
            }
        }
    }

    /**
     * 所有重放执行完成
     */
    protected abstract void onFinish();

}
