package com.wujiuye.hotkit.util.html;

import com.wujiuye.hotkit.util.html.lable.DivLableWriter;
import com.wujiuye.hotkit.util.html.lable.LableEnum;

/**
 * @author wujiuye 单独创建标签的工具类
 */
public class LableWriterUtils {

    public static class MyDivLableWriter extends DivLableWriter {

        public MyDivLableWriter() {
            super(LableEnum.Div);
        }

    }

    public static DivLableWriter createDiv() {
        return new MyDivLableWriter();
    }

}
