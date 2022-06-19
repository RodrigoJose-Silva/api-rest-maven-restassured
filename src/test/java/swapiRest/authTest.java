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

    @Test
    @DisplayName("Deve obter clima")
    public void obterClima () {
        given()
                .log().all()
                .queryParam("q", "Fortaleza,BR")
                .queryParam("appid", "b47c51cfc4cbd6cd97dc0a3aa87e69f1")
                .queryParam("units", "metric")
        .when()
                .post("https://api.openweathermap.org/data/2.5/weather")
        .then()
                .log().all()
                .statusCode(200)
                .body("name", is("Fortaleza"))
                .body("coord.lon", is(-38.5247F))
                .body("main.temp", is(27.07F))
                .body("main.temp", greaterThan(25F))  // verifica que o valor do atributo "temp" é maior que 25
        ;
    }
}

//b47c51cfc4cbd6cd97dc0a3aa87e69f1