package com.wujiuye.hotkit.rpc.playback;

/**
 * 反射回放
 *
 * @author wuijuye 2020/07/27
 */
public class ReflectPlayback implements Playback {

    /**
     * 回放操作
     *
     * @param log 操作日记
     * @return
     */
    @Override
    public Object playback(ActionRecord log) {
        // 获取对象工厂
        ObjectFactory objectFactory = PlaybackManager.getObjectFactory();
        if (objectFactory == null) {
            objectFactory = (beanName, className) -> {
                try {
                    Class<?> objClass = Class.forName(className);
                    return objClass.newInstance();
                } catch (Exception e) {
                    logger.error("objectFactory getObject error:" + e.getMessage(), e);
                    return null;
                }
            };
        }
        // 从对象工厂获取对象
        Object obj = objectFactory.getObject(log.getBeanName(), log.getClassName());
        if (obj == null) {
            logger.error("not foud obj:{}", log);
            return false;
        }
        // 反射调用方法
        return ActionInvokeUtils.invoke(obj, log);
    }

}
