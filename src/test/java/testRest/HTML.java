package testRest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class HTML {

    @BeforeAll
    public static void setup () {
        RestAssured.baseURI= "http://restapi.wcaquino.me";
    }

    @Test
    @DisplayName("Deve fazer buscas com formato HTML")
    public void fazerBuscaComHTML () {
        given()
                .log().all()
        .when()
                .get("/v2/users")
        .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.HTML)
                .body("html.body.div.table.tbody.tr.size()", is(3)) // verificando a qtde de linhas da tabela com HTML
                .body("html.body.div.table.tbody.tr[1].td[2]", is("25")) // verificando valor da atributo "age" da linha 2 através de HTML
        ;
    }
}
