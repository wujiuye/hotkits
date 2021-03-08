package com.wujiuye.hotkit.spring.common.executor;

import com.wujiuye.hotkit.spring.common.SpringContextHolder;
import org.springframework.core.task.AsyncTaskExecutor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.function.Supplier;

/**
 * 虚拟的信号量隔离的线程池上下文，通过SemaphoreExecutorContext创建虚拟信号量隔离的线程池
 *
 * @author wujiuye 2020/08/18
 */
public class SemaphoreExecutorContext {

    /**
     * 默认的，每个业务可并行占用的线程数，用于隔离业务之间线程使用情况
     * 避免因为一个业务同时占用全部线程，导致线程池被用完，其它业务无法执行的情况，
     * 也就是通过实现虚拟的线程池实现信号量隔离
     */
    private static final int MIN_THREADS = Integer.getInteger("business.threads",
            Runtime.getRuntime().availableProcessors());
    private static final Supplier<Integer> DEFAULT_THREADS = () -> MIN_THREADS;

    /**
     * 全局系统自适应线程池
     */
    private final static Supplier<AsyncTaskExecutor> executorSupplier =
            () -> SpringContextHolder.getBean(AsyncTaskExecutor.class);

    /**
     * 缓存业务ID与信号量的映射
     */
    private static Map<String, Semaphore> businessSemaphoreMap = new HashMap<>();

    /**
     * 获取信号量隔离的虚拟线程池
     *
     * @param businessId 业务ID(可以是任意类型对象，建议声明为常量，且全局唯一)
     * @return
     */
    public static SemaphoreExecutorService getExecutor(Object businessId) {
        AsyncTaskExecutor executor = executorSupplier.get();
        return new SemaphoreExecutorService(executor, getSemaphore(generateBusinessId(businessId),
                getBusinessThreads(businessId)));
    }

    /**
     * 获取信号量隔离的虚拟线程池
     *
     * @param businessId 业务ID(可以是任意类型对象，建议声明为常量，且全局唯一)
     * @param handler    异常处理器，用于捕捉中断异常，或者未try-catch的业务代码抛出的异常
     * @return
     */
    public static SemaphoreExecutorService getExecutor(Object businessId, Thread.UncaughtExceptionHandler handler) {
        AsyncTaskExecutor executor = executorSupplier.get();
        SemaphoreExecutorService semaphoreExecutorService = new SemaphoreExecutorService(executor, getSemaphore(generateBusinessId(businessId),
                getBusinessThreads(businessId)));
        semaphoreExecutorService.setUncaughtExceptionHandler(handler);
        return semaphoreExecutorService;
    }

    private static String generateBusinessId(Object businessBean) {
        if (businessBean instanceof String) {
            return (String) businessBean;
        }
        BusinessThread businessThread = businessBean.getClass().getAnnotation(BusinessThread.class);
        return "".equalsIgnoreCase(businessThread.businessId()) ?
                businessBean.getClass().getName() : businessThread.businessId();
    }

    private static Supplier<Integer> getBusinessThreads(Object businessId) {
        BusinessThread businessThread = businessId.getClass().getAnnotation(BusinessThread.class);
        if (businessThread == null) {
            return DEFAULT_THREADS;
        }
        return () -> Math.max(businessThread.threads(), MIN_THREADS);
    }

    private static Semaphore getSemaphore(String businessId, Supplier<Integer> threads) {
        Semaphore semaphore = businessSemaphoreMap.get(businessId);
        if (semaphore == null) {
            synchronized (SemaphoreExecutorContext.class) {
                if (!businessSemaphoreMap.containsKey(businessId)) {
                    semaphore = new Semaphore(threads.get());
                    Map<String, Semaphore> newMap = new HashMap<>(businessSemaphoreMap);
                    newMap.put(businessId, semaphore);
                    businessSemaphoreMap = newMap;
                }
            }
            semaphore = businessSemaphoreMap.get(businessId);
        }
        return semaphore;
    }

}
