import config.EndPoint;
import config.TestConfig;
import io.restassured.path.xml.XmlPath;
import io.restassured.path.xml.element.Node;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;

public class GPathXMLTests extends TestConfig {
    @Test
    public void getFirstGameInTheList(){
        Response response = get(EndPoint.VIDEOGAMES);

        String name = response.path("videoGames.videoGame.name[0]");

        System.out.println(name);

    }

    @Test
    public void getAttributeName(){
        Response response = get(EndPoint.VIDEOGAMES);

        //finding out the category of the first game in the array
        String category = response.path("VideoGame.videoGame[0].@category");
        System.out.println(category);

        /**
         * As you can see we use the '@' symbol in GPath to get attribute/s in XML
         */
    }

    @Test
    public void getListOfXMLNodes(){
        String responseAsString = get(EndPoint.VIDEOGAMES).asString();

        //Using Rest Assured Node
        List<Node> allResults = XmlPath.from(responseAsString).get(
                //element -> return element gives us back all games basically
                "videoGames.videoGame.findAll { element -> return element } "
        );

        /**
         *         from our list of games, we want the 2nd one in the array.
         *         then we print out its name
         */

        System.out.println(allResults.get(2).get("name").toString());
    }

    @Test
    public void getListOfXmlNodesByFindAllOnAttribute(){
        String responseAsString = get(EndPoint.VIDEOGAMES).asString();

        List<Node> allDrivingGames = XmlPath.from(responseAsString).get(
                //we find all games with a category we have defined, in this case 'Driving
                "videoGames.videoGame.findAll { videoGame -> def category = videoGame.@category; category == 'Driving' }"
        );

        /**
         * As you can see in the above line of code (61) we can *define* a category by doing using 'def category'
         * Below code simply prints out the name of the driving game stored in position 0 of the array
         */
        System.out.println(allDrivingGames.get(0).get("name").toString());
    }

    /**
     * Capturing all XML data to do with a specific node
     */

    @Test
    public void getSingleNode(){
        String responseAsString = get(EndPoint.VIDEOGAMES).asString();

        Node videoGame = XmlPath.from(responseAsString).get(
                "videoGame.videoGame.find { videoGame -> def name = videoGame.name; name == 'Tetris'}"
                                                               //we can define any attribute in our XML with 'def'
        );

        System.out.println(videoGame.get("name").toString());
    }


    @Test
    public void getSingleElementDepthFirst(){
        String responseAsString = get(EndPoint.VIDEOGAMES).asString();

        int reviewScore = XmlPath.from(responseAsString).getInt(
                /**
                 * The wild card ' ** ' simply means it finds everything of everything
                 * Here we are finding the review score of a video game with the specified name
                 * Then we print the review score out
                  */
                "**.find { it.name == 'The Legend of Zelda: Ocarina of Time'}.reviewScore"
        );

        System.out.println(reviewScore);
    }

    /**
     * Retrieving all XML nodes based on one condition
     */

    @Test
    public void getAllNodesBasedOnACondition(){
        String responseAsString = get(EndPoint.VIDEOGAMES).asString();

        //we've specified the review score to be 90 for our condition
        int reviewScore = 90;

        List<Node> allVideoGamesOverCertainScore = XmlPath.from(responseAsString).get(
                "videoGame.videoGame.findAll { it.reviewScore.toFloat() >= " + reviewScore + "}"
                                                                                // we've concatenated our int variable here
        );

        System.out.println(allVideoGamesOverCertainScore);
    }
}
