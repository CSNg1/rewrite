package com.servicerocket.chisiang.plugin.linking.macro;

import com.servicerocket.chisiang.plugin.linking.implementation.LinkPageImpl;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.junit.Assert.assertEquals;

/**
 * @author CSNg
 * @since 1.0.0.20160229
 */
public class LinkPageUnitTest {

    LinkPageImpl linkPage = new LinkPageImpl();

    @Test public void testPageTitleWithPrefix() {
        String prefix = randomAlphanumeric(5);
        String title = randomAlphanumeric(5);

        String pageTitle = linkPage.formPageTitle(prefix, title, null);

        assertEquals(prefix + title, pageTitle);
    }

    @Test public void testPageTitleWithPostfix() {
        String title = randomAlphanumeric(5);
        String postfix = randomAlphanumeric(5);

        String pageTitle = linkPage.formPageTitle(null, title, postfix);

        assertEquals(title + postfix, pageTitle);
    }

    @Test public void testPageTitleWithPrefixAndPostfix() {
        String prefix = randomAlphanumeric(5);
        String title = randomAlphanumeric(5);
        String postfix = randomAlphanumeric(5);

        String pageTitle = linkPage.formPageTitle(prefix, title, postfix);

        assertEquals(prefix + title + postfix, pageTitle);
    }

    @Test public void testBuildUrl() {
        String contextPath = randomAlphanumeric(5);
        String actionString = randomAlphanumeric(5);

        Map<String, String> pageParams = new HashMap<>();
        String param1 = randomAlphanumeric(5);
        String value1 = randomAlphanumeric(5);
        String param2 = randomAlphanumeric(5);
        String value2 = randomAlphanumeric(5);
        pageParams.put(param1, value1);
        pageParams.put(param2, value2);

        String url = linkPage.buildUrl(contextPath, actionString, pageParams);

        StringBuilder expectedUrl = new StringBuilder();
        expectedUrl.append(contextPath);
        expectedUrl.append(actionString);
        expectedUrl.append(param1 + "=" + value1 + "&");
        expectedUrl.append(param2 + "=" + value2);

        assertEquals(expectedUrl.toString(), url);
    }
}
