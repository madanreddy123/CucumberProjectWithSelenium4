package com.Cucumber.automation.test.pageFactory;

import com.Cucumber.automation.framework.constants.FrameworkConstants;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class TestPage extends  PageClass{

    WebDriver driver;
    WebDriverWait wait;
    public TestPage(WebDriver driver) {
        super(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(FrameworkConstants.MediumWait));

    }
}
