package com.wujiuye.hotkit.rpc.playback;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;

/**
 * Action日记，用于失败重放Action
 *
 * @author wujiuye 2020/07/20
 */
public class ActionRecord {

    private long id;
    /**
     * 已重放次数（默认为0）
     */
    private int playbackCount;
    /**
     * 最大重放次数
     */
    private int maxPlaybackCount;
    /**
     * 首次失败时间
     */
    private String firstFailTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:Ss"));
    /**
     * bean的名称
     */
    private String beanName;
    /**
     * 类名
     */
    private String className;
    /**
     * 方法名称
     */
    private String methodName;
    /**
     * 方法描述符
     */
    private String methodDescriptor;
    /**
     * 当时调用方法传递的参数
     * key: 参数在方法参数列表的下标
     */
    private Map<Integer, Object> params;

    private transient Object result;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodDescriptor() {
        return methodDescriptor;
    }

    public void setMethodDescriptor(String methodDescriptor) {
        this.methodDescriptor = methodDescriptor;
    }

    public Map<Integer, Object> getParams() {
        return params;
    }

    public void setParams(Map<Integer, Object> params) {
        this.params = params;
    }

    public int getPlaybackCount() {
        return playbackCount;
    }

    public void setPlaybackCount(int playbackCount) {
        this.playbackCount = playbackCount;
    }

    public String getFirstFailTime() {
        return firstFailTime;
    }

    public void setFirstFailTime(String firstFailTime) {
        this.firstFailTime = firstFailTime;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public int getMaxPlaybackCount() {
        return maxPlaybackCount;
    }

    public void setMaxPlaybackCount(int maxPlaybackCount) {
        this.maxPlaybackCount = maxPlaybackCount;
    }

    @Override
    public String toString() {
        return "ActionRecord{" +
                "id=" + id +
                ", playbackCount=" + playbackCount +
                ", maxPlaybackCount=" + maxPlaybackCount +
                ", firstFailTime='" + firstFailTime + '\'' +
                ", beanName='" + beanName + '\'' +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", methodDescriptor='" + methodDescriptor + '\'' +
                ", params=" + params +
                ", result=" + result +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ActionRecord that = (ActionRecord) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
