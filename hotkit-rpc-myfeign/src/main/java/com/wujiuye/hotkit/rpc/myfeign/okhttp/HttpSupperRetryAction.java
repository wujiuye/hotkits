package com.wujiuye.hotkit.rpc.myfeign.okhttp;

import java.io.IOException;

/**
 * 用于支持重试
 *
 * @author wujiuye 2020/05/27
 */
@FunctionalInterface
public interface HttpSupperRetryAction {

    /**
     * 使用自定义的重试规则
     */
    interface RetryRule {
        /**
         * 是否需要重试
         *
         * @param httpStatus 响应状态码
         * @param exception  异常
         * @return
         */
        boolean allowRetry(int httpStatus, Exception exception);
    }

    /**
     * 将http请求操作封装到doAction方法执行
     *
     * @return 响应结果
     * @throws IOException
     */
    HttpResponse rpcInvoke() throws IOException;

}
