package com.wujiuye.hotkit.rpc.myfeign.okhttp;

import java.io.IOException;

/**
 * 支持重试
 *
 * @author wujiuye 2020/05/27
 */
public class HttpSupperRetryUtils {

    /**
     * 使用自定义重试规则支持重试
     *
     * @param action         http请求动作
     * @param retryMaxNumber 重试次数
     * @param retryRule      重试规则
     * @return
     */
    public static HttpResponse retryByRetryRule(HttpSupperRetryAction action,
                                                int retryMaxNumber,
                                                HttpSupperRetryAction.RetryRule... retryRule) {
        if (retryMaxNumber < 0) {
            retryMaxNumber = 0;
        }
        retryMaxNumber++;
        HttpResponse response = null;
        Exception exception = null;
        for (int i = 1; i <= retryMaxNumber; i++) {
            try {
                response = action.rpcInvoke();
                if (response.getCode() == 200) {
                    return response;
                }
            } catch (Exception e) {
                exception = e;
            }
            boolean allow = false;
            int responseHttpStatusCode = response == null ? Integer.MIN_VALUE : response.getCode();
            for (HttpSupperRetryAction.RetryRule rule : retryRule) {
                if (rule.allowRetry(responseHttpStatusCode, exception)) {
                    allow = true;
                    break;
                }
            }
            if (!allow) {
                break;
            }
        }
        if (response != null || exception == null) {
            return response;
        }
        throw new RuntimeException(exception.getLocalizedMessage(), exception.getCause());
    }

    /**
     * 响应状态码非200则重试
     *
     * @param action    http请求动作
     * @param tryNumber 重试次数
     * @return
     */
    public static HttpResponse retryByNotOK(HttpSupperRetryAction action, int tryNumber) {
        return retryByHttpStatusCode(action, 200, tryNumber);
    }

    /**
     * 根据错误码重试(IOException也会重试)
     *
     * @param action               http请求动作
     * @param expectHttpStatusCode 期望的http响应状态码(响应状态码不等于该状态码则重试)
     * @param retryMaxNumber       重试次数
     * @return
     */
    public static HttpResponse retryByHttpStatusCode(HttpSupperRetryAction action, int expectHttpStatusCode, int retryMaxNumber) {
        if (retryMaxNumber < 0) {
            retryMaxNumber = 0;
        }
        retryMaxNumber++;
        HttpResponse response = null;
        RuntimeException exception = null;
        for (int i = 1; i < retryMaxNumber; i++) {
            try {
                response = action.rpcInvoke();
                if (response.getCode() == expectHttpStatusCode) {
                    return response;
                }
            } catch (IOException e) {
                exception = new RuntimeException(e.getLocalizedMessage(), e);
            }
        }
        if (response != null || exception == null) {
            return response;
        }
        throw exception;
    }

    /**
     * 根据异常类型重试
     *
     * @param action         http请求动作
     * @param exceptionClass 异常类型
     * @param retryMaxNumber 重试次数
     * @param <T>
     * @return
     */
    public static <T extends Exception> HttpResponse retryByException(HttpSupperRetryAction action,
                                                                      Class<T> exceptionClass, int retryMaxNumber) {
        if (retryMaxNumber < 0) {
            retryMaxNumber = 0;
        }
        retryMaxNumber++;
        RuntimeException exception = null;
        for (int i = 1; i < retryMaxNumber; i++) {
            try {
                return action.rpcInvoke();
            } catch (Exception e) {
                if (!exceptionClass.isAssignableFrom(e.getClass())
                        || i == retryMaxNumber - 1) {
                    exception = new RuntimeException(e.getLocalizedMessage(), e.getCause());
                    break;
                }
            }
        }
        if (exception == null) {
            return null;
        }
        throw exception;
    }

}
