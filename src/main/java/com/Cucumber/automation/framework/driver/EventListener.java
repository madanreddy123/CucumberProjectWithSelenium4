package com.Cucumber.automation.framework.driver;

import com.Cucumber.automation.framework.constants.FrameworkConstants;
import com.Cucumber.automation.framework.utils.Logging;
import com.Cucumber.automation.framework.utils.Screenshot;
import com.Cucumber.automation.framework.utils.PropertyReader;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.io.IOException;
import java.time.Duration;

public class EventListener {
    Screenshot ss;
    private Logging log = new Logging(super.getClass().getSimpleName());


    public void beforeClickOn(WebElement element, WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(FrameworkConstants.LargeWait));
        long tStart = System.currentTimeMillis();
        long maxWaitSeconds = FrameworkConstants.LargeWait;

        while (true) {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(element));
                this.hold();
                break;
            } catch (WebDriverException var11) {
                long tElapsed = System.currentTimeMillis() - tStart;
                tElapsed /= FrameworkConstants.MaximumWait;
                log.warn("Waiting for ExpectedCondition (Element to be clickable): " + tElapsed
                        + " seconds elapsed out of " + maxWaitSeconds);
                if (tElapsed > maxWaitSeconds) { break; }
            }
        }

        this.log.info("Attempting to click: '" + element + "'");
    }


    public void afterClickOn(WebElement element, WebDriver driver) {
        this.waitForAjax(driver, FrameworkConstants.MediumWait);
        this.log.info("Successful click on: '" + element + "'");
    }


    public void beforeFindBy(By by, WebElement element, WebDriver driver) {
        this.waitForAjax(driver, FrameworkConstants.MediumWait);
        this.log.info("Attempting to locate: '" + by + "'");
    }

    public void afterFindBy(By by, WebElement element, WebDriver driver) {
        this.log.info("Located: '" + by + "'");
    }

    public void beforeScript(String script, WebDriver driver) {
        this.log.info("Attempting to execute script: '" + script + "' on driver '" + driver + "'");
    }


    public void afterScript(String script, WebDriver driver) {
        this.log.info("Successful script execution: '" + script + "' on driver '" + driver + "'");
    }

    public void hold() {
        try {
            Thread.sleep(FrameworkConstants.MaximumWait);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    private int getAjaxCount(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            String res = js.executeScript("return jQuery.active", new Object[0]).toString();
            this.log.info("." + res + ".");
            return Integer.parseInt(res);
        } catch (WebDriverException var4) {
            return js.executeScript("return document.readyState", new Object[0]).equals("complete") ? 0 : 1;
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            return js.executeScript("return document.readyState", new Object[0]).equals("complete") ? 0 : 1;
        }
    }

    void waitForAjax(WebDriver driver, int maxWaitInSecond) {
        long tStart = System.currentTimeMillis();
        long tElapsed;

        do {
            try {
                if (this.getAjaxCount(driver) == 0) {
                    break;
                }
                this.hold();
            } catch (WebDriverException var8) {
                do {
                    this.hold();
                    tElapsed = System.currentTimeMillis() - tStart;
                    tElapsed /= FrameworkConstants.MaximumWait;
                    this.log.warn("<<JS Error>> Waiting for max time: " + tElapsed + " seconds elapsed out of "
                            + maxWaitInSecond);
                } while (tElapsed <= (long) maxWaitInSecond);

                return;
            }

            tElapsed = System.currentTimeMillis() - tStart;
            tElapsed /= FrameworkConstants.MaximumWait;
            this.log.info("Waiting for ajax: " + tElapsed + " seconds elapsed out of " + maxWaitInSecond);
        } while (tElapsed <= (long) maxWaitInSecond);
    }


    public void onException(Throwable throwable, WebDriver driver) {
        ITestResult testResult = Reporter.getCurrentTestResult();
        String ex = throwable.getClass().toString().trim().substring(6);
        this.log.error(ex);

        try {
            if (PropertyReader.getFieldValue("ScreenshotEnable").equalsIgnoreCase("true")) {
                this.ss = new Screenshot(driver);

                try {
                    this.ss.setPathTakeScreenshot(testResult);
                } catch (IOException var6) {
                    this.log.error("Unable to take screenshot.");
                    var6.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void beforeGetText(WebElement element, WebDriver driver) {
        waitForAjax(driver, FrameworkConstants.LargeWait);
        log.info("Attempting to locate: '" + element + "'");
    }


    public void afterGetText(WebElement element, WebDriver driver, String text) {
        log.info("Successful get text on: '" + element + "'");
    }
}
