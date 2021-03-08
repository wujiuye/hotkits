package com.wujiuye.hotkit.rpc.playback;

import com.wujiuye.hotkit.json.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 同步调用失败处理，记录日记
 *
 * @author wujiuye 2020/08/03
 */
public class MethodInvokeFailHandler {

    /**
     * 方法调用失败处理
     *
     * @param method
     * @param args
     * @param result
     * @param e
     */
    public static void handle(Method method, Object[] args, Object result, Exception e) {
        // 当前是回放发起的，则不记录
        if (PlaybackContext.curLog().isPresent()) {
            return;
        }
        SupporPlayback supporPlayback = method.getAnnotation(SupporPlayback.class);
        if (supporPlayback == null) {
            return;
        }
        String biz = supporPlayback.value();
        ActionLogger logger = ActionLoggerFactory.getLogger(biz);
        // 异常类型过滤
        if (e != null && supporPlayback.playbackOnThrowables().length > 0) {
            for (Class<? extends Throwable> throwableClass : supporPlayback.playbackOnThrowables()) {
                if (throwableClass.isAssignableFrom(e.getClass())) {
                    logger.apendAction(method.getDeclaringClass(), method, supporPlayback.maxPlaybackCount(), args);
                    return;
                }
            }
        }
        // 表达式过滤
        String expressionStr = supporPlayback.expression();
        if (!StringUtils.isNullOrEmpty(expressionStr)) {
            boolean isEquals;
            String[] expression;
            if (expressionStr.contains("==")) {
                isEquals = true;
                expression = expressionStr.split("==");
            } else if (expressionStr.contains("!=")) {
                isEquals = false;
                expression = expressionStr.split("!=");
            } else {
                return;
            }
            if (expression.length != 2) {
                return;
            }
            String[] link = expression[0].split("\\.");
            Object v;
            try {
                v = getObj(link, result, args);
            } catch (Exception ex) {
                return;
            }
            if (isEquals) {
                if ((v == null && "null".equalsIgnoreCase(expression[1].trim()))
                        || (v != null && v.toString().equalsIgnoreCase(expression[1].trim()))) {
                    logger.apendAction(method.getDeclaringClass(), method, supporPlayback.maxPlaybackCount(), args);
                }
            } else {
                if ((v == null && !"null".equalsIgnoreCase(expression[1].trim()))
                        || (v != null && !v.toString().equalsIgnoreCase(expression[1].trim()))) {
                    logger.apendAction(method.getDeclaringClass(), method, supporPlayback.maxPlaybackCount(), args);
                }
            }
        }
    }

    private static Object getObj(String[] link, Object result, Object[] args) throws Exception {
        if ("$return".equalsIgnoreCase(link[0].trim())) {
            if (link.length == 1) {
                return result;
            }
            return getObj(link, 1, result);
        } else if (link[0].trim().startsWith("$param")) {
            int index = Integer.parseInt(link[0].replace("$param", ""));
            if (index <= 0 || index > args.length) {
                return null;
            }
            Object paramValue = args[index - 1];
            if (link.length == 1) {
                return paramValue;
            }
            return getObj(link, 1, paramValue);
        }
        return null;
    }

    private static Object getObj(String[] link, int start, Object obj) throws Exception {
        Object value = obj;
        for (int i = start; i < link.length; i++) {
            Field field = obj.getClass().getDeclaredField(link[i].trim());
            field.setAccessible(true);
            value = field.get(value);
        }
        return value;
    }

}
