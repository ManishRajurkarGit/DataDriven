package com.test.base;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Properties;

public class ConfigurationUtilities {

    public static Logger log;
    public static Properties config = null;
    public static Properties OR = null;
    public static FileInputStream fis = null;
    public static Date d = new Date();
    public static WebDriver driver = null;  // reference which gets define runtime to get Chrome or Firefox based on cofig.
    public static  HashMap<String, Object> params = new HashMap<String, Object>() ;

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

    public static String executeSoql(HashMap<String, Object> params) throws ClientProtocolException, IOException {
      // Query to be executed on when the successfull connection is established
        String Query= (String)params.get("SOQL Query");
        System.out.println(Query);

      // Parameters which are required to make the secured connection
        String USERNAME     = (String)params.get("USERNAME");
        String PASSWORD     =  (String)params.get("PASSWORD");
        String LOGINURL     = "https://login.salesforce.com";
        String GRANTSERVICE = "/services/oauth2/token?grant_type=password";
        String CLIENTID     =  (String)params.get("CLIENTID");
        String CLIENTSECRET = (String)params.get("CLIENTSECRET");
        String ACCESSTOKEN =null ;
        String INSTANCEURL= null ;
        String RefreshToken ="";

      // An HttpClient can be used to send requests and retrieve their responses
        HttpClient httpclient = new DefaultHttpClient();
        //HttpPost httpPost =null;

        // Assemble the login request URL
        String loginURL = LOGINURL + GRANTSERVICE + "&client_id=" + CLIENTID +
                "&client_secret=" + CLIENTSECRET +  "&username=" + USERNAME +"&password=" + PASSWORD;

        // Login requests must be POSTs
        // System.out.println(loginURL);
        HttpClient client = new DefaultHttpClient();
        HttpPost request =new HttpPost(loginURL);
        HttpResponse response = client.execute(request);

        // verify response is HTTP OK
        String getResult = EntityUtils.toString(response.getEntity());
        System.out.println(getResult);

        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(getResult).getAsJsonObject();
        System.out.println(jsonObject);

        // System.out.println(jsonObject.get("error").getAsString());
        ACCESSTOKEN = jsonObject.get("access_token").getAsString();
        System.out.println("ACESSTOKEN IS : -" +ACCESSTOKEN);

        INSTANCEURL = jsonObject.get("instance_url").getAsString();
        System.out.println("Instance URL IS : -" +INSTANCEURL);
        RefreshToken = jsonObject.get("id").getAsString();

        // above code is to retrieve auth token

        Query=Query.replace(" ", "+");
        BasicHeader oauthHeader = new BasicHeader("Authorization", "OAuth " + ACCESSTOKEN);
        HttpGet httpGet = new HttpGet(INSTANCEURL+"/services/data/v39.0/query/?q="+Query);
        httpGet.addHeader(oauthHeader);
        httpGet.addHeader("Content-Type", "application/json");
        response = httpclient.execute(httpGet);
        String response_string = EntityUtils.toString(response.getEntity());

        //  JsonObject json = parser.parse(response_string).getAsJsonObject();

        System.out.println(response_string);
        request.releaseConnection();
        httpGet.releaseConnection();
        return response_string;
    }
    public String parseJsonString_returnfieldvalue(HashMap<String, Object> params){

        String jsonString     = (String)params.get("Json Payload");
        String fieldName      =  (String)params.get("Field Name");

        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(jsonString).getAsJsonObject();

        String fieldvalue= jsonObject.get(fieldName).getAsString();
        System.out.println("FieldName : " + fieldName + " Field Value : " + fieldvalue);
        return fieldvalue;
    }

    public String getJsonNodeValue(HashMap<String, Object> params){

        String jsonString     = (String)params.get("Json Payload");
        String jsonPathExpression =  (String)params.get("Json Path Expression");

        DocumentContext context = JsonPath.parse(jsonString);
        String nodeValue = context.read(jsonPathExpression).toString().replace("[\"","").replace("\"]","");
        return nodeValue;
    }

    
}
