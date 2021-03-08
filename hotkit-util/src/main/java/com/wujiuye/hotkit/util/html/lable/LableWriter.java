package com.wujiuye.hotkit.util.html.lable;

import com.wujiuye.hotkit.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 标签访问器基类
 *
 * @author wujiuye 2020/05/27
 */
public abstract class LableWriter implements LableVisiter {

    private String css;
    private Map<String, String> attributes;

    protected LableEnum lableEnum;

    protected LableWriter(LableEnum lableEnum) {
        this.lableEnum = lableEnum;
        this.attributes = new HashMap<>();
    }

    @Override
    public void visitCss(String css) {
        this.css = css;
    }

    @Override
    public void visitAttribute(String attributeName, String attributeValue) {
        this.attributes.put(attributeName, attributeValue);
    }

    @Override
    public String buildLableScript() {
        StringBuilder sb = new StringBuilder();
        sb.append("<").append(this.lableEnum.getLableName());
        if (this.css != null) {
            sb.append(" style=\"").append(css).append("\"");
        }
        if (!CollectionUtils.isEmpty(this.attributes)) {
            this.attributes.entrySet().forEach(entry -> sb.append(" ")
                    .append(entry.getKey()).append("=\"").append(entry.getValue()).append("\""));
        }
        if (this.lableEnum.isAutoClose()) {
            sb.append("/>");
        } else {
            sb.append(">");
        }
        buildLableBody(sb);
        if (!this.lableEnum.isAutoClose()) {
            sb.append("</").append(this.lableEnum.getLableName()).append(">");
        }
        return sb.toString();
    }

    /**
     * 构造标签的body
     *
     * @param sb 将标签的body追加到StringBuilder
     */
    protected abstract void buildLableBody(StringBuilder sb);

}
