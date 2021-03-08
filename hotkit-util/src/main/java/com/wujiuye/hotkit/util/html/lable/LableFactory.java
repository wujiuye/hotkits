package com.wujiuye.hotkit.util.html.lable;

import com.wujiuye.hotkit.util.html.lable.table.TableLableWriter;

/**
 * @author wujiuye 2020/05/27
 */
public class LableFactory {

    public static <T extends LableWriter> T createLable(LableEnum lableEnum) {
        if (lableEnum == LableEnum.Thead
                || lableEnum == LableEnum.Tbody
                || lableEnum == LableEnum.Tfoot
                || lableEnum == LableEnum.Tr
                || lableEnum == LableEnum.Th || lableEnum == LableEnum.Td) {
            throw new IllegalArgumentException("不支持创建的标签！");
        }
        if (lableEnum == LableEnum.Table) {
            return (T) new TableLableWriter(lableEnum);
        }
        if (lableEnum == LableEnum.Div) {
            return (T) new DivLableWriter(lableEnum);
        }
        return (T) new GeneralLableWriter(lableEnum);
    }

}
