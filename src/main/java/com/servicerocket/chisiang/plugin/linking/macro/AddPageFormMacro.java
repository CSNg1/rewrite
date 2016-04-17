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
import com.atlassian.renderer.links.LinkResolver;
import com.servicerocket.chisiang.plugin.linking.implementation.LinkingImpl;
import org.apache.commons.lang.RandomStringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.atlassian.confluence.util.velocity.VelocityUtils.getRenderedTemplate;
import static com.servicerocket.chisiang.plugin.linking.PluginInfo.AddPageFormMacro.TEMPLATE;
import static com.servicerocket.chisiang.plugin.linking.PluginInfo.ERR_TEMPLATE;

/**
 * @author CSNg
 * @since 1.0.0.20160403
 */
public class AddPageFormMacro extends AbstractMacro {

    protected final PageManager pageManager;
    protected final LinkResolver linkResolver;
    protected final ContextPathHolder contextPathHolder;
    protected final PageTemplateManager pageTemplateManager;
    protected final SpaceManager spaceManager;

    public AddPageFormMacro(PageManager pageManager, LinkResolver linkResolver, ContextPathHolder contextPathHolder, PageTemplateManager pageTemplateManager, SpaceManager spaceManager) {
        this.pageManager = pageManager;
        this.linkResolver = linkResolver;
        this.contextPathHolder = contextPathHolder;
        this.pageTemplateManager = pageTemplateManager;
        this.spaceManager = spaceManager;
    }

    public String execute(Map<String, String> map, String s, ConversionContext conversionContext) throws MacroExecutionException {
        LinkingImpl linkingImpl = new LinkingImpl();
        Map<String, Object> contextMap = getDefaultContext();
        String formId = RandomStringUtils.randomAlphabetic(6);

        String paramLinkText = map.containsKey("linkText") ? map.get("linkText") : "";
        String paramTemplate = map.containsKey("template") ? map.get("template") : "";
        String paramPrefix = map.containsKey("prefix") ? map.get("prefix") : "";
        String paramPostfix = map.containsKey("postfix") ? map.get("postfix") : "";
        String paramParent = map.containsKey("parent") ? map.get("parent") : "";
        String paramLabels = map.containsKey("labels") ? map.get("labels") : "";

        String actionString = "/pages/createpage-entervariables.action?";
        Map<String, String> targetPageParams = new HashMap<>();

        targetPageParams.put("spaceKey", conversionContext.getSpaceKey());

        if (!paramTemplate.isEmpty()) {
            PageTemplate template;
            template = pageTemplateManager.getPageTemplate(paramTemplate, spaceManager.getSpace(conversionContext.getSpaceKey()));

            if (template == null) template = pageTemplateManager.getGlobalPageTemplate(paramTemplate);

            if (template != null) {
                targetPageParams.put("templateId", Long.toString(template.getId()));
            } else {
                return renderErr("The assigned template does not exist");
            }
        }

        String parentPageId;
        if (!paramParent.isEmpty()) {
            switch (paramParent.toLowerCase()) {
                case "@self":
                    targetPageParams.put("fromPageId", pageManager.getPage(conversionContext.getEntity().getId()).getIdAsString());
                    break;

                case "@parent":
                    parentPageId = pageManager.getPage(conversionContext.getEntity().getId()).getParent().getIdAsString();
                    if (parentPageId != null) {
                        targetPageParams.put("fromPageId", parentPageId);
                    } else {
                        return renderErr("Parent page does not exist");
                    }
                    break;

                case "@home":
                    break;

                default:
                    Page parentPage = getConfluencePage(paramParent, conversionContext.getSpaceKey());
                    if (parentPage != null) {
                        targetPageParams.put("fromPageId", parentPage.getIdAsString());
                        targetPageParams.put("spaceKey", parentPage.getSpaceKey());
                    } else {
                        return renderErr("Parent page does not exist");
                    }
                    break;
            }
        }

        //TODO: Confluence createpage-entervariables action's labelsString is broken
        if (!paramLabels.isEmpty()) targetPageParams.put("labelsString", paramLabels);

        contextMap.put("pageUrl", linkingImpl.buildUrl(contextPathHolder.getContextPath(), actionString, targetPageParams));
        contextMap.put("linkText", paramLinkText);
        contextMap.put("prefix", paramPrefix);
        contextMap.put("postfix", paramPostfix);
        contextMap.put("formId", formId);

        return renderMacro(contextMap);
    }

    private Page getConfluencePage(String pageName, String spaceKey) {
        Page confPage;

        String linkTextRegex = "([A-Z0-9]{1,5}):(.*)";
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

    String renderErr(String errorMsg) {
        Map<String, Object> contextMap = getDefaultContext();
        contextMap.put("errorMsg", errorMsg);
        return getRenderedTemplate(ERR_TEMPLATE, contextMap);
    }

    public BodyType getBodyType() {
        return BodyType.NONE;
    }

    public OutputType getOutputType() {
        return OutputType.INLINE;
    }

}
