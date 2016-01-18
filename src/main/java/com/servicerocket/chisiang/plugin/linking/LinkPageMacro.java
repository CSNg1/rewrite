package com.servicerocket.chisiang.plugin.linking;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.confluence.pages.Page;
import com.atlassian.confluence.pages.PageManager;
import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.confluence.setup.settings.SettingsManager;
import com.atlassian.renderer.links.Link;
import com.atlassian.renderer.links.LinkResolver;

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

    protected final SettingsManager settingsManager;

    public LinkPageMacro(PageManager pageManager, LinkResolver linkResolver, SettingsManager settingsManager) {
        this.pageManager = pageManager;
        this.linkResolver = linkResolver;
        this.settingsManager = settingsManager;
    }

    public String execute(Map<String, String> map, String s, ConversionContext conversionContext) throws MacroExecutionException {
        Map<String, Object> contextMap = getDefaultContext();
        String pattern = "([A-Z]{1,4}):(.*)";
        Pattern r = Pattern.compile(pattern);

        Matcher m = r.matcher(map.get("linkText"));

        Page targetPage = null;
        if (m.matches()) {
            targetPage = pageManager.getPage(map.get("linkText").replaceAll(pattern, "$1"), map.get("linkText").replaceAll(pattern, "$2"));
        } else {
            targetPage = pageManager.getPage(conversionContext.getSpaceKey(), map.get("linkText"));
        }

        String url = "$" + targetPage.getId();

        Link link = getLinkResolver().createLink(conversionContext.getPageContext(), url);

        contextMap.put("pageLink", getSettingsManager().getGlobalSettings().getBaseUrl() + link.getUrl());

        return renderMacro(contextMap);
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

    private SettingsManager getSettingsManager() { return settingsManager; }
}
