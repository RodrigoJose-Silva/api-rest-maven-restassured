package testRest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class VerbsTest {

    @BeforeAll
    public static void setup () {
        RestAssured.baseURI= "http://restapi.wcaquino.me/";
    }

    @Test
    @DisplayName("Deve salvar um usuário")
    public void salvarUser () {
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body("{ \"name\": \"João da Silva\", \"age\": 30}")
        .when()
                .post("users")
        .then()
                .log().all()
                .statusCode(201)
                .body("id", is(notNullValue()))
                .body("name", is("João da Silva"))
                .body("age", is(30))
        ;


    }
}
