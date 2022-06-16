package testRest;

import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class OlaMundoTest {

    @Test
    @DisplayName("Teste básico de resquest GET")
    public void testApi () {

        String baseUrl = "http://restapi.wcaquino.me/";

        given()// Pré condições
            .contentType(ContentType.JSON)
        .when() // Ação do método
            .get(baseUrl + "ola")
        .then() // Verificações - Assertivas
            .statusCode(200);
    }

    @Test
    @DisplayName("Teste com a assertivas do Hamcrest")
    public void conhendoMatchersHamcrest () {
        String baseUrl = "http://restapi.wcaquino.me/";

        given()// Pré condições
                .contentType(ContentType.JSON)
        .when() // Ação do método
                .get(baseUrl + "ola")
        .then() // Verificações - Assertivas
                .statusCode(200)
                .body(is("Ola Mundo!"))
                .body(containsString("Mundo"))
                .body(is(not(nullValue())));
    }
}
