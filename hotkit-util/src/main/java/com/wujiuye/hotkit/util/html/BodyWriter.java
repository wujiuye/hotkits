package com.wujiuye.hotkit.util.html;

import com.wujiuye.hotkit.util.CollectionUtils;
import com.wujiuye.hotkit.util.html.lable.DivLableWriter;
import com.wujiuye.hotkit.util.html.lable.LableEnum;
import com.wujiuye.hotkit.util.html.lable.LableFactory;
import com.wujiuye.hotkit.util.html.lable.LableVisiter;
import com.wujiuye.hotkit.util.html.lable.table.TableLableWriter;
import com.wujiuye.hotkit.util.html.lable.word.ScriptLableVisiter;

import java.util.ArrayList;
import java.util.List;

/**
 * html body标签访问者
 *
 * @author wujiuye 2020/05/27
 */
public class BodyWriter {

    private List<LableVisiter> lableVisiters;
    private List<ScriptLableVisiter> scriptLableVisiters;

    protected BodyWriter() {
        this.lableVisiters = new ArrayList<>();
        this.scriptLableVisiters = new ArrayList<>();
    }

    public LableVisiter visitLable(LableEnum lableEnum) {
        LableVisiter lableVisiter = LableFactory.createLable(lableEnum);
        this.lableVisiters.add(lableVisiter);
        return lableVisiter;
    }

    public TableLableWriter visitTableLable() {
        return (TableLableWriter) visitLable(LableEnum.Table);
    }

    public DivLableWriter visitDivLable() {
        return (DivLableWriter) visitLable(LableEnum.Div);
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

    public String buildBodyScript() {
        StringBuilder sb = new StringBuilder("<body>");
        if (!CollectionUtils.isEmpty(lableVisiters)) {
            for (LableVisiter lableVisiterTmp : lableVisiters) {
                sb.append(lableVisiterTmp.buildLableScript());
            }
        }
        if (!CollectionUtils.isEmpty(scriptLableVisiters)) {
            for (ScriptLableVisiter scriptLableVisiter : scriptLableVisiters) {
                sb.append(scriptLableVisiter.toString());
            }
        }
        return sb.append("</body>").toString();
    }

}
