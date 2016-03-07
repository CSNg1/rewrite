package com.servicerocket.chisiang.plugin.linking;

/**
 * @author CSNg
 * @since  1.0.0.20160118
 */
public final class PluginInfo {
    public static final String PLUGIN_KEY = "com.servicerocket.chisiang.plugin.com.servicerocket.chisiang.plugin.linking";

    public interface AddPageMacro {
        static final String NAME = "Add Page";
        static final String TEMPLATE = "templates/macro/addpage.vm";
    }

    public interface LinkPageMacro {
        static final String NAME = "Link Page";
        static final String TEMPLATE = "templates/macro/linkpage.vm";
    }

    public interface LinkWindowMacro {
        static final String NAME = "Link in New Window";
        static final String TEMPLATE = "templates/macro/linkwindow.vm";
    }
}
