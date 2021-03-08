package com.wujiuye.hotkit.util.html;

import com.wujiuye.hotkit.util.CollectionUtils;
import com.wujiuye.hotkit.util.html.lable.word.LinkLableVisiter;
import com.wujiuye.hotkit.util.html.lable.word.ScriptLableVisiter;
import com.wujiuye.hotkit.util.html.lable.word.StyleLableVisiter;

import java.util.ArrayList;
import java.util.List;

/**
 * html头部标签访问者
 *
 * @author wujiuye 2020/05/27
 */
public class HeaderWriter {

    private List<StyleLableVisiter> styleLableVisiters;
    private List<ScriptLableVisiter> scriptLableVisiters;
    private List<LinkLableVisiter> linkLableVisiters;

    protected HeaderWriter() {
        this.styleLableVisiters = new ArrayList<>();
        this.linkLableVisiters = new ArrayList<>();
        this.scriptLableVisiters = new ArrayList<>();
    }

    public ScriptLableVisiter visitScriptLableBySrc(String src) {
        ScriptLableVisiter scriptLableVisiter = new ScriptLableVisiter();
        scriptLableVisiter.visitSrc(src);
        this.scriptLableVisiters.add(scriptLableVisiter);
        return scriptLableVisiter;
    }

    public ScriptLableVisiter visitScriptLableByScript(String script) {
        ScriptLableVisiter scriptLableVisiter = new ScriptLableVisiter();
        scriptLableVisiter.visitScript(script);
        this.scriptLableVisiters.add(scriptLableVisiter);
        return scriptLableVisiter;
    }

    public LinkLableVisiter visitLinkLable(String rel, String type, String href) {
        LinkLableVisiter linkLableVisiter = new LinkLableVisiter(rel, type, href);
        linkLableVisiters.add(linkLableVisiter);
        return linkLableVisiter;
    }

    public StyleLableVisiter visitStyleLable() {
        StyleLableVisiter styleLableVisiter = new StyleLableVisiter();
        styleLableVisiters.add(styleLableVisiter);
        return styleLableVisiter;
    }

    public String buildHeaderScript() {
        StringBuilder sb = new StringBuilder("<head>");
        if (!CollectionUtils.isEmpty(linkLableVisiters)) {
            for (LinkLableVisiter linkLableVisiter : linkLableVisiters) {
                sb.append(linkLableVisiter.buildLableScript());
            }
        }
        if (!CollectionUtils.isEmpty(scriptLableVisiters)) {
            for (ScriptLableVisiter scriptLableVisiter : scriptLableVisiters) {
                sb.append(scriptLableVisiter.buildLableScript());
            }
        }
        if (!CollectionUtils.isEmpty(styleLableVisiters)) {
            for (StyleLableVisiter styleLableVisiter : styleLableVisiters) {
                sb.append(styleLableVisiter.buildLableScript());
            }
        }
        return sb.append("</head>").toString();
    }

}
