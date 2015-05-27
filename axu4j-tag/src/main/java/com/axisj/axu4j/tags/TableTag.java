package com.axisj.axu4j.tags;

import com.axisj.axu4j.config.ConfigReader;
import org.apache.commons.lang.StringUtils;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.tagext.JspFragment;
import java.io.IOException;

/**
 * Created by HJ.Park on 2015-05-12.
 */
public class TableTag extends AXUTagSupport {


    public TableTag() throws Exception {
        super();
    }


    // =======================================================

    private String id;
    private String css;
    private String style;

    // =======================================================


    public String getId() {
        return StringUtils.defaultIfEmpty(id, String.format("table-%d", tagIndex));
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCss() {
        return css;
    }

    public void setCss(String css) {
        this.css = css;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }


    @Override
    public void beforeDoTag(JspContext context, JspFragment fragment) throws IOException {
        this.tagBody = ConfigReader.getConfig().getTableWrap();
        this.doBody = TagUtils.toString(fragment);
    }

    @Override
    public void afterDoTag(JspContext context, JspFragment fragment) { }
}
