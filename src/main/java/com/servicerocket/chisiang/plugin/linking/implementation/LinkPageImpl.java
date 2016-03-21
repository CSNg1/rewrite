package com.servicerocket.chisiang.plugin.linking.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author CSNg
 * @since 1.0.0.20160321
 */
public class LinkPageImpl {

    public String formPageTitle(String prefix, String pageName, String postfix) {
        if (prefix != null) pageName = prefix + pageName;

        if (postfix != null) pageName = pageName + postfix;

        return pageName;
    }

    public String buildUrl(String contextPath, String actionString, Map<String, String> pageParams) {
        StringBuilder url = new StringBuilder();
        url.append(contextPath);
        url.append(actionString);

        List<String> paramList = new ArrayList<>();

        for (Map.Entry<String, String> entry : pageParams.entrySet()) {
            paramList.add(entry.getKey() + "=" + entry.getValue());
        }

        url.append(String.join("&", paramList));

        return url.toString();
    }
}
