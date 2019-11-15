package com.test.base;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.Assert;

import java.io.FileInputStream;
import java.sql.*;
import java.util.Date;
import java.util.HashMap;
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


    }
//jdbc:oracle:thin:@wspdev.bmc.com:1521
    /*-------------------------------------------------------------
Function Name: switchToClassic
Created By:
Purpose: Switching to Classic if user is in Lightning page
Input Parameters:
Return Value:
Modified by: XX
--------------------------------------------------------------*/

    public static String executeSQL(HashMap<String, Object> params) throws SQLException, Exception {
        String Host = (String) params.get("HOST");
        String Username = (String) params.get("USERNAME");
        String Password = (String) params.get("PASSWORD");
        String Query = (String) params.get("QUERY");
        String ColumnName = (String) params.get("COLUMN NAME");
        String returnNumRecords = (String) params.get("EXPECTED RESULT");
        String ColumnData = null;
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection con = null;
        try {
            con = DriverManager.getConnection(Host, Username, Password);
            Statement stmt = con.createStatement();
            System.out.println("Given Query : " + Query);
            ResultSet rs = stmt.executeQuery(Query);
            int no_of_rows = 0;
            if (!rs.equals(null)) {
                while (rs.next()) {
                    ColumnData = rs.getString(ColumnName);
                    no_of_rows++;
                    System.out.println("Column Name is :" +  ColumnName);
                    System.out.println("Column Name Data is :" +  ColumnData);
                }
                System.out.println("No. of Rows in DB are : " + no_of_rows);
            }
            if (no_of_rows > 1) {
                System.out.println("As Multiple rows are found data is compared with last row");
            }else if (no_of_rows ==0){
                throw new Exception("No Records found , Check query by manually runing in the db : " + Query);
            }
        } catch (Exception ex) {
            throw new Exception("ERROR : " + ex.getMessage());
        } finally {
            if(!(con==null))
                con.close();
        }
        Assert.assertEquals(ColumnData, returnNumRecords, "Data is NOT Matching");
        return ColumnData;
    }

    public String querySQLServer(HashMap<String, Object> params)  throws SQLException, Exception {
        String Host = (String) params.get("HOST");
        String Username = (String) params.get("USERNAME");
        String Password = (String) params.get("PASSWORD");
        String Query = (String) params.get("QUERY");
        String ColumnName = (String) params.get("COLUMN NAME");
        String returnNumRecords = (String) params.get("EXPECTED RESULT");
        String ColumnData = "";
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        Connection con = DriverManager.getConnection(Host, Username, Password);
        Statement stmt = con.createStatement();
        System.out.println("Given Query : " + Query);
        ResultSet rs = stmt.executeQuery(Query);
        int no_of_rows = 0;
        while (rs.next()) {
            ColumnData = rs.getString(ColumnName);
            no_of_rows += 1;
            System.out.println("No. of Rows in DB are :- " + no_of_rows);
            System.out.println(ColumnData);
        }
        con.close();
        System.out.println("No. of Rows : " + no_of_rows);
        return ColumnData;
    }
}