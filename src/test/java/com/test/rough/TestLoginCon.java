package com.test.rough;

public class TestLoginCon {


}

package actions.SeleniumJava;
        import java.io.*;
        import java.util.*;
        import org.sikuli.script.Pattern;
        import org.sikuli.script.Screen;
        import javax.net.ssl.SSLContext;
        import java.sql.Connection;
        import java.sql.DriverManager;
        import java.sql.ResultSet;
        import java.sql.ResultSetMetaData;
        import java.sql.Statement;
        import java.text.SimpleDateFormat;
        import java.util.Date;
        import java.util.Iterator;
        import java.util.List;
        import java.util.Random;
        import org.openqa.selenium.NoSuchElementException;
        import java.util.Set;
        import java.util.concurrent.TimeUnit;
        import org.apache.log4j.Logger;
        import org.openqa.selenium.By;
        import org.openqa.selenium.Dimension;
        import org.openqa.selenium.JavascriptExecutor;
        import org.openqa.selenium.Keys;
        import org.openqa.selenium.WebDriver;
        import org.openqa.selenium.WebElement;
        import org.openqa.selenium.interactions.Actions;
        import org.openqa.selenium.interactions.internal.Coordinates;
        import org.openqa.selenium.internal.Locatable;
        import org.openqa.selenium.support.ui.ExpectedConditions;
        import org.openqa.selenium.support.ui.WebDriverWait;
        import org.testng.Assert;
        import actions.selenium.Browser;
        import actions.SeleniumJava.MVELUtil;
        import actions.SeleniumJava.CommonMethods;
        import java.util.*;
        import java.util.Map.Entry;
        import java.util.HashMap;
        import actions.selenium.Browser;
        import actions.SeleniumJava.SFDC;
        import actions.SeleniumJava.DWS;
        import actions.SeleniumJava.SerCld;
        import java.sql.Connection;
        import java.sql.DriverManager;
        import java.sql.ResultSet;
        import java.sql.SQLException;
        import java.sql.Statement;
        import org.testng.Assert;
        import java.io.IOException;
        import org.apache.commons.codec.binary.Base64;
        import org.apache.http.HttpResponse;
        import org.apache.http.ParseException;
        import org.apache.http.client.ClientProtocolException;
        import org.apache.http.client.HttpClient;
        import org.apache.http.client.methods.HttpGet;
        import org.apache.http.client.methods.HttpPost;
        import org.apache.http.conn.scheme.Scheme;
        import org.apache.http.conn.scheme.SchemeRegistry;
        import org.apache.http.conn.ssl.SSLSocketFactory;
        import org.apache.http.impl.client.DefaultHttpClient;
        import org.apache.http.impl.conn.BasicClientConnectionManager;
        import org.apache.http.message.BasicHeader;
        import org.apache.http.util.EntityUtils;
        import java.util.ArrayList;
        import com.google.gson.JsonArray;
        import com.google.gson.JsonObject;
        import com.google.gson.JsonParser;
        import com.jayway.jsonpath.DocumentContext;
        import com.jayway.jsonpath.JsonPath;

class Connector{
    public String executeSfdcSoql(HashMap<String, Object> params) throws ClientProtocolException, IOException {

        String Query= (String)params.get("SOQL Query");
        System.out.println(Query);

        String USERNAME     = (String)params.get("USERNAME");
        String PASSWORD      =  (String)params.get("PASSWORD");
        String LOGINURL     = "https://test.salesforce.com";
        String GRANTSERVICE = "/services/oauth2/token?grant_type=password";
        String CLIENTID     =  (String)params.get("CLIENTID");
        String CLIENTSECRET = (String)params.get("CLIENTSECRET");
        String ACCESSTOKEN =null ;
        String INSTANCEURL= null ;
        String RefreshToken ="";
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost =null;

        // Assemble the login request URL
        String loginURL = LOGINURL +
                GRANTSERVICE +
                "&client_id=" + CLIENTID +
                "&client_secret=" + CLIENTSECRET +
                "&username=" + USERNAME +
                "&password=" + PASSWORD;

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

        // System.out.println(jsonObject.get("error").getAsString());
        ACCESSTOKEN = jsonObject.get("access_token").getAsString();
        INSTANCEURL = jsonObject.get("instance_url").getAsString();
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
    public String querySQL(HashMap<String, Object> params)  throws SQLException, Exception
    {
        WebDriver d= Browser.Driver;
        String Host    = (String) params.get("HOST");
        String Username   = (String) params.get("USERNAME");
        String Password    = (String) params.get("PASSWORD");
        String Query  = (String) params.get("QUERY");
        String ColumnName = (String) params.get("COLUMN NAME");
        String returnNumRecords = (String) params.get("EXPECTED RESULT");
        String ColumnData="";
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection con = DriverManager.getConnection(Host,Username,Password);
        Statement stmt = con.createStatement();
        System.out.println("Given Query : " + Query);
        ResultSet rs = stmt.executeQuery(Query);
        int no_of_rows = 0;
        while (rs.next())
        {
            ColumnData =rs.getString(ColumnName);
            no_of_rows+=1;
            System.out.println("No. of Rows in DB are :- " + no_of_rows);
            System.out.println(ColumnData);
        }
        con.close();
        System.out.println("No. of Rows : " + no_of_rows);
        return ColumnData;
    }
    public String querySQLServer(HashMap<String, Object> params)  throws SQLException, Exception
    {
        WebDriver d= Browser.Driver;

        String Host    = (String) params.get("HOST");
        String Username   = (String) params.get("USERNAME");
        String Password    = (String) params.get("PASSWORD");
        String Query  = (String) params.get("QUERY");
        String ColumnName = (String) params.get("COLUMN NAME");
        String returnNumRecords = (String) params.get("EXPECTED RESULT");
        String ColumnData="";
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        Connection con = DriverManager.getConnection(Host,Username,Password);
        Statement stmt = con.createStatement();
        System.out.println("Given Query : " + Query);
        ResultSet rs = stmt.executeQuery(Query);
        int no_of_rows = 0;
        while (rs.next())
        {
            ColumnData =rs.getString(ColumnName);
            no_of_rows+=1;
            System.out.println("No. of Rows in DB are :- " + no_of_rows);
            System.out.println(ColumnData);
        }
        con.close();
        System.out.println("No. of Rows : " + no_of_rows);
        return ColumnData;
    }
    public void SikuliFileUpload(HashMap<String, Object> params) throws Exception {
        WebDriver d = Browser.Driver;
        String ActualFiletobeUploaded = (String) params.get("File To be uploaded");
        String SikuliImgFilename= (String) params.get("Sikuli File Name Image");
        String SikuliImgFileOpen = (String) params.get("Sikuli Open button Image");
        Screen screen= new Screen();
        File fn = new File(SikuliImgFilename);
        File fo = new File(SikuliImgFileOpen);
        File fa = new File(ActualFiletobeUploaded);
        Pattern fileName = new Pattern(fn.getAbsolutePath()).similar((float) 0.7);
        Pattern open = new Pattern(fo.getAbsolutePath()).similar((float) 0.3);
        screen.type(fileName, fa.getAbsolutePath());
        screen.click(open);
        Thread.sleep(6000);
    }
    public String getFilePath(HashMap<String, Object> params){

        String filename = (String) params.get("File Name under Bin Directory");

        File fn = new File(filename);

        System.out.println("File Absolute path agent machine: " + fn.getAbsolutePath());
        return fn.getAbsolutePath();

    }
    public void multipleColumnDataValidation(HashMap<String, Object> params) throws SQLException, Exception {
        String host = (String) params.get("HOST");
        String username = (String) params.get("USERNAME");
        String password = (String) params.get("PASSWORD");
        String query = (String) params.get("QUERY");

        String columnNames = (String) params.get("COLUMN NAME:EXPECTED RESULT");

        String[] validationData = columnNames.split("\\|");
        Connection con = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection(host, username, password);
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println("Given Query : " + query);
            ResultSet rs = stmt.executeQuery(query);

            int no_of_rows = 0;
            try {
                rs.last();
                no_of_rows = rs.getRow();
                rs.beforeFirst();
            } catch (Exception ex) {
                throw new Exception("Somthing wnet wrong while retriving Row Count " + ex.getMessage());
            }

            if (no_of_rows != 1) {
                throw new Exception(
                        "Number of Rows is Greater than 1 or 0. Make sure used Query must return 1 Row Only.\n No of Rows return : "
                                + no_of_rows + " Query : " + query);
            }

            while (rs.next()) {
                for (int i = 0; i < validationData.length; i++) {
                    System.out.println("**Validation Line "+validationData[i]);
                    String[] data = validationData[i].split(":");
                    System.out.println("**Column Name : "+data[0]);
                    System.out.println("**Expected Data : "+data[1]);
                    String value = rs.getString(data[0]);
                    System.out.println("**DB Value : "+value);
                    Assert.assertEquals(value, data[1], "MISMATCH FOUND : Validation Column - " + data[0]
                            + ", Value From DB : " + value + ", Expected Value : " + data[1]);
                }
            }
        } catch (Exception ex) {
            throw new Exception("Somthing wnet wrong while testing data from DB " + ex.getMessage());
        } finally {
            if (con != null)
                con.close();
        }
    }
    public String getJsonNodeValue(HashMap<String, Object> params){

        String jsonString     = (String)params.get("Json Payload");
        String jsonPathExpression =  (String)params.get("Json Path Expression");

        DocumentContext context = JsonPath.parse(jsonString);
        String nodeValue = context.read(jsonPathExpression).toString().replace("[\"","").replace("\"]","");
        return nodeValue;
    }

}