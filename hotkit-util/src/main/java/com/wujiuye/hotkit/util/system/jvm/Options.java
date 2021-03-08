package com.wujiuye.hotkit.util.system.jvm;

/**
 * @author wujiuye 2020/06/18
 */
public interface Options {

    int CPU = 1;
    int MEMORY = 1 << 1;
    int DISK = 1 << 2;

    int JVM_HEAP = 1 << 3;
    int JVM_STACK = 1 << 4;
    int JVM_GCUTIL = 1 << 5;

}
