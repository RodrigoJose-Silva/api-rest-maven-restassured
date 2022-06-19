package swapiRest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class authTest {

    @BeforeAll
    public static void setup () {
        baseURI = "https://swapi.dev/api/";
    }

    @Test
    @DisplayName("Deve efetuar o acesso a SWAPI")
    public void acessarSWAPI () {

        given()
                .log().all()
        .when()
                .get("people/1/")
        .then()
                .log().all()
                .statusCode(200)
                .body("name", is("Luke Skywalker"))
        ;
    }
}
