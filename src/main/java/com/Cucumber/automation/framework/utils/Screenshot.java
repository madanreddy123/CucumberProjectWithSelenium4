package com.Cucumber.automation.framework.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import ru.yandex.qatools.allure.annotations.Attachment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Screenshot {
    WebDriver _driver;
    protected Logging log = new Logging(this.getClass().getName());

    public Screenshot(WebDriver driver) {
        this.setDriver(driver);
    }

    public void setDriver(WebDriver driver) {
        this._driver = driver;
    }

    public void setPathTakeScreenshot(ITestResult result) throws IOException {
        String caller = Thread.currentThread().getStackTrace()[2].getMethodName();
        String separator = File.separator;
        String ssDir = "Screenshot";
        String workingDirectory = System.getProperty("user.dir");

        SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss");
        Date now = new Date();
        String strDate = sdfDate.format(now);

        XmlSuite suite = result.getMethod().getTestClass().getXmlTest().getSuite();
        String suiteName = suite.getName();

        XmlTest xmlTest = result.getMethod().getTestClass().getXmlTest();
        String method = result.getName().trim();

        String imgName = "";
        String screenshotPathFFTest2;

        if (caller.contains("onException")) {
            screenshotPathFFTest2 = method + "_" + strDate + "_" + "Exception" + ".png";
            imgName = screenshotPathFFTest2;
        } else if (caller.contains("onTestFailure")) {
            screenshotPathFFTest2 = method + "_" + strDate + "_" + "Failure" + ".png";
            imgName = screenshotPathFFTest2;
        }

        screenshotPathFFTest2 = workingDirectory + separator + ssDir + separator + "FF" + separator + "Test1" + separator + suiteName + separator + xmlTest.getName() + separator + imgName;
        String screenshotPathFFQA = workingDirectory + separator + ssDir + separator + "FF" + separator + "QA" + separator + suiteName + separator + xmlTest.getName() + separator + imgName;

        String screenshotPathChromeTest2 = workingDirectory + separator + ssDir + separator + "Chrome" + separator + "Test1" + separator + suiteName + separator + xmlTest.getName() + separator + imgName;
        String screenshotPathChromeQA = workingDirectory + separator + ssDir + separator + "Chrome" + separator + "QA" + separator + suiteName + separator + xmlTest.getName() + separator + imgName;

        String screenshotPathEdgeTest2 = workingDirectory + separator + ssDir + separator + "Edge" + separator + "Test1" + separator + suiteName + separator + xmlTest.getName() + separator + imgName;
        String screenshotPathEdgeQA = workingDirectory + separator + ssDir + separator + "Edge" + separator + "QA" + separator + suiteName + separator + xmlTest.getName() + separator + imgName;

        String screenshotPathIETest2 = workingDirectory + separator + ssDir + separator + "IE" + separator + "Test1" + separator + suiteName + separator + xmlTest.getName() + separator + imgName;
        String screenshotPathIEQA = workingDirectory + separator + ssDir + separator + "IE" + separator + "QA" + separator + suiteName + separator + xmlTest.getName() + separator + imgName;

        String xmlTestResult = xmlTest.getAllParameters().toString();
        String url = this._driver.getCurrentUrl();
        String gridParam = suite.getParameter("Sel_Grid");
        String testBrowser = PropertyReader.getFieldValue("TestBrowser");
        String defaultEnv = PropertyReader.getFieldValue("DefaultEnvironment");

        if (gridParam != null) {
            if (StringUtils.equalsIgnoreCase(xmlTestResult, "chrome")) {
                if (StringUtils.equalsIgnoreCase(url, "qa")) {
                    this.takeScreenshot(screenshotPathChromeQA);
                }
                if (StringUtils.equalsIgnoreCase(url, "Test1")) {
                    this.takeScreenshot(screenshotPathChromeTest2);
                }
            }

            if (StringUtils.equalsIgnoreCase(xmlTestResult, "firefox")) {
                if (StringUtils.equalsIgnoreCase(url, "qa")) {
                    this.takeScreenshot(screenshotPathFFQA);
                }
                if (StringUtils.equalsIgnoreCase(url, "Test1")) {
                    this.takeScreenshot(screenshotPathFFTest2);
                }
            }

            if (StringUtils.equalsIgnoreCase(xmlTestResult, "edge")) {
                if (StringUtils.equalsIgnoreCase(url, "qa")) {
                    this.takeScreenshot(screenshotPathEdgeQA);
                }
                if (StringUtils.equalsIgnoreCase(url, "Test1")) {
                    this.takeScreenshot(screenshotPathEdgeTest2);
                }
            }

            if (StringUtils.equalsIgnoreCase(xmlTestResult, "ie")) {
                if (StringUtils.equalsIgnoreCase(url, "qa")) {
                    this.takeScreenshot(screenshotPathIEQA);
                }
                if (StringUtils.equalsIgnoreCase(url, "Test1")) {
                    this.takeScreenshot(screenshotPathIETest2);
                }
            }

        } else {
            if (testBrowser.contains("chrome")) {
                if (StringUtils.containsIgnoreCase(defaultEnv, "qa")) {
                    this.takeScreenshot(screenshotPathChromeQA);
                } else if (StringUtils.containsIgnoreCase(defaultEnv, "Test1")) {
                    this.takeScreenshot(screenshotPathChromeTest2);
                }
            }

            if (testBrowser.contains("firefox")) {
                if (StringUtils.containsIgnoreCase(defaultEnv, "qa")) {
                    this.takeScreenshot(screenshotPathFFQA);
                } else if (StringUtils.containsIgnoreCase(defaultEnv, "Test1")) {
                    this.takeScreenshot(screenshotPathFFTest2);
                }
            }

            if (testBrowser.contains("edge")) {
                if (StringUtils.containsIgnoreCase(defaultEnv, "qa")) {
                    this.takeScreenshot(screenshotPathEdgeQA);
                } else if (StringUtils.containsIgnoreCase(defaultEnv, "Test1")) {
                    this.takeScreenshot(screenshotPathEdgeTest2);
                }
            }

            if (testBrowser.contains("ie")) {
                if (StringUtils.containsIgnoreCase(defaultEnv, "qa")) {
                    this.takeScreenshot(screenshotPathIEQA);
                } else if (StringUtils.containsIgnoreCase(defaultEnv, "Test1")) {
                    this.takeScreenshot(screenshotPathIETest2);
                }
            }

        }
    }
    @Attachment(value = "Screenshot", type = "image/png")
    public byte[] takeScreenshot(String destPath) throws IOException {
        this.log.info("Trying to take screenshot...");
        File scrFile = ((TakesScreenshot) this._driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrFile, new File(destPath));
        this.log.info("Screenshot taken.");

        return ((TakesScreenshot) this._driver).getScreenshotAs(OutputType.BYTES);
    }
}
