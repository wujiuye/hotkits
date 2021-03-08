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
public class TableLableWriter extends LableWriter {

    private TheadLableWiter theadLableWiter;
    private TbodyLableWiter tbodyLableWiter;
    private TfootLableWiter tfootLableWiter;
    private List<TrLableWriter> trs;

    public TableLableWriter(LableEnum lableEnum) {
        super(lableEnum);
        this.trs = new ArrayList<>();
    }

    public TheadLableWiter visitThead() {
        this.theadLableWiter = new TheadLableWiter(LableEnum.Thead);
        return this.theadLableWiter;
    }

    public TbodyLableWiter visitTbody() {
        this.tbodyLableWiter = new TbodyLableWiter(LableEnum.Tbody);
        return this.tbodyLableWiter;
    }

    public TfootLableWiter visitTfoot() {
        this.tfootLableWiter = new TfootLableWiter(LableEnum.Tfoot);
        return this.tfootLableWiter;
    }

    public TrLableWriter visitTr() {
        TrLableWriter trLableWriter = new TrLableWriter(LableEnum.Tr);
        trs.add(trLableWriter);
        return trLableWriter;
    }

    @Override
    protected void buildLableBody(StringBuilder sb) {
        if (theadLableWiter != null) {
            sb.append(theadLableWiter.buildLableScript());
        }
        if (tbodyLableWiter != null) {
            sb.append(tbodyLableWiter.buildLableScript());
        }
        if (!CollectionUtils.isEmpty(trs)) {
            trs.forEach(trLableWriter -> sb.append(trLableWriter.buildLableScript()));
        }
        if (tfootLableWiter != null) {
            sb.append(tfootLableWiter.buildLableScript());
        }
    }

}
