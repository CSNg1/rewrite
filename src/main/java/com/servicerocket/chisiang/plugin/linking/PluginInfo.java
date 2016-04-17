package com.servicerocket.chisiang.plugin.linking;

/**
 * @author CSNg
 * @since  1.0.0.20160118
 */
public final class PluginInfo {
    public static final String PLUGIN_KEY = "com.servicerocket.chisiang.plugin.com.servicerocket.chisiang.plugin.linking";
    public static final String ERR_TEMPLATE = "templates/macro/error.vm";

    public interface AddPageMacro {
        static final String NAME = "Add Page";
        static final String TEMPLATE = "templates/macro/addpage.vm";
    }

    public interface AddPageFormMacro {
        static final String NAME = "Add Page Form";
        static final String TEMPLATE = "templates/macro/addpageform.vm";
    }

    public interface LinkFromMacro {
        static final String NAME = "Link From";
        static final String TEMPLATE = "templates/macro/linkfrom.vm";
    }

    public interface LinkPageMacro {
        static final String NAME = "Link Page";
        static final String TEMPLATE = "templates/macro/linkpage.vm";
    }

    public interface LinkToMacro {
        static final String NAME = "Link To Location";
        static final String TEMPLATE = "templates/macro/linkto.vm";
    }

    public interface LinkWindowMacro {
        static final String NAME = "Link in New Window";
        static final String TEMPLATE = "templates/macro/linkwindow.vm";
    }

    public interface OutgoingLinksMacro {
        static final String NAME = "Outgoing Links";
        static final String TEMPLATE = "templates/macro/outgoinglinks.vm";
    }

    public interface IncomingLinksMacro {
        static final String NAME = "Incoming Links";
        static final String TEMPLATE = "templates/macro/incominglinks.vm";
    }

    public interface UndefinedLinksMacro {
        static final String NAME = "Undefined Links";
        static final String TEMPLATE = "templates/macro/undefinedlinks.vm";
    }

    public interface OrphanedLinksMacro {
        static final String NAME = "Orphaned Links";
        static final String TEMPLATE = "templates/macro/orphanedlinks.vm";
    }
}
