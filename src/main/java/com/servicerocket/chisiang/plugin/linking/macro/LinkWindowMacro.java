package com.servicerocket.chisiang.plugin.linking.macro;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.core.ContextPathHolder;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.confluence.pages.Page;
import com.atlassian.confluence.pages.PageManager;
import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.renderer.links.Link;
import com.atlassian.renderer.links.LinkResolver;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.atlassian.confluence.util.velocity.VelocityUtils.getRenderedTemplate;
import static com.servicerocket.chisiang.plugin.linking.PluginInfo.LinkWindowMacro.TEMPLATE;

/**
 * @author CSNg
 * @since 1.0.0.20160115
 */
public class LinkWindowMacro extends AbstractMacro {

    protected final PageManager pageManager;
    protected final LinkResolver linkResolver;
    protected final ContextPathHolder contextPathHolder;

    public LinkWindowMacro(PageManager pageManager, LinkResolver linkResolver, ContextPathHolder contextPathHolder) {
        this.pageManager = pageManager;
        this.linkResolver = linkResolver;
        this.contextPathHolder = contextPathHolder;
    }

    public String execute(Map<String, String> map, String s, ConversionContext conversionContext) throws MacroExecutionException {
        Map<String, Object> contextMap = getDefaultContext();

        String paramPageName = map.get("link");
        String paramLinkText = map.get("linkText");
        String paramWindowType = map.get("windowType");

        String pageUrl = "pageUrl";
        String linkText = "linkText";

        Page targetPage = getConfluencePage(paramPageName, conversionContext.getSpaceKey());

        if (targetPage != null) {
            String url = "$" + targetPage.getId();

            Link link = getLinkResolver().createLink(conversionContext.getPageContext(), url);
            contextMap.put(pageUrl, contextPathHolder.getContextPath() + link.getUrl());
        } else {
            contextMap.put(pageUrl, paramPageName);
        }

        switch (paramWindowType.toLowerCase()) {
            case "normal":
                break;

            case "popup":
                if (map.get("width") != null) contextMap.put("width", map.get("width"));
                if (map.get("height") != null) contextMap.put("height", map.get("height"));
                break;

            case "tab":
                contextMap.put("target", "target=_blank");
                break;

            default:
                break;
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

    private LinkResolver getLinkResolver() {
        return linkResolver;
    }

}
