package com.wujiuye.hotkit.util.html.lable;

import com.wujiuye.hotkit.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用标签生成器
 *
 * @author wujiuye 2020/05/27
 */
public class GeneralLableWriter extends LableWriter {

    private String textContent;
    private List<LableVisiter> childs;

    protected GeneralLableWriter(LableEnum lableEnum) {
        super(lableEnum);
        this.childs = new ArrayList<>();
    }

    @Override
    public void visitBody(String content) {
        this.textContent = content;
    }

    @Override
    public LableVisiter visitChild(LableEnum lableEnum) {
        if (!this.lableEnum.isAllowChildLable()) {
            throw new IllegalArgumentException("当前标签不支持有子标签. " + lableEnum.getLableName());
        }
        LableVisiter child = LableFactory.createLable(lableEnum);
        this.childs.add(child);
        return child;
    }

    @Override
    protected void buildLableBody(StringBuilder sb) {
        if (!this.lableEnum.isAutoClose()) {
            if (textContent != null) {
                sb.append(textContent);
            }
            if (!CollectionUtils.isEmpty(childs)) {
                for (LableVisiter child : childs) {
                    sb.append(child.buildLableScript());
                }
            }
        }
    }

}
