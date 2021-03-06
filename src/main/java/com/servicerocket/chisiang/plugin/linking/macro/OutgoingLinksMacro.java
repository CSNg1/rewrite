package com.servicerocket.chisiang.plugin.linking.macro;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.links.OutgoingLink;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.confluence.pages.Page;
import com.atlassian.confluence.pages.PageManager;
import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.atlassian.confluence.util.velocity.VelocityUtils.getRenderedTemplate;
import static com.servicerocket.chisiang.plugin.linking.PluginInfo.OutgoingLinksMacro.TEMPLATE;

/**
 * @author CSNg
 * @since  1.0.0.20160310
 */
public class OutgoingLinksMacro extends AbstractMacro {

    protected final PageManager pageManager;

    public OutgoingLinksMacro(PageManager pageManager) {
        this.pageManager = pageManager;
    }

    public String execute(Map<String, String> map, String s, ConversionContext conversionContext) throws MacroExecutionException {
        Map<String, Object> contextMap = getDefaultContext();

        Page targetPage = pageManager.getPage(conversionContext.getSpaceKey(), conversionContext.getPageContext().getPageTitle());

        List<OutgoingLink> outgoingLinks = targetPage.getOutgoingLinks();

        List<String> targetLinks = new ArrayList<>();
        for (OutgoingLink ogl : outgoingLinks) {
            targetLinks.add(ogl.getUrlLink());
        }

        contextMap.put("outgoingLinks", targetLinks);

        return renderMacro(contextMap);
    }

    Map<String, Object> getDefaultContext() {
        return MacroUtils.defaultVelocityContext();
    }

    String renderMacro(Map<String, Object> contextMap) {
        return getRenderedTemplate(TEMPLATE, contextMap);
    }

    public BodyType getBodyType() {
        return BodyType.PLAIN_TEXT;
    }

    public OutputType getOutputType() {
        return OutputType.INLINE;
    }

}
