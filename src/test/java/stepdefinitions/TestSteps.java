package stepdefinitions;

import com.Cucumber.automation.framework.basetest.TestClass;
import com.Cucumber.automation.framework.basetest.TestContext;
import com.Cucumber.automation.framework.constants.AppConstants;
import com.Cucumber.automation.test.pageFactory.PageClass;
import com.Cucumber.automation.test.pageFactory.TestPage;
import io.cucumber.java.en.Given;
import org.apache.logging.log4j.ThreadContext;
import org.testng.Assert;
import org.testng.asserts.Assertion;

public class TestSteps extends TestClass {

    TestContext testContext;
    TestPage testPage;
    PageClass pageClass;

    public TestSteps(TestContext context) throws Exception {
        super();
        this.testContext = context;
//        pageClass = testContext.getPageObjectManager().getCommonPage();
        ThreadContext.pop();
        ThreadContext.push(this.getClass().getSimpleName());
    }

    @Given("user opens Konakart application in browser")
    public void user_opens_konakart_application_in_browser() {

        testContext.getDriver().get(AppConstants.Web.UI_BASE_URL);
        String title = testContext.getDriver().getTitle();
        log.info("title of the page : " + title);
    }
}
