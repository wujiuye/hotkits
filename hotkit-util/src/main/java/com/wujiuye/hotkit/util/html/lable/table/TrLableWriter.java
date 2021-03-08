package com.wujiuye.hotkit.util.html.lable.table;

import com.wujiuye.hotkit.util.CollectionUtils;
import com.wujiuye.hotkit.util.html.lable.LableEnum;
import com.wujiuye.hotkit.util.html.lable.LableWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于生成Table
 *
 * @author wujiuye 2020/05/27
 * @return
 */
public class TrLableWriter extends LableWriter {

    private List<TdLableWiter> tds;

    protected TrLableWriter(LableEnum lableEnum) {
        super(lableEnum);
        this.tds = new ArrayList<>();
    }

    public TdLableWiter visitTd() {
        TdLableWiter tdLableWiter = new TdLableWiter(LableEnum.Td);
        tds.add(tdLableWiter);
        return tdLableWiter;
    }

    public TdLableWiter visitTh() {
        TdLableWiter tdLableWiter = new TdLableWiter(LableEnum.Th);
        tds.add(tdLableWiter);
        return tdLableWiter;
    }

    @Override
    protected void buildLableBody(StringBuilder sb) {
        if (!CollectionUtils.isEmpty(tds)) {
            tds.forEach(tdLableWiter -> sb.append(tdLableWiter.buildLableScript()));
        }
    }

}
