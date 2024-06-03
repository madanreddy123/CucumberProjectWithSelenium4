package com.Cucumber.automation.framework.utils;

import com.Cucumber.automation.framework.constants.FrameworkConstants;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WebUtils {

    protected WebDriver _driver;
    protected WebDriverWait wait;

    protected Logging log = new Logging(this.getClass().getName());

    public WebUtils(WebDriver driver) {
        this._driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(FrameworkConstants.MediumWait));
//        log.info("Class " + this.getClass().getName() + " initiated");
        ThreadContext.pop();
        ThreadContext.push(this.getClass().getSimpleName());
        PageFactory.initElements(driver, this);
    }

    public void refreshPage() {
        this._driver.navigate().refresh();
    }

    public void takeScreenshot(Scenario name) {
        try {
            final byte[] screenshot = ((TakesScreenshot) _driver).getScreenshotAs(OutputType.BYTES);
            name.attach(screenshot, "image/png", name.getName());
        } catch (Exception e) {
            log.error("Could not capture screenshot. " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void takeScreenshotForFailedTestCases(Scenario name) {
        if (name.isFailed()) {
            final byte[] screenshot = ((TakesScreenshot) _driver).getScreenshotAs(OutputType.BYTES);
            name.attach(screenshot, "image/png", name.getName());
        }
    }

    public void closeWindow() {
        try {
            this._driver.close();
            log.info("Current window has been closed.");
        } catch (Exception e) {
            log.error("Current window could not be closed. " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void launchUrlInNewTab(String url) {
        try {
            String link = "window.open('" + url + "');";
            ((JavascriptExecutor) _driver).executeScript(link);
            log.info("Open new tab by pressing Ctrl+T");
        } catch (NoSuchWindowException ns) {
            log.error("No window exist. " + ns.getMessage());
            ns.printStackTrace();
        } catch (Exception e) {
            log.error("Error occurred while opening new tab. " + e.getMessage());
            e.printStackTrace();
        }
    }
}
