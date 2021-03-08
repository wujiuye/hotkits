package com.wujiuye.hotkit.util.html;

/**
 * html脚本访问者
 *
 * @author wujiuye 2020/05/27
 */
public class HtmlWriter {

    private HeaderWriter headerWriter;
    private BodyWriter bodyWriter;
    private FooterWriter footerWriter;

    public HeaderWriter visitHeader() {
        this.headerWriter = new HeaderWriter();
        return this.headerWriter;
    }

    public BodyWriter visitBody() {
        this.bodyWriter = new BodyWriter();
        return this.bodyWriter;
    }

    public FooterWriter visitFooter() {
        this.footerWriter = new FooterWriter();
        return this.footerWriter;
    }

    public String buildHttpScript() {
        StringBuilder html = new StringBuilder();
        html.append("<html>");
        if (headerWriter != null) {
            html.append(headerWriter.buildHeaderScript());
        }
        if (bodyWriter != null) {
            html.append(bodyWriter.buildBodyScript());
        }
        if (footerWriter != null) {
            html.append(footerWriter.buildFooterScript());
        }
        html.append("</html>");
        return html.toString();
    }

}
