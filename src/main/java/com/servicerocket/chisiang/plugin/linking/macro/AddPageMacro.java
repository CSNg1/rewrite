package com.servicerocket.chisiang.plugin.linking.macro;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.core.ContextPathHolder;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.confluence.pages.PageManager;
import com.atlassian.confluence.pages.templates.PageTemplate;
import com.atlassian.confluence.pages.templates.PageTemplateManager;
import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.confluence.spaces.SpaceManager;
import com.atlassian.renderer.links.LinkResolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.atlassian.confluence.util.velocity.VelocityUtils.getRenderedTemplate;
import static com.servicerocket.chisiang.plugin.linking.PluginInfo.AddPageMacro.TEMPLATE;

/**
 * @author CSNg
 * @since 1.0.0.20160115
 */
public class AddPageMacro extends AbstractMacro {

    protected final PageManager pageManager;
    protected final LinkResolver linkResolver;
    protected final ContextPathHolder contextPathHolder;
    protected final PageTemplateManager pageTemplateManager;
    protected final SpaceManager spaceManager;

    private String pageUrl = "pageUrl";

    public AddPageMacro(PageManager pageManager, LinkResolver linkResolver, ContextPathHolder contextPathHolder, PageTemplateManager pageTemplateManager, SpaceManager spaceManager) {
        this.pageManager = pageManager;
        this.linkResolver = linkResolver;
        this.contextPathHolder = contextPathHolder;
        this.pageTemplateManager = pageTemplateManager;
        this.spaceManager = spaceManager;
    }

    public String execute(Map<String, String> map, String s, ConversionContext conversionContext) throws MacroExecutionException {
        Map<String, Object> contextMap = getDefaultContext();

        String paramPageName = map.get("pageName");
        String paramLinkText = map.get("linkText");
        String paramSource = map.get("source");
        String paramSourceType = map.get("sourceType");
        String paramPrefix = map.get("prefix");
        String paramPostfix = map.get("postfix");
        String paramParent = map.get("parent");
        String paramLabels = map.get("labels");

        String actionString = "/pages/createpage-entervariables.action?";
        Map<String, String> targetPageParams = new HashMap<>();

        targetPageParams.put("spaceKey", conversionContext.getSpaceKey());

        targetPageParams.put("title", formPageTitle(paramPrefix, paramPageName, paramPostfix));

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
                    //TODO
                    break;

                default:
                    break;
            }
        }
        contextMap.put(pageUrl, buildUrl(actionString, targetPageParams));

        String linkText = "linkText";
        contextMap.put(linkText, paramLinkText);

        return renderMacro(contextMap);
    }

    public String formPageTitle(String prefix, String pageName, String postfix) {
        if (prefix != null) pageName = prefix + pageName;

        if (postfix != null) pageName = pageName + postfix;

        return pageName;
    }

    private String buildUrl(String actionString, Map<String, String> params) {
        StringBuilder url = new StringBuilder();
        url.append(contextPathHolder.getContextPath());
        url.append(actionString);

        List<String> tempList = new ArrayList<>();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            tempList.add(entry.getKey() + "=" + entry.getValue());
        }

        url.append(String.join("&", tempList));

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

}
