package com.wujiuye.hotkit.util.html.lable;

/**
 * 通用标签访问器
 *
 * @author wujiuye 2020/05/27
 */
public interface LableVisiter extends LableBuild {

    /**
     * 为标签添加样式
     *
     * @param css css样式
     * @return
     */
    void visitCss(String css);

    /**
     * 为标签添加属性
     *
     * @param attributeName  属性名
     * @param attributeValue 属性值
     * @return
     */
    void visitAttribute(String attributeName, String attributeValue);

    /**
     * 为标签添加文本内容
     *
     * @param content 文本内容
     * @return
     */
    default void visitBody(String content) {
        throw new UnsupportedOperationException();
    }

    /**
     * 为标签添加子标签
     *
     * @param lableEnum 标签类型
     * @return 子标签
     */
    default LableVisiter visitChild(LableEnum lableEnum) {
        throw new UnsupportedOperationException();
    }

}
