package com.Cucumber.automation.framework.driver;

import com.Cucumber.automation.framework.constants.AppConstants;
import com.Cucumber.automation.framework.constants.FrameworkConstants;
import com.Cucumber.automation.framework.utils.Logging;
import com.Cucumber.automation.framework.utils.PropertyReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.grid.Main;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.WebDriverListener;
import org.testng.Reporter;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class GlobalDriver {

    private String browserName;
    private String _headless = null;
    private String executionServer = null;
    private WebDriver _ldriver = null;
    private Logging log = new Logging(GlobalDriver.class.getName());
    private String defaultDownloadPath = null;

    public GlobalDriver() {
        try {
            _headless = System.getProperty("headlessMode");
            if (_headless.equalsIgnoreCase("true"))
                log.info("Running tests in headless mode.");
        } catch (Exception ignore) {
        }
    }

    public String setDownloadPath() {
        this.defaultDownloadPath = System.getProperty("user.dir")
                + PropertyReader.getFieldValue("DefaultDownloadPath");

        return defaultDownloadPath;
    }


    public WebDriver init() {
        setDownloadPath();
        String browser = null;
        if (Reporter.getCurrentTestResult() != null) {

            browser = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("browser");

        }
        if (browser == null) {
            browserName = PropertyReader.getFieldValue("TestBrowser");
        } else {
            browserName = browser;
        }

        executionServer = PropertyReader.getFieldValue("ExecutionServer");

        ThreadContext.pop();
        ThreadContext.push(executionServer.toUpperCase());
        ThreadContext.push(browserName.toUpperCase());

        if (browserName.equalsIgnoreCase("chrome")) {
//            WebDriverManager.chromedriver().clearResolutionCache().setup();
            System.setProperty("webdriver.chrome.driver", "src/main/resources/drivers/chromedriver.exe");

            if (executionServer.equalsIgnoreCase("remote")) {
                Main.main(new String[]{"standalone", "--port", AppConstants.Web.GRIP_HUB_PORT});
                _ldriver = WebDriverManager.chromedriver()
                        .capabilities(setChromeOptions())
                        .remoteAddress(AppConstants.Web.GRID_HUB_URL)
                        .create();
            } else {
                _ldriver = new ChromeDriver(setChromeOptions());
            }
        }
        else if (browserName.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().clearResolutionCache().setup();

            if (executionServer.equalsIgnoreCase("remote")) {
                Main.main(new String[]{"standalone", "--port", AppConstants.Web.GRIP_HUB_PORT});
                _ldriver = WebDriverManager.firefoxdriver()
                        .capabilities(setFirefoxOptions())
                        .remoteAddress(AppConstants.Web.GRID_HUB_URL)
                        .create();
            } else {
                _ldriver = new FirefoxDriver(setFirefoxOptions());
            }
        }
        else if (browserName.equalsIgnoreCase("edge")) {
            WebDriverManager.edgedriver().clearResolutionCache().setup();

            if (executionServer.equalsIgnoreCase("remote")) {
                Main.main(new String[]{"standalone", "--port", AppConstants.Web.GRIP_HUB_PORT});
                _ldriver = WebDriverManager.edgedriver()
                        .capabilities(setEdgeOptions())
                        .remoteAddress(AppConstants.Web.GRID_HUB_URL)
                        .create();
            } else {
                _ldriver = new EdgeDriver(setEdgeOptions());
            }
        }

        WebDriverListener listener = new WebDriverListener() {
            @Override
            public void beforeClick(WebElement element) {
                WebDriverListener.super.beforeClick(element);
            }
        };
        WebDriver driver = new EventFiringDecorator(listener).decorate(_ldriver);


        log.info("New driver instantiated.");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(FrameworkConstants.SmallWait));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(FrameworkConstants.LargeWait));
        driver.manage().window().maximize();

        return driver;
    }

    private ChromeOptions setChromeOptions() {
        ChromeOptions options = new ChromeOptions();

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", defaultDownloadPath);  //adding download folder preference
        prefs.put("download.prompt_for_download", "false");  //preferences for download notification
        prefs.put("profile.default_content_settings.popups", 0);  //preferences for pop-ups
        prefs.put("settings.language.preferred_languages", "en");  //language preferences

        options.setExperimentalOption("prefs", prefs);
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        options.addArguments("--disable-notifications");
        options.addArguments("--test-type");
        options.addArguments("ignore-certificate-errors");
        options.addArguments("--disable-extensions");
        options.addArguments("start-maximized");
        options.addArguments("--use-fake-ui-for-media-stream=1");
//        if (_headless.equalsIgnoreCase("true"))
//            options.addArguments("--headless");

        return options;
    }

    private FirefoxOptions setFirefoxOptions() {
        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("browser.download.folderList", 2);
        profile.setPreference("browser.download.dir", defaultDownloadPath);
        profile.setPreference("browser.download.manager.showWhenStarting", false);
        profile.setPreference("browser.popups.showPopupBlocker", false);
        profile.setPreference("privacy.popups.showBrowserMessage", false);
        profile.setPreference("browser.download.manager.closeWhenDone", true);
        profile.setPreference("browser.zoom.full", true);
        profile.setPreference("javascript.enabled", true);
//    profile.setPreference("browser.download.manager.showAlertOnComplete", false);
//    profile.setPreference("browser.helperApps.neverAsk.saveToDisk",
//      "image/jpeg;application/vnd.ms-excel;image/png;application/pdf;application/msword;application/zip;text/csv");

        FirefoxOptions options = new FirefoxOptions();
        options.setProfile(profile);
        options.setAcceptInsecureCerts(true);
//        if (_headless.equalsIgnoreCase("true"))
//            options.setHeadless(true);

        return options;
    }

    private EdgeOptions setEdgeOptions() {
        EdgeOptions options = new EdgeOptions();

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", defaultDownloadPath);  //adding download folder preference
        prefs.put("download.prompt_for_download", "false");  //preferences for download notification
        prefs.put("profile.default_content_settings.popups", 0);  //preferences for pop-ups
        prefs.put("settings.language.preferred_languages", "en");  //language preferences

        options.setExperimentalOption("prefs", prefs);
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        options.addArguments("--disable-notifications");
        options.addArguments("--test-type");
        options.addArguments("ignore-certificate-errors");
        options.addArguments("--disable-extensions");
        options.addArguments("start-maximized");
        options.addArguments("--use-fake-ui-for-media-stream=1");
//        if (_headless.equalsIgnoreCase("true"))
//            options.addArguments("--headless");

        return options;
    }



}
