package com.servicerocket.chisiang.plugin.linking.macro;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.links.OutgoingLink;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.confluence.pages.PageManager;
import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.atlassian.confluence.util.velocity.VelocityUtils.getRenderedTemplate;
import static com.servicerocket.chisiang.plugin.linking.PluginInfo.UndefinedLinksMacro.TEMPLATE;

/**
 * @author CSNg
 * @since  1.0.0.20160401
 */
public class UndefinedLinksMacro extends AbstractMacro {

    protected final PageManager pageManager;

    public UndefinedLinksMacro(PageManager pageManager) { this.pageManager = pageManager; }

    public String execute(Map<String, String> map, String s, ConversionContext conversionContext) throws MacroExecutionException {
        Map<String, Object> contextMap = getDefaultContext();

        List<OutgoingLink> undefinedLinks = pageManager.getUndefinedPages(conversionContext.getSpaceKey());

        List<String> targetLinks = new ArrayList<>();
        for (OutgoingLink undefinedLink : undefinedLinks) {
            targetLinks.add(undefinedLink.getUrlLink());
        }

        contextMap.put("undefinedLinks", targetLinks);

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
