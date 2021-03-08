package com.wujiuye.hotkit.util.filter;

import com.wujiuye.hotkit.util.CollectionUtils;

import java.util.List;

/**
 * 重复数据过滤器
 *
 * @author wujiuye 2020/05/25
 */
@FunctionalInterface
public interface RepeatFilter<T> {

    /**
     * 唯一key生成器
     *
     * @param <T>
     */
    @FunctionalInterface
    interface UniqueKeyGenerator<T> {
        /**
         * 获取唯一key，该key用来判断对象是否重复的
         *
         * @param obj 对象
         * @return
         */
        String getUniqueKey(T obj);
    }

    /**
     * 当发现重复项时被调用
     *
     * @param <T>
     */
    @FunctionalInterface
    interface OnRepeatOps<T> {
        /**
         * 处理重复，如将current的某些字段的值赋给exist
         *
         * @param exist   已经存在的项
         * @param current 当前项
         */
        void onRepeat(T exist, T current);
    }

    /**
     * 过滤重复数据
     *
     * @param collection         含有重复数据的集合
     * @param uniqueKeyGenerator 唯一key生成器，根据生成的key过滤key重复的记录
     * @return
     */
    default List<T> filter(List<T> collection, UniqueKeyGenerator<T> uniqueKeyGenerator) {
        return filter(collection, uniqueKeyGenerator, null);
    }

    /**
     * 过滤重复数据
     *
     * @param collection         含有重复数据的集合
     * @param uniqueKeyGenerator 唯一key生成器，根据生成的key过滤key重复的记录
     * @param onRepeatOps        当遇到重复数据时调用，可用于将新的记录（当前出现）的字段的值替换替换旧记录（已经存在）的
     * @return
     */
    default List<T> filter(List<T> collection, UniqueKeyGenerator<T> uniqueKeyGenerator, OnRepeatOps<T> onRepeatOps) {
        if (CollectionUtils.isEmpty(collection) || collection.size() == 1) {
            return collection;
        }
        if (uniqueKeyGenerator == null) {
            return collection;
        }
        return doFilter(collection, uniqueKeyGenerator, onRepeatOps);
    }

    List<T> doFilter(List<T> collection, UniqueKeyGenerator<T> uniqueKeyGenerator, OnRepeatOps<T> onRepeatOps);

}
