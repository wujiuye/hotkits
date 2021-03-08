package com.wujiuye.hotkit.rpc.playback;

import com.wujiuye.hotkit.util.ReflectUtils;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 封装模版方法
 *
 * @author wujiuye 2020/07/20
 */
abstract class AbstractActionLogger implements ActionLogger {

    protected String business;

    public AbstractActionLogger(String business) {
        this.business = business;
    }

    public String getBusiness() {
        return business;
    }

    @Override
    public void apendAction(String beanName, Class<?> tClass, Method method, int maxPlaybackCnt, Map<Integer, Object> params) {
        ActionRecord actionRecord = new ActionRecord();
        actionRecord.setId(System.nanoTime());
        actionRecord.setPlaybackCount(0);
        actionRecord.setMaxPlaybackCount(maxPlaybackCnt);
        actionRecord.setBeanName(beanName);
        actionRecord.setClassName(tClass.getName());
        actionRecord.setMethodName(method.getName());
        actionRecord.setMethodDescriptor(ReflectUtils.getMethodDescriptor(method));
        actionRecord.setParams(params);
        this.doSava(actionRecord);
    }

    /**
     * 输出日记（持久化ACTION）
     *
     * @param record 失败操作记录
     */
    protected abstract void doSava(ActionRecord record);

}
