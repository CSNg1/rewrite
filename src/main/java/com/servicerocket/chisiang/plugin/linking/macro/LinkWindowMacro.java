package com.servicerocket.chisiang.plugin.linking.macro;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.core.ContextPathHolder;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.confluence.pages.Page;
import com.atlassian.confluence.pages.PageManager;
import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.confluence.velocity.htmlsafe.HtmlSafe;
import com.atlassian.renderer.links.Link;
import com.atlassian.renderer.links.LinkResolver;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.atlassian.confluence.util.velocity.VelocityUtils.getRenderedTemplate;
import static com.servicerocket.chisiang.plugin.linking.PluginInfo.LinkWindowMacro.TEMPLATE;

/**
 * @author CSNg
 * @since  1.0.0.20160115
 */
public class LinkWindowMacro extends AbstractMacro {

    protected final PageManager pageManager;
    protected final LinkResolver linkResolver;
    protected final ContextPathHolder contextPathHolder;

    private String pageUrl = "pageUrl";
    private String linkText = "linkText";

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

        String confPageRegex = "([A-Z]{1,4}):(.*)";
        Pattern confPagePattern = Pattern.compile(confPageRegex);
        Matcher confPageMatcher = confPagePattern.matcher(paramPageName);

        Page targetPage = pageManager.getPage(conversionContext.getSpaceKey(), paramPageName);

        if (confPageMatcher.matches()) {
            targetPage = pageManager.getPage(paramPageName.replaceAll(confPageRegex, "$1"), paramPageName.replaceAll(confPageRegex, "$2"));
        }

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
                //contextMap.put("width", 250);
                contextMap.put("height", 250);
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

    @HtmlSafe
    private String getOnClick() {
        return "width=300, height=250";
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
