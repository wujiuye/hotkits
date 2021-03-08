package com.wujiuye.hotkit.util.html.lable;

/**
 * 标签枚举
 * 参数1：标签名称
 * 参数2：是否自动闭合标签
 * 参数3：是否允许有子标签
 *
 * @author wujiuye 2020/05/27
 */

public enum LableEnum {

    /**
     * 标题相关
     */
    H1("h1", false, true),
    H2("h2", false, true),
    H3("h2", false, true),
    H4("h2", false, true),
    H5("h2", false, true),
    H6("h2", false, true),

    /**
     * 文本标签
     */
    P("p", false, true),
    Span("span",false,false),

    /**
     * div标签
     */
    Div("div", false, true),

    /**
     * 表格相关
     */
    Table("table", false, false),
    Thead("thead", false, false),
    Tfoot("tfoot", false, false),
    Tbody("tbody", false, false),
    Tr("tr", false, false),
    Th("th", false, true),
    Td("td", false, true),

    /**
     * 闭合标签
     */
    Br("br", true, false),
    Img("br", true, false);

    String lableName;
    boolean autoClose;
    boolean allowChildLable;

    LableEnum(String lableName, boolean autoClose, boolean allowChildLable) {
        this.lableName = lableName;
        this.autoClose = autoClose;
        this.allowChildLable = allowChildLable;
    }

    public String getLableName() {
        return lableName;
    }

    public boolean isAutoClose() {
        return autoClose;
    }

    public boolean isAllowChildLable() {
        return allowChildLable;
    }

}
