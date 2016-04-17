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
import com.servicerocket.chisiang.plugin.linking.implementation.LinkingImpl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.atlassian.confluence.util.velocity.VelocityUtils.getRenderedTemplate;
import static com.servicerocket.chisiang.plugin.linking.PluginInfo.LinkPageMacro.TEMPLATE;

/**
 * @author CSNg
 * @since  1.0.0.20160115
 */
public class LinkPageMacro extends AbstractMacro {

    protected final PageManager pageManager;
    protected final LinkResolver linkResolver;
    protected final ContextPathHolder contextPathHolder;
    protected final PageTemplateManager pageTemplateManager;
    protected final SpaceManager spaceManager;

    public LinkPageMacro(PageManager pageManager, LinkResolver linkResolver, ContextPathHolder contextPathHolder, PageTemplateManager pageTemplateManager, SpaceManager spaceManager) {
        this.pageManager = pageManager;
        this.linkResolver = linkResolver;
        this.contextPathHolder = contextPathHolder;
        this.pageTemplateManager = pageTemplateManager;
        this.spaceManager = spaceManager;
    }

    public String execute(Map<String, String> map, String s, ConversionContext conversionContext) throws MacroExecutionException {
        LinkingImpl linkingImpl = new LinkingImpl();

        Map<String, Object> contextMap = getDefaultContext();

        String paramPageName = map.get("pageName");
        String paramLinkText = map.get("linkText");
        String paramSource = map.get("source");
        String paramSourceType = map.get("sourceType");
        String paramPrefix = map.get("prefix");
        String paramPostfix = map.get("postfix");
        String paramParent = map.get("parent");
        String paramLabels = map.get("labels");

        String pageUrl = "pageUrl";
        String linkText = "linkText";

        Page targetPage = getConfluencePage(paramPageName, conversionContext.getSpaceKey());

        if (targetPage != null) {
            String url = "$" + targetPage.getId();

            Link link = getLinkResolver().createLink(conversionContext.getPageContext(), url);
            contextMap.put(pageUrl, contextPathHolder.getContextPath() + link.getUrl());
        } else if (paramPageName.startsWith("http") || paramPageName.startsWith("www.")) {
            contextMap.put(pageUrl, paramPageName);
        } else {
            String actionString = "/pages/createpage-entervariables.action?";
            Map<String, String> targetPageParams = new LinkedHashMap<>();

            targetPageParams.put("spaceKey", conversionContext.getSpaceKey());

            targetPageParams.put("title", linkingImpl.formPageTitle(paramPrefix, paramPageName, paramPostfix));

            //TODO: https://jira.atlassian.com/browse/CONF-23704 (default macro parameters ignored)
            if (paramSourceType != null) {
                switch (paramSourceType.toLowerCase()) {
                    case "page":
                        //TODO
                        break;

                    case "template":
                        PageTemplate template = pageTemplateManager.getPageTemplate(paramSource, spaceManager.getSpace(conversionContext.getSpaceKey()));
                        if (template != null) targetPageParams.put("templateId", Long.toString(template.getId()));
                        break;

                    default:
                        break;
                }
            }

            //TODO: Confluence createpage-entervariables action's labelsString is broken
            if (paramLabels != null) targetPageParams.put("labelsString", paramLabels);

            if (paramParent != null) {
                switch (paramParent.toLowerCase()) {
                    case "@self":
                        targetPageParams.put("fromPageId", pageManager.getPage(conversionContext.getEntity().getId()).getIdAsString());
                        break;

                    case "@parent":
                        targetPageParams.put("fromPageId", pageManager.getPage(conversionContext.getEntity().getId()).getParent().getIdAsString());
                        break;

                    case "@home":
                        break;

                    default:
                        break;
                }
            }
            contextMap.put(pageUrl, linkingImpl.buildUrl(contextPathHolder.getContextPath(), actionString, targetPageParams));
        }

        contextMap.put(linkText, paramLinkText);

        return renderMacro(contextMap);
    }

    private Page getConfluencePage(String pageName, String spaceKey) {
        Page confPage;

        String linkTextRegex = "([A-Z]{1,5}):(.*)";
        Pattern linkTextPattern = Pattern.compile(linkTextRegex);
        Matcher linkTextMatcher = linkTextPattern.matcher(pageName);

        if (linkTextMatcher.matches()) {
            confPage = pageManager.getPage(pageName.replaceAll(linkTextRegex, "$1"), pageName.replaceAll(linkTextRegex, "$2"));
        } else {
            confPage = pageManager.getPage(spaceKey, pageName);
        }

        return confPage;
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
