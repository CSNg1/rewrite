package com.servicerocket.chisiang.plugin.linking.macro;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.core.ContextPathHolder;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.confluence.pages.Page;
import com.atlassian.confluence.pages.PageManager;
import com.atlassian.confluence.pages.templates.PageTemplate;
import com.atlassian.confluence.pages.templates.PageTemplateManager;
import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.confluence.spaces.SpaceManager;
import com.atlassian.renderer.links.Link;
import com.atlassian.renderer.links.LinkResolver;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.atlassian.confluence.util.velocity.VelocityUtils.getRenderedTemplate;
import static com.servicerocket.chisiang.plugin.linking.PluginInfo.LinkPageMacro.TEMPLATE;

/**
 * @author by CSNg on 15/01/2016.
 * @since 1.0.0.20160115
 */
public class LinkPageMacro extends AbstractMacro {

    protected final PageManager pageManager;
    protected final LinkResolver linkResolver;
    protected final ContextPathHolder contextPathHolder;
    protected final PageTemplateManager pageTemplateManager;
    protected final SpaceManager spaceManager;

    private String pageName = "pageName";
    private String linkText = "linkText";
    private String pageUrl = "pageUrl";

    public LinkPageMacro(PageManager pageManager, LinkResolver linkResolver, ContextPathHolder contextPathHolder, PageTemplateManager pageTemplateManager, SpaceManager spaceManager) {
        this.pageManager = pageManager;
        this.linkResolver = linkResolver;
        this.contextPathHolder = contextPathHolder;
        this.pageTemplateManager = pageTemplateManager;
        this.spaceManager = spaceManager;
    }

    public String execute(Map<String, String> map, String s, ConversionContext conversionContext) throws MacroExecutionException {
        Map<String, Object> contextMap = getDefaultContext();

        String linkTextRegex = "([A-Z]{1,4}):(.*)";
        Pattern linkTextPattern = Pattern.compile(linkTextRegex);
        Matcher linkTextMatcher = linkTextPattern.matcher(map.get(pageName));

        Page targetPage = null;

        if (linkTextMatcher.matches()) {
            targetPage = pageManager.getPage(map.get(pageName).replaceAll(linkTextRegex, "$1"), map.get(pageName).replaceAll(linkTextRegex, "$2"));
        } else {
            targetPage = pageManager.getPage(conversionContext.getSpaceKey(), map.get(pageName));
        }

        if (targetPage != null) {
            String url = "$" + targetPage.getId();

            Link link = getLinkResolver().createLink(conversionContext.getPageContext(), url);
            contextMap.put(pageUrl, contextPathHolder.getContextPath() + link.getUrl());

        } else {
            Map<String, String> newPageParams = new HashMap<String, String>();
            newPageParams.put("spaceKey", conversionContext.getSpaceKey());
            newPageParams.put("title", map.get(linkText));

            if (map.get("source") != null && map.get("sourceType").equals("template")) {
                PageTemplate template = pageTemplateManager.getPageTemplate(map.get("source"), spaceManager.getSpace(conversionContext.getSpaceKey()));
                newPageParams.put("templateId", Long.toString(template.getId()));
            }
            contextMap.put(pageUrl, generateUrl(newPageParams));

        }

        contextMap.put(linkText, map.get(linkText));

        return renderMacro(contextMap);
    }

    private String generateUrl(Map<String, String> params) {
        StringBuilder url = new StringBuilder();
        url.append(contextPathHolder.getContextPath());
        url.append("/pages/createpage-entervariables-labeled.action?");

        for (Map.Entry<String, String> entry : params.entrySet()) {
            url.append(entry.getKey() + "=" + entry.getValue() + "&");
        }

        return url.toString();
    }

    Map<String, Object> getDefaultContext() {
        return MacroUtils.defaultVelocityContext();
    }

    String renderMacro(Map<String, Object> contextMap) {
        return getRenderedTemplate(TEMPLATE, contextMap);
    }

    public BodyType getBodyType() {
        return BodyType.NONE;
    }

    public OutputType getOutputType() {
        return OutputType.INLINE;
    }

    private LinkResolver getLinkResolver() { return linkResolver; }

}
