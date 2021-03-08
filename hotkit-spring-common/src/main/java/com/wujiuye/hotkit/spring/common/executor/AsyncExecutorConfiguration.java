package com.wujiuye.hotkit.spring.common.executor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.task.AsyncTaskExecutor;

/**
 * 全局业务线程池配置
 *
 * @author wujiuye 2020/08/17
 */
@ConditionalOnProperty(name = "spring.system-load-auto-thread-pool-executor.enable", havingValue = "true")
@Configuration
public class AsyncExecutorConfiguration {

    /**
     * 全局业务线程池
     *
     * @return
     */
    @Bean
    @Lazy
    public AsyncTaskExecutor asyncTaskExecutor(@Value("${spring.system-load-auto-thread-pool-executor.load-threshol:0.90}") double systemLoadThreshol,
                                               @Value("${spring.system-load-auto-thread-pool-executor.cpu-use-rate-threshol:0.90}") double cpuUseRateThreshol,
                                               @Value("${spring.system-load-auto-thread-pool-executor.memory-use-rate-threshol:0.90}") double memoryUseRateThreshol) {
        // 系统自适应线程池
        SystemLoadAutoThreadPoolExecutor executor = new SystemLoadAutoThreadPoolExecutor(
                systemLoadThreshol, cpuUseRateThreshol, memoryUseRateThreshol, (r, executor1) -> r.run());
        executor.setThreadNamePrefix("async-executor");
        executor.setDaemon(true);
        return executor;
    }

}
