package com.wujiuye.hotkit.util.loop;

import java.util.List;

/**
 * 批量处理
 *
 * @author wujiuye 2020/05/27
 */
@FunctionalInterface
public interface LoopHandler<T> {

    /**
     * 处理批量
     *
     * @param batchData 批量数据
     */
    void handle(List<T> batchData);

}
