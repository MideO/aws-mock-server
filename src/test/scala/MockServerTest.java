import com.github.mideo.mockserver.MockServer;
import com.github.mideo.apitestkit.ApiTestKitProperties;
import com.github.mideo.apitestkit.JsonParser;
import com.github.mideo.apitestkit.RestAssuredSpecFactory;

import com.google.common.collect.ImmutableMap;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class MockServerTest {
    class Mapping {
        Mapping(ImmutableMap<String, String> request, ImmutableMap<String, Object> response) {
            this.request = request;
            this.response = response;
        }

        public ImmutableMap<String, String> getRequest() {
            return request;
        }

        public ImmutableMap<String, Object> getResponse() {
            return response;
        }

        ImmutableMap<String, String> request;
        ImmutableMap<String, Object> response;
    }

    @Test
    public void main() {
        //Given
        RestAssured.defaultParser = Parser.JSON;

        MockServer.main(MockServer.args());
        ApiTestKitProperties properties = ApiTestKitProperties.create();
        String port = properties.getString("wiremock.port");
        String localhost = properties.getString("wiremock.host");
        ImmutableMap<String, String> request = ImmutableMap.of("method", "GET", "url", "/ping");
        ImmutableMap<String, Object> response = ImmutableMap.of(
                "status", 200,
                "jsonBody", ImmutableMap.of("ping", "pong")
        );

        Mapping mapping = new Mapping(request, response);


        //When

        RestAssuredSpecFactory
                .givenARequestSpecification()
                .baseUri("http://" + localhost + ":" + port)
                .body(JsonParser.serialize(mapping)).log().everything()
                .post("/__admin/mappings")
                .then().log().everything().statusCode(201);

        //Then
        RestAssuredSpecFactory
                .givenARequestSpecification()
                .baseUri("http://" + localhost + ":" + port)
                .get("/ping")
                .then().log().everything()
                .statusCode(200)
                .body("ping", equalTo("pong"));
    }

}