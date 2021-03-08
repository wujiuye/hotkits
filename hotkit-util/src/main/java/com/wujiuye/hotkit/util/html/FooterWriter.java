package com.wujiuye.hotkit.util.html;

import com.wujiuye.hotkit.util.CollectionUtils;
import com.wujiuye.hotkit.util.html.lable.LableEnum;
import com.wujiuye.hotkit.util.html.lable.LableFactory;
import com.wujiuye.hotkit.util.html.lable.LableVisiter;

import java.util.ArrayList;
import java.util.List;

/**
 * html底部标签访问者
 *
 * @author wujiuye 2020/05/27
 */
public class FooterWriter {

    private List<LableVisiter> lableVisiters;

    protected FooterWriter() {
        this.lableVisiters = new ArrayList<>();
    }

    public LableVisiter visitLable(LableEnum lableEnum) {
        LableVisiter lableVisiters = LableFactory.createLable(lableEnum);
        this.lableVisiters.add(lableVisiters);
        return lableVisiters;
    }

    public String buildFooterScript() {
        StringBuilder sb = new StringBuilder("<footer>");
        if (!CollectionUtils.isEmpty(lableVisiters)) {
            for (LableVisiter lableVisiter : lableVisiters) {
                sb.append(lableVisiter.buildLableScript());
            }
        }
        return sb.append("</footer>").toString();
    }

}
