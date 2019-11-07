package com.test.testcases;

import com.test.base.TestBase;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class LoginTest extends TestBase {
    @Test (priority = 1)
    public void loginAsBankManager() throws InterruptedException {
        driver.findElement(By.xpath(OR.getProperty("bankManagerLoginBtn"))).click();
        Thread.sleep(4000);
        // driver.findElement(By.xpath(OR.getProperty("")))
        assertTrue(isElementPresent(By.cssSelector(OR.getProperty("addCustBtm"))));
    }
}
