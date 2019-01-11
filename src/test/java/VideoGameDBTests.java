import config.EndPoint;
import config.TestConfig;
import io.restassured.http.ContentType;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.codehaus.groovy.runtime.powerassert.SourceText;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static  io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.lessThan;

public class VideoGameDBTests extends TestConfig {

    @Test
    public void getAllGames(){
        given().when().get(EndPoint.VIDEOGAMES).then();
    }

    @Test
    public void postMyGame(){
        String gameBodyJson = "{\n" +
                "  \"id\": 66,\n" +
                "  \"name\": \"myGame\",\n" +
                "  \"releaseDate\": \"2019-01-04T17:04:17.546Z\",\n" +
                "  \"reviewScore\": 0,\n" +
                "  \"category\": \"Driving\",\n" +
                "  \"rating\": \"20\"\n" +
                "}";
        given().body(gameBodyJson).when().post(EndPoint.VIDEOGAMES).then();
    }

    @Test
    public void updateGame(){
        String gameBodyJson = "{\n" +
                "  \"id\": 66,\n" +
                "  \"name\": \"myGame\",\n" +
                "  \"releaseDate\": \"2019-01-04T17:04:17.546Z\",\n" +
                "  \"reviewScore\": 0,\n" +
                "  \"category\": \"Driving\",\n" +
                "  \"rating\": \"99\"\n" +
                "}";
        given().body(gameBodyJson).when().put("videogames/66").then();
    }



    @Test

    public void deleteGame(){
        when().delete("videogames/4");
        //how does this work without specifying the EndPoint
    }

    @Test
    //path parameter
    public void getSingleGame(){
        Response response = given().pathParam("videoGameId", 5).
                when().get(EndPoint.SINGLE_VIDEOGAME);
        String responseAsString = response.asString();
        System.out.println(responseAsString);
    }

    @Test
    public void getResponseAsString(){
        String responseBody = given().pathParam("videoGameId", 1).spec(videoGame_requestSpec).when().get(EndPoint.SINGLE_VIDEOGAME).asString();
        System.out.println(responseBody);
    }


    /**
     * Extracting response body
     */

    @Test
    public void getAllVideoGameData_DoCheckFirst(){
        Response response =
                given().
                        spec(videoGame_requestSpec).
                when().
                        get(EndPoint.VIDEOGAMES).
                then().
                       contentType(ContentType.JSON). //makes sure we're getting response in Json format
                       extract().response();  //use extract if you want to use the response elsewhere
        String jsonResponseAsString = response.asString();
        System.out.println(jsonResponseAsString);
    }

    /**
     *Extracting response header
     */

    @Test
    public void extractHeaders(){
        Response response =
                given().
                        pathParam("videoGameId", 2).
                        spec(videoGame_requestSpec).
                when().
                        get(EndPoint.SINGLE_VIDEOGAME).
                then().
                        contentType(ContentType.JSON). //can leave this out since we've specified it in TestConfig class
                        extract().response();
        Headers headers = response.getHeaders();

        //Get the content type
        String contentType = response.getHeader("Content-Type");
        System.out.println("The content type is: " + contentType);

        //Get All Cookies
        Map<String, String> allCookies = response.getCookies();

        /**
         * will return an empty array bc no cookies exists
         * check by including log().all() in given()
         */

        System.out.println("Number of Cookies: " + allCookies);

        //Get status code
        int statusCode = response.getStatusCode();
        System.out.println("Status code is: " + statusCode);

        //Get status line
        String statusLine = response.getStatusLine();
        System.out.println("Status line: " + statusLine);

    }


    /**
     * Extracting specific data from certain parts of the response
     */

    @Test
    public void extractFirstVideoGameName() {
        /**
         * Here we are requesting *all* the video games (which are stored in an array format)
         * and returning the name of the first game in the array
         * remember arrays start at 0
         */

        String firstVideoGameName = when().get(EndPoint.VIDEOGAMES).jsonPath().getString("name[0]");
        System.out.println(firstVideoGameName);
    }

    @Test
    public void extractingAllVideoGameNames(){
        /**
         * Here we extract the response and store in a variable called 'response'
         */

        Response response =
                given().
                            spec(videoGame_requestSpec).
                        when().
                            get(EndPoint.VIDEOGAMES).
                        then().
                            contentType(ContentType.JSON).
                            extract().response();

        /**
         * Then we get all video game names in our response using path()
         * and storing that in an List of type string
         * which is a fancy way of saying a List that stores words(strings) in it
         */
        List<String> videoGameNames = response.path("name");

        /**
         * And lastly we use a for each loop to print it out
         */
        for(String videoGameName : videoGameNames ) {
            System.out.println(videoGameName);
        }

    }

    @Test
    public void testVideoGameSeralisationByJSON(){
        VideoGame videoGame = new VideoGame("4","Shooter","2005-10-23","MadMax", "Universal","95");
        given().body(videoGame).when().post(EndPoint.VIDEOGAMES).then();
    }

    /**
     * Testing JSON Schema, making sure the JSON response matches the JSON Schema
     */

    @Test
    public void testVideoGameJSONSchema(){
        given().
                pathParam("videoGameId", 4).
        when().
                get(EndPoint.SINGLE_VIDEOGAME).
        then().
                body(matchesJsonSchemaInClasspath("VideoGameJsonSchema.json")).
                log().ifValidationFails();
    }

    /**
     * Measuring Response Time In Rest Assured
     */

    @Test
    public void getResponseTime(){
         given().
                pathParam("videoGameId", 5).
                when().
                get(EndPoint.SINGLE_VIDEOGAME).
                then().
                time(lessThan(2000L));

    }
}
