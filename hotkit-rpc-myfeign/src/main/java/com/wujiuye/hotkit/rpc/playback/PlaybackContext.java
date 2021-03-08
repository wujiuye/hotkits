package com.wujiuye.hotkit.rpc.playback;

import java.util.Optional;

/**
 * 回放上下文
 *
 * @author wujiuye 2020/08/03
 */
public class PlaybackContext {

    private final static ThreadLocal<ActionRecord> CONTEXT = new ThreadLocal<>();

    public static Optional<ActionRecord> curLog() {
        return Optional.ofNullable(CONTEXT.get());
    }

    public static void setCurLog(ActionRecord log) {
        CONTEXT.set(log);
    }

    public static void clear() {
        CONTEXT.remove();
    }

}
