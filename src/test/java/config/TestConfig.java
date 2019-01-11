package config;

import io.restassured.RestAssured;
import io.restassured.specification.*;
import io.restassured.builder.*;
import org.junit.BeforeClass;

public class TestConfig {

   public static RequestSpecification videoGame_requestSpec;
   public static RequestSpecification football_requestSpec;
   public static  ResponseSpecification response_Spec;
    @BeforeClass
    public static void setup(){

        // Fiddler
        RestAssured.proxy("localhost", 8888);

         videoGame_requestSpec = new RequestSpecBuilder().
                setBaseUri("http://localhost").
                setPort(8080).
                setBasePath("/app/").
                addHeader("Content-Type", "application/xml").
                addHeader("Accept", "application/xml").
                build();

        RestAssured.requestSpecification = videoGame_requestSpec;
         //http://localhost:8080/app/videogames
       football_requestSpec = new RequestSpecBuilder().
               setBaseUri("http://api.football-data.org").
               setBasePath("/v1/").
               addHeader("X-Auth-Token", "3681e1112a4741ab901cea8d608669e6").
               //addHeader("X-Requested-Control", "minified").
               build();
           // RestAssured.requestSpecification = football_requestSpec;

       response_Spec = new ResponseSpecBuilder().
                expectStatusCode(200).build();
       RestAssured.responseSpecification = response_Spec;
    }
}
