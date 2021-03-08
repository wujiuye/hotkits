package com.wujiuye.hotkit.util.concurrent;

/**
 * @author wujiuye
 * @version 1.0 on 2019/11/3 {描述：
 * 获取异步请求结果的包装类
 * }
 */
public class AsyncWraper<T> {

    public interface RefProxy<T> {
        T get();
    }

    private RefProxy<T> ref;
    private volatile T result;

    public AsyncWraper(RefProxy<T> ref) {
        this.ref = ref;
    }

    public T getRef() {
        if (result != null) {
            return result;
        }
        // 调用引用对象的get方法获取结果，
        // 对于异步调用，如果请求未得到响应，此时才会阻塞等待结果
        result = ref.get();
        return result;
    }

}
