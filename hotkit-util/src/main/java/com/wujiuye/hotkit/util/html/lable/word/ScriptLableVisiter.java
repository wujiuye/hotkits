package com.wujiuye.hotkit.util.html.lable.word;

import com.wujiuye.hotkit.util.StringUtils;
import com.wujiuye.hotkit.util.html.lable.LableBuild;

/**
 * js脚本标签
 *
 * @author wujiuye 2020/05/27
 * @return
 */
public class ScriptLableVisiter implements LableBuild {

    private String src;
    private String script;

    public ScriptLableVisiter() {

    }

    public ScriptLableVisiter visitSrc(String src) {
        this.src = src;
        return this;
    }

    public ScriptLableVisiter visitScript(String script) {
        this.script = script;
        return this;
    }

    @Override
    public String buildLableScript() {
        StringBuilder sb = new StringBuilder();
        sb.append("<script");
        if (!StringUtils.isNullOrEmpty(src)) {
            sb.append(" src=\"").append(src).append("\">");
        } else {
            sb.append(">");
        }
        if (!StringUtils.isNullOrEmpty(script)) {
            sb.append(script);
        }
        sb.append("</script>");
        return sb.toString();
    }

}
