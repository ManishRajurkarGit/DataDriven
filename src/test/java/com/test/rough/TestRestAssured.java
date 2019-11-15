package com.test.rough;
import com.test.base.TestBase;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

public class TestRestAssured extends TestBase {

    /*
    * Checking the status code
    * */

    @Test (priority = 1)
    public static void testStatusCode() {
        given().
                get("http://jsonplaceholder.typicode.com/posts/3").
                then().
                statusCode(200).log().all();
        log.info("Success");
    }

/*  // @Test(priority = 2)
    public void testLogging(){
        given().get("http://services.groupkt.com/country/get/iso2code/in").then().statusCode(200).log().all();
   }*/

  @Test (priority = 2)
   public static void testResponse() {
       Response res =  given().
                get("http://jsonplaceholder.typicode.com/users/1").
        then().
                body("name", is("Leanne Graham")).
                body("email", is("Sincere@april.biz")).extract().response();
                log.info("Success");

      System.out.println("The Response is : " +res.asString());

   /*   JsonParser parser = new JsonParser();
      JsonObject jsonObject = (JsonObject) parser.parse(res.asString()); //new JsonObject(res.asString());
      System.out.println("The Second Response is---> : " +jsonObject);*/

    }
 }