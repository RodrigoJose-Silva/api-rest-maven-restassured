package testRest;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class UserJsonTest {

    @Test
    @DisplayName("Deve verificar primeiro item")
    public void primeiroItem () {

        String baseUrl = "http://restapi.wcaquino.me/";

        given()
        .when()
                .get(baseUrl + "users/1")
        .then()
                .statusCode(200)
                .body("id", is(1))
                .body("name", containsString("João"))
                .body("age", greaterThan(18));
    }

    @Test
    @DisplayName("Deve verificar primeiro nivel de outras formas")
    public void verificandoPrimeiroNivelDeOutraForma () {

        String baseUrl = "http://restapi.wcaquino.me/";

        Response response = request(Method.GET,baseUrl + "users/1");

        //path
        Assertions.assertEquals(new Integer(1), response.path("id"));

        //jsonpath
        JsonPath jpath = new JsonPath(response.asString());
        Assertions.assertEquals(1, jpath.getInt("id"));

        //from
        int id = JsonPath.from(response.asString()).getInt("id");
        Assertions.assertEquals(1, id);
    }

    @Test
    @DisplayName("Deve verificar o segundo nivel")
    public void verificarSegundoNivel () {

        String baseUrl = "http://restapi.wcaquino.me/";

        given()
        .when()
                .get(baseUrl + "users/2")
        .then()
                .statusCode(200)
                .body("name", containsString("Joaquina")) // verificação do primeiro nivel do response
                .body("endereco.rua", is("Rua dos bobos")); // verificação do segundo nivel do response

    }
}
