package com.wujiuye.hotkit.util.html.lable;

import com.wujiuye.hotkit.util.html.lable.table.TableLableWriter;

/**
 * Div标签
 *
 * @author wujiuye 2020/05/27
 * @return
 */
public class DivLableWriter extends GeneralLableWriter {

    protected DivLableWriter(LableEnum lableEnum) {
        super(lableEnum);
    }

    public TableLableWriter visitTableLable() {
        return (TableLableWriter) visitChild(LableEnum.Table);
    }

    public DivLableWriter visitDivLable() {
        return (DivLableWriter) visitChild(LableEnum.Div);
    }

}
