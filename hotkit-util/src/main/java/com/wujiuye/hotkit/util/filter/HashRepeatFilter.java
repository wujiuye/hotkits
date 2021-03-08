package com.wujiuye.hotkit.util.filter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 使用hash过滤
 * 空间换时间
 *
 * @author wujiuye 2020/05/25
 */
public class HashRepeatFilter<T> implements RepeatFilter<T> {

    private Node<T>[] table;
    private int tableSize;

    public HashRepeatFilter() {
        tableSize = 16;
        table = new Node[tableSize];
    }

    public HashRepeatFilter(int tableSize) {
        this.tableSize = tableSize ^ 0x01;
        table = new Node[this.tableSize];
    }

    @Override
    public List<T> doFilter(List<T> collection, UniqueKeyGenerator<T> uniqueKeyGenerator, OnRepeatOps<T> onRepeatOps) {
        return collection.stream().filter(item -> {
            String key = uniqueKeyGenerator.getUniqueKey(item);
            int keyHash = key.hashCode();
            int index = Math.abs(keyHash % tableSize);
            Node<T> old = table[index];
            if (old == null) {
                table[index] = new Node<>(item);
                return true;
            }
            Node<T> last = old;
            for (; old != null; old = old.getNext()) {
                String oldKey = uniqueKeyGenerator.getUniqueKey(old.getValue());
                if (oldKey.equals(key)) {
                    if (onRepeatOps != null) {
                        onRepeatOps.onRepeat(old.getValue(), item);
                    }
                    return false;
                }
                last = old;
            }
            last.setNext(new Node<>(item));
            return true;
        }).collect(Collectors.toList());
    }

    private static class Node<T> {
        private T value;
        private Node<T> next;

        public Node(T value) {
            this.value = value;
            this.next = null;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public Node<T> getNext() {
            return next;
        }

        public void setNext(Node<T> next) {
            this.next = next;
        }
    }

}
