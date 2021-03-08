package com.wujiuye.hotkit.util.system.jvm;

import java.text.DecimalFormat;

/**
 * @author wujiuye 2021/03/08
 */
public class ByteFormatUtils {

    /**
     * 将字节数转kb、mb、gb、tb等可识别的单位
     *
     * @param byteNumber
     * @return
     */
    public static String formatByte(long byteNumber) {
        double FORMAT = 1024.0;
        double kbNumber = byteNumber / FORMAT;
        if (kbNumber < FORMAT) {
            return new DecimalFormat("#.##KB").format(kbNumber);
        }
        double mbNumber = kbNumber / FORMAT;
        if (mbNumber < FORMAT) {
            return new DecimalFormat("#.##MB").format(mbNumber);
        }
        double gbNumber = mbNumber / FORMAT;
        if (gbNumber < FORMAT) {
            return new DecimalFormat("#.##GB").format(gbNumber);
        }
        double tbNumber = gbNumber / FORMAT;
        return new DecimalFormat("#.##TB").format(tbNumber);
    }

}
