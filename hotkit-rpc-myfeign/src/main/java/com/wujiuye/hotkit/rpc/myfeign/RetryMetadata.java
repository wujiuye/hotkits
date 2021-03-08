package com.wujiuye.hotkit.rpc.myfeign;

import com.wujiuye.hotkit.rpc.myfeign.okhttp.HttpSupperRetryAction;

/**
 * 重试策略元数据
 *
 * @author wujiuye 2020/07/01
 */
public class RetryMetadata {

    /**
     * 是否开启了重试功能
     */
    private boolean enableRetry;
    /**
     * 重试策略
     */
    private HttpSupperRetryAction.RetryRule[] retryStrategys;
    /**
     * 重试最大次数
     */
    private int retryMaxNumber;

    public boolean isEnableRetry() {
        return enableRetry;
    }

    public void setEnableRetry(boolean enableRetry) {
        this.enableRetry = enableRetry;
    }

    public HttpSupperRetryAction.RetryRule[] getRetryStrategys() {
        return retryStrategys;
    }

    public void setRetryStrategys(HttpSupperRetryAction.RetryRule[] retryStrategys) {
        this.retryStrategys = retryStrategys;
    }

    public int getRetryMaxNumber() {
        return retryMaxNumber;
    }

    public void setRetryMaxNumber(int retryMaxNumber) {
        this.retryMaxNumber = retryMaxNumber;
    }

}
