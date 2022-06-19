package testRest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

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
                .body("{ \"name\": \"João da Silva\", \"age\": 30}") // body JSON com valores e atributos a serem registrados
        .when()
                .post("users")// método POST para add novo registro
        .then()
                .log().all()
                .statusCode(201)
                .body("id", is(notNullValue()))
                .body("name", is("João da Silva"))
                .body("age", is(30))
        ;
    }

    @Test
    @DisplayName("Deve salvar um usuário com MAP")
    public void salvarUserComMap () {

        Map<String, Object> params = new HashMap<String, Object>(); // criando MAP, um tipo de lista que armazena PARES
        params.put("name", "Usuário via MAP");
        params.put("age", 51);

        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(params) // body JSON com valores e atributos a serem registrados conforme a config do MAP
        .when()
                .post("users")// método POST para add novo registro
        .then()
                .log().all()
                .statusCode(201)
                .body("id", is(notNullValue()))
                .body("name", is("Usuário via MAP"))
                .body("age", is(51))
        ;
    }

    @Test
    @DisplayName("Deve salvar um usuário com Object")
    public void salvarUserComObject () {

        User user = new User("Usuário através de um objeto", 33); // chamando a classe "User", ja delarando os valores conforme configurado na classe

        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(user) // body JSON com valores e atributos a serem registrados conforme a classe
        .when()
                .post("users")// método POST para add novo registro
        .then()
                .log().all()
                .statusCode(201)
                .body("id", is(notNullValue()))
                .body("name", is("Usuário através de um objeto"))
                .body("age", is(33))
        ;
    }

    @Test
    @DisplayName("Deve deserializar um Object ao salvar um usuário")
    public void deserializarObject () {

        User user = new User("Usuário deserializado", 33); // chamando a classe "User", ja delarando os valores conforme configurado na classe

        User usuarioInserido = given() // add uma variável para receber o retorno desta request
                .log().all()
                .contentType(ContentType.JSON)
                .body(user) // body JSON com valores e atributos a serem registrados conforme a classe
        .when()
                .post("users")// método POST para add novo registro
        .then()
                .log().all()
                .statusCode(201)
                .extract().body().as(User.class) //extraindo o BODY do response para um arquivo da classe informada
        ;

        System.out.println(usuarioInserido);
        Assertions.assertEquals("Usuário deserializado", usuarioInserido.getName()); // verificando o valor do atributo "name"
        Assertions.assertEquals(33, usuarioInserido.getAge()); // verificando o valor do atributo "age"
    }

    @Test
    @DisplayName("Não deve salvar usuário sem nome")
    public void naoDeveSalvarUsuarioSemNome () {
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body("{ \"name\": \"\", \"age\": 30}")  // boy JSON com valores e atributos a serem registrados
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
                .body("<user>" + "<name>Joao de Barro</name>\n" + "<age>33</age>\n" + "</user>") // body XML com valores e atributos a serem registrados
        .when()
                .post("usersXML")// método de add novo registro
        .then()
                .log().all()
                .statusCode(201)
                .body("user.@id", is(notNullValue()))
                .body("user.name", is("Joao de Barro"))
                .body("user.age", is("33"))
        ;
    }

    @Test
    @DisplayName("Deve salvar um usuário via XML usando um objeto")
    public void salvarUserXMLcomOBject () {

        User user = new User("Usuario XML", 40); // add uma variável para apontar usuario e idade

        given()
                .log().all()
                .contentType(ContentType.XML)
                .body(user) // body XML com valores e atributos a serem registrados
                .when()
                .post("usersXML")// método de add novo registro
                .then()
                .log().all()
                .statusCode(201)
                .body("user.@id", is(notNullValue()))
                .body("user.name", is("Usuario XML"))
                .body("user.age", is("40"))
        ;
    }

    @Test
    @DisplayName("Deve alterar um usuário")
    public void alterarUser () {
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body("{ \"name\": \"Jose de Barro\", \"age\": 44}") // boy JSON com valores e atributos a serem alterados
        .when()
                .put("users/1") // método para alterar um registro existente
        .then()
                .log().all()
                .statusCode(200)
                .body("id", is(1))
                .body("name", is("Jose de Barro"))
                .body("age", is(44))
                .body("salary", is(1234.5678F))
        ;
    }

    @Test
    @DisplayName("Devo customizar a URL")
    public void customizandoURL () {
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body("{ \"name\": \"Manoel de Barro\", \"age\": 55}")
        .when()
                .put("{entidade}/{userID}", "users", "1")// usando customização na PATH
        .then()
                .log().all()
                .statusCode(200)
                .body("id", is(1))
                .body("name", is("Manoel de Barro"))
                .body("age", is(55))
                .body("salary", is(1234.5678F))
        ;
    }

    @Test
    @DisplayName("Deve remover um usário")
    public void removeUser () {
        given()
        .when()
                .delete("users/2")
        .then()
                .statusCode(204)
                ;
    }

    @Test
    @DisplayName("Não deve remover um usário inexistente")
    public void naoRemoveUserInexistente () {
        given()
                .log().all()
        .when()
                .delete("users/100")
        .then()
                .statusCode(400)
                .body("error", is("Registro inexistente"))
        ;
    }
}
