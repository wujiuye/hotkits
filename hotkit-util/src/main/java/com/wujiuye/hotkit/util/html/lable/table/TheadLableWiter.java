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
public class TheadLableWiter extends LableWriter {

    private List<TrLableWriter> trLableWriterList;

    protected TheadLableWiter(LableEnum lableEnum) {
        super(lableEnum);
        this.trLableWriterList = new ArrayList<>();
    }

    public TrLableWriter visitTr() {
        TrLableWriter trLableWriter = new TrLableWriter(LableEnum.Tr);
        trLableWriterList.add(trLableWriter);
        return trLableWriter;
    }

    @Override
    protected void buildLableBody(StringBuilder sb) {
        if (!CollectionUtils.isEmpty(trLableWriterList)) {
            trLableWriterList.forEach(trLableWriter -> sb.append(trLableWriter.buildLableScript()));
        }
    }

}
