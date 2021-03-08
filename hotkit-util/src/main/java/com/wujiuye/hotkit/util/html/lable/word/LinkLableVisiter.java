package com.wujiuye.hotkit.util.html.lable.word;

import com.wujiuye.hotkit.util.html.lable.LableBuild;

/**
 * <link rel="icon" type="image/png" href="https://fanyi-cdn.cdn.bcebos.com/static/translation/img/favicon/favicon-32x32_ca689c3.png" sizes="32x32">
 *
 * @author wujiuye 2020/05/27
 * @return
 */
public class LinkLableVisiter implements LableBuild {

    private String rel;
    private String type;
    private String href;
    private String sizes;

    public LinkLableVisiter(String rel, String type, String href) {
        this.rel = rel;
        this.type = type;
        this.href = href;
    }

    public LinkLableVisiter visitSizes(int w, int h) {
        this.sizes = w + "x" + h;
        return this;
    }

    @Override
    public String buildLableScript() {
        StringBuilder sb = new StringBuilder();
        sb.append("<link ");
        sb.append("rel=\"").append(rel).append("\" ");
        sb.append("type=\"").append(type).append("\" ");
        sb.append("href=\"").append(href).append("\" ");
        if (sizes != null) {
            sb.append("sizes=\"").append(sizes).append("\" ");
        }
        sb.append(">");
        return sb.toString();
    }

}
