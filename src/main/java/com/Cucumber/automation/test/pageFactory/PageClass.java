package com.Cucumber.automation.test.pageFactory;

import com.Cucumber.automation.framework.utils.WebUtils;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class PageClass extends WebUtils {

    public PageClass(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        ThreadContext.pop();
        ThreadContext.push(this.getClass().getSimpleName());
    }
}
