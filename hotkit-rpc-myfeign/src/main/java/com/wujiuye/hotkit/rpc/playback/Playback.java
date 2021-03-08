package com.wujiuye.hotkit.rpc.playback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 重试操作
 *
 * @author wujiuye 2020/07/20
 */
interface Playback {

    Logger logger = LoggerFactory.getLogger(Playback.class);

    /**
     * 读取到日记时回放操作
     *
     * @param log 操作日记
     * @return
     */
    Object playback(ActionRecord log);

}
