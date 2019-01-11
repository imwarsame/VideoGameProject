import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class AuthenticationTests {

    @BeforeClass
    static public void setUp(){
        RestAssured.proxy("localhost" , 8888);
    }

    /**
     * Below test will send basic authentication every time it makes a request
     */

    @Test
    public void basicPremptiveAuthTest(){
            given().
                    auth().preemptive().basic("username", "password").
            when().
                    get("http://localhost:8080/Endpoint").
            then();
    }

    /**
     * Below test will send basic authentication after the first request
     */


    @Test
    public void basicChallengedAuth(){
        given().
                auth().basic("username", "password").
        when().
                get("http://localhost:8080/Endpoint").
        then();
    }

    @Test
    public void oauth1Test(){
        given().
                auth().
                oauth("consumerKey", "consumerSecret", "accessToken", " secretToken").
        when().
                get("http://localhost:8080/Endpoint");
    }

    @Test
    public void oauth2Test(){
        given().
                auth().oauth2("accessToken").
        when().
                get("http://localhost:8080/Endpoint");
    }

    @Test
    public void relaxedHttpValidation(){
        given().
                relaxedHTTPSValidation().
        when().
                get("http://localhost:8080/Endpoint");
    }

    @Test
    public void keyStoreTest(){
        given().
                keyStore("/pathtoJKS", "password").
        when().
                get("http://localhost:8080/Endpoint");
    }
}
