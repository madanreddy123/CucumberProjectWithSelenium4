package com.Cucumber.automation.framework.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Reporter;

public class Logging {
    private Logger log;

    public Logging(String logName) {
        this.log = LogManager.getLogger(logName);
    }

    public void startTestCase(String sTestCaseName){
        this.log.info("$$$$$$$$$$$$$$$$$$$$$$$        " + sTestCaseName + "        $$$$$$$$$$$$$$$$$$$$$$$");
        Reporter.log("|  TEST STARTED  |");
    }

    public void endTestCase(String sTestCaseName){
        this.log.info("$$$$$$$$$$$$$$$$$$$$$    END OF " + sTestCaseName + "        $$$$$$$$$$$$$$$$$$$$$$");
        Reporter.log("|  TEST ENDED  |");
    }

    public void info(String message) {
        this.log.info(message);
        Reporter.log("| " + message + "| ");
    }

    public void warn(String message) {
        this.log.warn(message);
    }

    public void error(String message) {
        this.log.error(message);
    }

    public void fatal(String message) {
        this.log.fatal(message);
    }

    public void debug(String message) {
        this.log.debug(message);
    }

    public void trace(String message) {
        this.log.trace(message);
    }

    public Logger getLog() {
        return this.log;
    }
}