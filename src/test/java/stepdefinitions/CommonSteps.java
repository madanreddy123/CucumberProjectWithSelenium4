package stepdefinitions;

import com.Cucumber.automation.framework.basetest.TestClass;
import com.Cucumber.automation.framework.basetest.TestContext;
import com.Cucumber.automation.test.pageFactory.PageClass;
import io.cucumber.core.gherkin.Step;
import io.cucumber.java.*;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterSuite;

public class CommonSteps extends TestClass {
    TestContext testContext;
    PageClass pageClass;
    WebDriver driver;

    public CommonSteps(TestContext context) throws Exception {
        this.testContext = context;
        pageClass = testContext.getPageObjectManager().getCommonPage();
//        ThreadContext.pop();
//        ThreadContext.push(this.getClass().getSimpleName());
    }

    @Before
    public void testSetup(Scenario name) {


        log.info("Scenario Name: " + name.getName());
    }

    @After
    public void tearDown(Scenario name) {
        testContext.getDriver().quit();
        log.info("Execution status is: " + name.getStatus());
    }

    @AfterStep
    public void addScreenshot(Scenario name) {
        pageClass.takeScreenshot(name);
    }
}
