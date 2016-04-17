package com.servicerocket.chisiang.plugin.linking.macro;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.core.ContextPathHolder;
import com.atlassian.confluence.links.LinkManager;
import com.atlassian.confluence.links.OutgoingLink;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.confluence.pages.Page;
import com.atlassian.confluence.pages.PageManager;
import com.atlassian.confluence.pages.actions.beans.PageIncomingLinks;
import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.confluence.security.PermissionManager;
import com.atlassian.confluence.spaces.SpaceManager;
import com.atlassian.confluence.user.AuthenticatedUserThreadLocal;
import com.atlassian.confluence.user.ConfluenceUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.atlassian.confluence.util.velocity.VelocityUtils.getRenderedTemplate;
import static com.servicerocket.chisiang.plugin.linking.PluginInfo.IncomingLinksMacro.TEMPLATE;

/**
 * @author CSNg
 * @since  1.0.0.20160115
 */
public class IncomingLinksMacro extends AbstractMacro {

    protected final PageManager pageManager;
    protected final ContextPathHolder contextPathHolder;
    protected final SpaceManager spaceManager;
    protected final PermissionManager permissionManager;
    protected final LinkManager linkManager;

    public IncomingLinksMacro(PageManager pageManager, ContextPathHolder contextPathHolder, SpaceManager spaceManager, PermissionManager permissionManager, LinkManager linkManager) {
        this.pageManager = pageManager;
        this.contextPathHolder = contextPathHolder;
        this.spaceManager = spaceManager;
        this.permissionManager = permissionManager;
        this.linkManager = linkManager;
    }

    public String execute(Map<String, String> map, String s, ConversionContext conversionContext) throws MacroExecutionException {
        Map<String, Object> contextMap = getDefaultContext();

        Page targetPage = pageManager.getPage(conversionContext.getSpaceKey(), conversionContext.getPageContext().getPageTitle());

        AuthenticatedUserThreadLocal authenticatedUserThreadLocal = new AuthenticatedUserThreadLocal();

        ConfluenceUser currentUser = authenticatedUserThreadLocal.get();

        PageIncomingLinks pageIncomingLinks = new PageIncomingLinks(linkManager, permissionManager);

        List<OutgoingLink> incomingLinks = pageIncomingLinks.getIncomingLinks(targetPage, currentUser);

        List<String> targetLinks = new ArrayList<>();
        for (OutgoingLink ogl : incomingLinks) {
            targetLinks.add(ogl.getUrlLink());
        }

        contextMap.put("incomingLinks", targetLinks);

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
