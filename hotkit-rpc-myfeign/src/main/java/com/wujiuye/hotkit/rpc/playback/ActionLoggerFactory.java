package com.wujiuye.hotkit.rpc.playback;

import java.util.HashMap;
import java.util.Map;

/**
 * action日记工厂
 *
 * @author wujiuye 2020/07/20
 */
public class ActionLoggerFactory {

    private static Map<String, ActionLogger> loggerMap = new HashMap<>();

    /**
     * 为业务分配一个ActionLogger
     *
     * @param business
     * @return
     */
    public static ActionLogger getLogger(String business) {
        ActionLogger logger = loggerMap.get(business);
        if (logger != null) {
            return logger;
        }
        synchronized (ActionLoggerFactory.class) {
            if (loggerMap.get(business) == null) {
                logger = new FileActionLogger(business);
                // 因为对象只创建一次，所以使用重新构造map的方式，去掉使用并发安全的map，性能会提升很多
                Map<String, ActionLogger> newMap = new HashMap<>(loggerMap.size() + 1);
                newMap.putAll(loggerMap);
                newMap.put(business, logger);
                loggerMap = newMap;
            }
        }
        return loggerMap.get(business);
    }

}
