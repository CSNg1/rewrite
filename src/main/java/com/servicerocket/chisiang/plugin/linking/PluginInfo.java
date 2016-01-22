package com.servicerocket.chisiang.plugin.linking;

/**
 * @author by CSNg on 18/01/2016.
 * @since 1.0.0.20160118
 */
public final class PluginInfo {
    public static final String PLUGIN_KEY = "com.servicerocket.chisiang.plugin.linking";

    public interface LinkPageMacro {
        static final String NAME = "Link Page";
        static final String TEMPLATE = "templates/macro/linkpage.vm";
    }
}
