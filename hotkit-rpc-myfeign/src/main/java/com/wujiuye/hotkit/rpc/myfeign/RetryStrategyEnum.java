package com.wujiuye.hotkit.rpc.myfeign;

import com.wujiuye.hotkit.rpc.myfeign.okhttp.HttpSupperRetryAction;

/**
 * 重试策略枚举
 *
 * @author wujiuye 2020/07/01
 */
public enum RetryStrategyEnum {

    /**
     * 请求响应http状态码非2xx
     */
    ON_HTTP_STATUS_NOT_OK((httpStatus, exception) -> 200 != httpStatus),

    /**
     * 请求出现异常
     */
    ON_ANY_EXCEPTION((response, exception) -> exception != null),
    ;

    /**
     * 规则
     */
    private HttpSupperRetryAction.RetryRule retryRule;

    RetryStrategyEnum(HttpSupperRetryAction.RetryRule retryRule) {
        this.retryRule = retryRule;
    }

    public HttpSupperRetryAction.RetryRule getRetryRule() {
        return retryRule;
    }

}
