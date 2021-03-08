package com.wujiuye.hotkit.rpc.playback;

/**
 * action回放回调
 *
 * @author wujiuye 2020/07/20
 */
public interface ActionConsumListener {

    /**
     * 是否消费这类消息
     *
     * @param record
     * @return
     */
    boolean suppor(ActionRecord record);

    /**
     * 重放失败
     *
     * @param action
     * @param e
     * @param result 回放调用方法返回的结果
     */
    void onFail(ActionRecord action, Exception e, Object result);

    /**
     * 重放成功
     *
     * @param action
     * @param result 回放调用方法返回的结果
     */
    void onSuccess(ActionRecord action, Object result);

    /**
     * 重放全部完成
     */
    void onFinish();

}
