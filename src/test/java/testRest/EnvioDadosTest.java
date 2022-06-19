package testRest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class EnvioDadosTest {

    @BeforeAll
    public static void setup () {
        baseURI= "http://restapi.wcaquino.me/";
    }

    @Test
    @DisplayName("Deve enviar um valor via QUERY")
    public void enviaValorViaQuery () {
        given()
                .log().all()
        .when()
                .get("v2/users?format=json")// declarando o valor da Query no path
        .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.JSON)
                ;
    }

    @Test
    @DisplayName("Deve enviar um valor via QUERY via Parametro")
    public void enviaValorViaQueryViaParametro () {
        given()
                .log().all()
                .queryParam("format", "xml") // declarando a Query via parametro escolhido
        .when()
                .get("v2/users")
        .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.XML)
                .contentType(containsString("charset=utf-8"))
        ;
    }

    @Test
    @DisplayName("Deve enviar um valor via Header")
    public void enviaValorViaHeader () {
        given()
                .log().all()
                .accept(ContentType.JSON) // declarando o tipo de Body da request
        .when()
                .get("v2/users")// declarando o valor da Query no path
        .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.JSON)
        ;
    }


}
