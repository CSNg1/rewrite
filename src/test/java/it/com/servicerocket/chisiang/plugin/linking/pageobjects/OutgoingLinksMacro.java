package it.com.servicerocket.chisiang.plugin.linking.pageobjects;

import com.atlassian.confluence.pageobjects.component.ConfluenceAbstractPageComponent;
import com.atlassian.confluence.pageobjects.page.content.ViewPage;
import com.atlassian.pageobjects.PageBinder;
import com.atlassian.pageobjects.elements.PageElement;
import com.atlassian.pageobjects.elements.query.Poller;
import com.atlassian.pageobjects.elements.query.TimedCondition;
import org.openqa.selenium.By;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CSNg on 29/03/16.
 */
public class OutgoingLinksMacro extends ConfluenceAbstractPageComponent {

    private PageElement pageElement;
    private String linkText;
    private String urlLink;

    public static List<OutgoingLinksMacro> getAll(PageBinder pageBinder, ViewPage viewPage) {

        List<PageElement> pageElements = viewPage.getMainContent().findAll(By.cssSelector("#main-content ul li a"));

        List<OutgoingLinksMacro> outgoingLinksMacros = new ArrayList<OutgoingLinksMacro>();
        for (PageElement pageElement : pageElements) {
            outgoingLinksMacros.add(pageBinder.bind(OutgoingLinksMacro.class, pageElement));
        }

        return outgoingLinksMacros;
    }

    public OutgoingLinksMacro(PageElement pageElement) {
        this.pageElement = pageElement;
        Poller.waitUntilTrue(isReady());

        this.linkText = pageElement.getText();
        this.urlLink = pageElement.getAttribute("href");
    }

    public TimedCondition isReady() {
        return pageElement.timed().isVisible();
    }

    public String getLinkText() {
        return linkText;
    }

    public String getUrlLink() {
        return urlLink;
    }
}

