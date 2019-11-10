package com.test.base;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.apache.log4j.Logger;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Properties;


public class TestBase {

    /* INTIALISE
     * Webdriver
     * properities
     * Excelreadwrite
     * RepotNG Extentreport
     * DB
     * Mail
     * log  log4j.properties , Logger file from apache
     */

    public static WebDriver driver = null;  // reference which gets define runtime to get Chrome or Firefox based on cofig.
    public static Properties config = null ;
    public static Properties OR = null ;
    public static FileInputStream fis = null ;
    public static Logger log ;
    public static Date d = new Date();


    @BeforeSuite
    public void setUp() {
        //executes before every suite and test cases
            intiateLogger();
            intiatePropertyfiles();



           //Intialize the ChromeDriver instance with the reference of the webDriver object
            if(config.getProperty("browser").equalsIgnoreCase("chrome")) {
                System.setProperty("webdriver.chrome.driver", "F:\\Automation\\INTLJ\\DataDriven\\src\\test\\resources\\Executables\\chromedriver.exe");
                driver = new ChromeDriver();
                System.out.println("chromeDriver created");

            }    //Intialize the ChromeDriver instance with the reference of the webDriver object
            else if(config.getProperty("browser").equalsIgnoreCase("firefox")) {
                System.setProperty("webdriver.gecko.driver", "F:\\Automation\\INTLJ\\DataDriven\\src\\test\\resources\\Executables\\geckodriver.exe");
                driver = new FirefoxDriver();
            } else if (config.getProperty("browser").equalsIgnoreCase("ie")) {
                System.setProperty("webdriver.ie.driver", "F:\\Automation\\INTLJ\\DataDriven\\src\\test\\resources\\Executables\\IEDriverServer.exe");
                driver = new InternetExplorerDriver();
            } else {
                System.out.println("BrowserDriver not intiated");
            }
        }

public void intiateLogger() {

    log = Logger.getLogger(TestBase.class.getName());
    System.setProperty("Current.date", d.toString().replace(":", "_").replace(" ", "_"));
    try {
        PropertyConfigurator.configure("./src/test/resources/Logs/log4j.properties");
        System.out.println("Logger Intiatiated in the Base class and Property Configurate pointed to log4j.property file");
    }catch(Exception e){
        e.printStackTrace();
        System.out.println("log4j File is not availble on the said lovation");
    }

}

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
public void intiateBrowserDriver() {
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

    @AfterSuite
    public void tearDown() {
        // after executing all the test cases.

        System.out.println("In the method to close the browser");
        if (driver != null) {
            driver.quit();
            //driver.close();

            System.out.println("Browser Driver colosed ");
        }
    }
}
