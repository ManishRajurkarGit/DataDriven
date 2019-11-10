package com.test.testcases;

import com.test.base.TestBase;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class LoginTest extends TestBase{

    @Test (priority = 0)
    public void performSanity(){

        Assert.assertTrue(isPropertyKeyPresent(config.getProperty("browser")));
        log.info("Property key browser present ");

        Assert.assertTrue(isPropertyKeyPresent(OR.getProperty("homeBtn")));
        log.info("Property key homeBtn present ");
  /*
        driver.get(config.getProperty("testUrl"));
        log.info("URL opened"+ config.getProperty("testUrl"));
        driver.manage().window().maximize();*/
    }



    @Test (priority = 1)
    public void loginAsBankManager() throws InterruptedException {


        driver.get(config.getProperty("testUrl"));
        log.info("URL opened"+ config.getProperty("testUrl"));
        driver.manage().window().maximize();
        log.info("window maximized");
        driver.findElement(By.xpath(OR.getProperty("bankManagerLoginBtn"))).click();
        log.info("Clicked on the button ");
        Thread.sleep(4000);
        // driver.findElement(By.xpath(OR.getProperty("")))
        assertTrue(isElementPresent(By.cssSelector(OR.getProperty("addCustBtm"))));

    }
}
