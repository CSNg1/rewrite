package com.servicerocket.chisiang.plugin.linking;

/**
 * Created by CSNg on 18/01/2016.
 */
public final class PluginInfo {
    public static final String PLUGIN_KEY = "com.servicerocket.chisiang.plugin.linking";

    public interface LinkPageMacro {
        static final String NAME = "Link Page";
        static final String TEMPLATE = "templates/linkpage.vm";
    }
}
