package it.com.servicerocket.chisiang.plugin.linking;

import com.atlassian.confluence.it.Page;
import com.atlassian.confluence.it.User;
import com.atlassian.confluence.pageobjects.component.dialog.WikiMarkupDialog;
import com.atlassian.confluence.pageobjects.page.content.EditContentPage;
import com.atlassian.confluence.pageobjects.page.content.EditorPage;
import com.atlassian.confluence.pageobjects.page.content.ViewPage;
import com.atlassian.confluence.webdriver.AbstractWebDriverTest;
import com.servicerocket.chisiang.plugin.linking.macro.LinkPageMacro;
import it.com.servicerocket.chisiang.plugin.linking.pageobjects.OutgoingLinksMacro;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by CSNg on 25/03/16.
 */
public class LinkingTest extends AbstractWebDriverTest {

//    EditorPage page;
//    WikiMarkupDialog dialog;
//
//    @Before public void setUp() throws Exception {
//        page = product.loginAndEdit(User.TEST, Page.TEST);
//    }
//
//    @Test
//    public void test() throws InterruptedException {
//        String targetURL = "http://www.google.com";
//
//        dialog = page.openWikiMarkupDialog();
//        dialog.setWikiMarkup("{outgoing-links-rewrite}{outgoing-links-rewrite}");
//        dialog.clickInsert();
//        page.
//
//        Thread.sleep(100000);
//        page.save();
//
//        ViewPage viewPage = product.visit(ViewPage.class, Page.TEST);
//        List<OutgoingLinksMacro> outgoingLinksMacros = OutgoingLinksMacro.getAll(product.getPageBinder(), viewPage);
//
//        assertEquals("Should have display the correct URL link in Outgoing Links", targetURL, outgoingLinksMacros.get(0).getLinkText());
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        product.logOut();
//    }

}
