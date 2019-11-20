package com.test.base;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;


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

  }

