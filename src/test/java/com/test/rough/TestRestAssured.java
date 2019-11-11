package com.test.rough;
import com.test.base.TestBase;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;

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
        given().
                get("http://jsonplaceholder.typicode.com/users").

                        then().body("id".equals("1")).log().all();
                //statusCode(200).log().all();
        log.info("Success");

}
 }