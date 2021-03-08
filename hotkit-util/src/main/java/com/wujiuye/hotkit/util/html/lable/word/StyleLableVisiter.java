package com.wujiuye.hotkit.util.html.lable.word;

import com.wujiuye.hotkit.util.html.lable.LableBuild;

/**
 * 样式标签
 *
 * @author wujiuye 2020/05/27
 * @return
 */
public class StyleLableVisiter implements LableBuild {

    private String type;
    private String css;

    public StyleLableVisiter() {
        this.type = "text/css";
    }

    public StyleLableVisiter visitCss(String css) {
        this.css = css;
        return this;
    }

    @Override
    public String buildLableScript() {
        StringBuilder sb = new StringBuilder();
        sb.append("<style ");
        sb.append("type=\"").append(type).append("\" >");
        if (css != null) {
            sb.append(css);
        }
        sb.append("</style>");
        return sb.toString();
    }

}
