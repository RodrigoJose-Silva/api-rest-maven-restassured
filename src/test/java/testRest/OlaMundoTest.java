package testRest;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;

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
                .assertThat().statusCode(200);
    }
}
