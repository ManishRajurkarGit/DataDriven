package com.test.base;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import java.io.FileInputStream;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Properties;

public class ConfigurationUtilities {

    public static Logger log;
    public static Properties config = null;
    public static Properties OR = null;
    public static FileInputStream fis = null;
    public static Date d = new Date();
    public static WebDriver driver = null;  // reference which gets define runtime to get Chrome or Firefox based on cofig.

    public void intiateLogger() {
        log = Logger.getLogger(TestBase.class.getName());
        System.setProperty("current.date", d.toString().replace(":", "_").replace(" ", "_"));
        try {
            PropertyConfigurator.configure("./src/test/resources/Logs/log4j.properties");
            System.out.println("Logger Intiatiated in the Base class and Property Configurate pointed to log4j.property file");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("log4j File is not availble on the said lovation");
        }

    }

    /////
    public void intiatePropertyfiles() {
        if (driver == null) {   //when driver is null first load property file
            config = new Properties();
            OR = new Properties();
            try {
                fis = new FileInputStream(System.getProperty("user.dir") + "\\src\\test\\resources\\Properties\\Config.properties");
                config.load(fis);

                fis = new FileInputStream(System.getProperty("user.dir") + "\\src\\test\\resources\\Properties\\OR.properties");
                OR.load(fis);
                System.out.println("File Input Stream created");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    ////
    public static void intiateBrowserDriver() {
        //Intialize the ChromeDriver instance with the reference of the webDriver object
        if (config.getProperty("intiateDriver").equalsIgnoreCase("YES")) {
            if (config.getProperty("browser").equalsIgnoreCase("chrome")) {
                System.setProperty("webdriver.chrome.driver", "F:\\Automation\\INTLJ\\DataDriven\\src\\test\\resources\\Executables\\chromedriver.exe");
                driver = new ChromeDriver();
                System.out.println("chromeDriver created");

            }    //Intialize the ChromeDriver instance with the reference of the webDriver object
            else if (config.getProperty("browser").equalsIgnoreCase("firefox")) {
                System.setProperty("webdriver.gecko.driver", "F:\\Automation\\INTLJ\\DataDriven\\src\\test\\resources\\Executables\\geckodriver.exe");
                driver = new FirefoxDriver();
            } else if (config.getProperty("browser").equalsIgnoreCase("ie")) {
                System.setProperty("webdriver.ie.driver", "F:\\Automation\\INTLJ\\DataDriven\\src\\test\\resources\\Executables\\IEDriverServer.exe");
                driver = new InternetExplorerDriver();
            } else {
                System.out.println("BrowserDriver not intiated");
            }
        }

    }


    /*Close() - It is used to close the browser or page currently which is having the focus.
       Quit() - It is used to shut down the web driver instance or destroy the web driver
    */
    public static void closeQuiteDriver () {
        System.out.println("Inside close and quit browser");
        if (driver != null) {
            driver.close();
            driver.quit();
            System.out.println("Browser Driver colosed and quite ");
        }
    }

    public void readExcel (){


    }
    public void writeExcel (){
    /*-------------------------------------------------------------
Function Name: 
Created By:
Purpose: Switching to Classic if user is in Lightning page
Input Parameters:
Return Value:
Modified by: XX
--------------------------------------------------------------*/


    }

    public static boolean isElementPresent (By by) {
        try {
            driver.findElement(by);
            return true;
        }catch (NoSuchElementException e) {
            return false;
        }
    }

    public static boolean isPropertyKeyPresent(String propKey) {
        try{
            config.getProperty(propKey);
            OR.getProperty(propKey);
            return true;
        } catch( NoSuchElementException e) {

            return false;
        }

    }

    
}
