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
import com.servicerocket.chisiang.plugin.linking.implementation.LinkingImpl;

import java.util.HashMap;
import java.util.Map;

import static com.atlassian.confluence.util.velocity.VelocityUtils.getRenderedTemplate;
import static com.servicerocket.chisiang.plugin.linking.PluginInfo.AddPageMacro.TEMPLATE;

/**
 * @author CSNg
 * @since 1.0.0.20160115
 */
public class LinkToMacro extends AbstractMacro {

    protected final PageManager pageManager;
    protected final LinkResolver linkResolver;
    protected final ContextPathHolder contextPathHolder;
    protected final PageTemplateManager pageTemplateManager;
    protected final SpaceManager spaceManager;

    public LinkToMacro(PageManager pageManager, LinkResolver linkResolver, ContextPathHolder contextPathHolder, PageTemplateManager pageTemplateManager, SpaceManager spaceManager) {
        this.pageManager = pageManager;
        this.linkResolver = linkResolver;
        this.contextPathHolder = contextPathHolder;
        this.pageTemplateManager = pageTemplateManager;
        this.spaceManager = spaceManager;
    }

    public String execute(Map<String, String> map, String s, ConversionContext conversionContext) throws MacroExecutionException {
        LinkingImpl linkingImpl = new LinkingImpl();
        Map<String, Object> contextMap = getDefaultContext();

        String paramLocation = map.get("location");
        String paramLinkText = map.get("linkText");
        String paramSource = map.get("source");
        String paramSourceType = map.get("sourceType");
        String paramPrefix = map.get("prefix");
        String paramPostfix = map.get("postfix");
        String paramParent = map.get("parent");
        String paramLabels = map.get("labels");

        String pageUrl = "pageUrl";
        String linkText = "linkText";

        Map<String, String> locationMap = new HashMap<String, String>() {{
            put("admin", "/admin/console.action");
        }};

        String locationUrl = locationMap.get(paramLocation);

        contextMap.put(pageUrl, contextPathHolder.getContextPath() + locationUrl );

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

}
