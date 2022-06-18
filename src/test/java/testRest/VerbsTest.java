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

    @Test
    @DisplayName("Não deve salvar usuário sem nome")
    public void naoDeveSalvarUsuarioSemNome () {
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body("{ \"name\": \"\", \"age\": 30}")
        .when()
                .post("users")
                .then()
                .log().all()
        .statusCode(400)
                .body("error", is("Name é um atributo obrigatório"))
                ;
    }

    @Test
    @DisplayName("Deve salvar um usuário via XML")
    public void salvarUserXML () {
        given()
                .log().all()
                .contentType(ContentType.XML)
                .body("<user>" + "<name>Joao de Barro</name>\n" + "<age>33</age>\n" + "</user>")
        .when()
                .post("usersXML")
        .then()
                .log().all()
                .statusCode(201)
                .body("user.@id", is(notNullValue()))
                .body("user.name", is("Joao de Barro"))
                .body("user.age", is("33"))
        ;
    }

    @Test
    @DisplayName("Deve alterar um usuário")
    public void alterarUser () {
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body("{ \"name\": \"Jose de Barro\", \"age\": 44}")
        .when()
                .put("users/1")
        .then()
                .log().all()
                .statusCode(200)
                .body("id", is(1))
                .body("name", is("Jose de Barro"))
                .body("age", is(44))
                .body("salary", is(1234.5678F))
        ;
    }
}
