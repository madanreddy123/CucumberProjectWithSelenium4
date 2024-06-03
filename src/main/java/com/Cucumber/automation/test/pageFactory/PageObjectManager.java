package com.Cucumber.automation.test.pageFactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class PageObjectManager {
    WebDriver gDriver;
    PageClass pageClass;
    TestPage testPage;

    public PageObjectManager(WebDriver driver) {
        this.gDriver = driver;
        PageFactory.initElements(driver, this);
    }

    public PageClass getCommonPage() {
        return(pageClass == null) ? pageClass = new PageClass(gDriver) : pageClass;
    }

    public TestPage getTestPage() {
        return (testPage == null) ? testPage = new TestPage(gDriver) : testPage;
    }
}
