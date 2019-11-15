package com.test.rough;

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

import java.io.IOException;
import java.util.HashMap;
public class jsoncon {

//https://engineering.gosquared.com/salesforce-rest-api-integration
// https://developer.salesforce.com/docs/atlas.en-us.api_rest.meta/api_rest/intro_understanding_username_password_oauth_flow.htm
//https://jsoneditoronline.org/
//https://jsonpath.curiousconcept.com

 public static  HashMap<String, Object> params = new HashMap<String, Object>() ;

    public static void main(String[] args) throws Exception {
        params.put("USERNAME", "manishrajurkar87@gmail.com");
        params.put("PASSWORD","Magic@123BYjeCYrs7zC4dHmuJQMqXujmP");
        params.put("CLIENTID","3MVG9d8..z.hDcPIOtFMLGaOQn_8JxZ6mzOxU.6vF3AmLcrzcf1w4br_xDDuBzqDQoJU8C7lUYJfJSE0oGNh4");
        params.put("CLIENTSECRET","B204ECC8C399FE85E75E94FC12A4E3AB7748007C5E489A9692BE03F4165B8C63");
        params.put("SOQL Query","select id from contact limit 1");
        jsoncon ts1 = new jsoncon();

        ts1.executeSoql(params);
        //params.put("Json Path Expression" ,"$.address.street");
        params.put("Json Path Expression" ,"$.records[0].Id");

        String response_node  =ts1.getJsonNodeValue(params);
        System.out.println("RESPONSE NODE IS :" +response_node);
    }

   public static String executeSoql(HashMap<String, Object> params) throws ClientProtocolException, IOException {

        String Query= (String)params.get("SOQL Query");
        System.out.println(Query);

        String USERNAME     = (String)params.get("USERNAME");
        String PASSWORD     =  (String)params.get("PASSWORD");
        String LOGINURL     = "https://login.salesforce.com";
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
       System.out.println("ACESSTOKEN IS : -" +ACCESSTOKEN);

        INSTANCEURL = jsonObject.get("instance_url").getAsString();
       System.out.println("ACESSTOKEN IS : -" +INSTANCEURL);
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
