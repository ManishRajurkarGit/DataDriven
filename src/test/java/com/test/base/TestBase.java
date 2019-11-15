package com.test.base;
import org.openqa.selenium.By;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.util.NoSuchElementException;


public class TestBase extends ConfigurationUtilities {

    /* INTIALISE
     * Webdriver
     * properities
     * Excelreadwrite
     * RepotNG Extentreport
     * DB
     * Mail
     * log  log4j.properties , Logger file from apache
     */

    @BeforeSuite
    public void setUp() {
        //executes before every suite and test cases
        intiateLogger();
        intiatePropertyfiles();
        intiateBrowserDriver();
    }

    @AfterSuite
    public void tearDown() {
        // after executing all the test cases.
        closeQuiteDriver();
    }

   public boolean isElementPresent (By by) {
        try {
            driver.findElement(by);
            return true;
        }catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isPropertyKeyPresent(String propKey) {
        try{
            config.getProperty(propKey);
            OR.getProperty(propKey);
            return true;
        } catch( NoSuchElementException e) {
            return false;
        }

    }
}

