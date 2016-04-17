package com.servicerocket.chisiang.plugin.linking.macro;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.confluence.pages.Page;
import com.atlassian.confluence.pages.PageManager;
import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.renderer.links.Link;
import com.atlassian.renderer.links.LinkResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.atlassian.confluence.util.velocity.VelocityUtils.getRenderedTemplate;
import static com.servicerocket.chisiang.plugin.linking.PluginInfo.OrphanedLinksMacro.TEMPLATE;

/**
 * @author CSNg
 * @since  1.0.0.20160401
 */
public class OrphanedLinksMacro extends AbstractMacro {

    protected final PageManager pageManager;
    protected final LinkResolver linkResolver;

    public OrphanedLinksMacro(PageManager pageManager, LinkResolver linkResolver) {
        this.pageManager = pageManager;
        this.linkResolver = linkResolver;
    }

    public String execute(Map<String, String> map, String s, ConversionContext conversionContext) throws MacroExecutionException {
        Map<String, Object> contextMap = getDefaultContext();

        List<Page> orphanedPages = pageManager.getOrphanedPages(conversionContext.getSpaceKey());

        List<String> targetLinks = new ArrayList<>();
        for (Page orphanedPage : orphanedPages) {
            String url = "$" + orphanedPage.getId();

            Link link = getLinkResolver().createLink(conversionContext.getPageContext(), url);
            targetLinks.add(link.getUrl());
        }

        contextMap.put("orphanedLinks", targetLinks);

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

    private LinkResolver getLinkResolver() { return linkResolver; }

}
