package swapiRest;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class authTest {

    @Test
    @DisplayName("Deve efetuar o acesso a SWAPI")
    public void acessarSWAPI () {

        given()
                .log().all()
        .when()
                .get("https://swapi.dev/api/people/1/")
        .then()
                .log().all()
                .statusCode(200)
                .body("name", is("Luke Skywalker"))
        ;
    }

    @Test
    @DisplayName("Não deve acessar sem senha")
    public void tentarAcessarSemSenha () {
        given()
                .log().all()
        .when()
                .post("https://restapi.wcaquino.me/basicauth")
        .then()
                .statusCode(404) // status code correto para este cenário teria que ser "401 Unauthorized"
        ;
    }

    @Test
    @DisplayName("Deve efetuar a autenticação básica")
    public void tentarFazerAutenticacaoBasica () {
        given()
                .log().all()
        .when()
                .post("https://admin:senha@restapi.wcaquino.me/basicauth")// usuário e senha declarado antes da "base URI"
        .then()
                .log().all()
                .statusCode(200)
                .body("status", is("logado"))
        ;
    }

    @Test
    @DisplayName("Deve efetuar a autenticação básica 2")
    public void tentarFazerAutenticacaoBasica2 () {
        given()
                .log().all()
                .auth().basic("admin", "senha") // declarando usuário e senha
        .when()
                .post("https://restapi.wcaquino.me/basicauth")
        .then()
                .log().all()
                .statusCode(200)
                .body("status", is("logado"))
        ;
    }

    @Test
    @DisplayName("Deve efetuar a autenticação básica com CHAllenge")
    public void tentarFazerAutenticacaoBasicaChallenge () {
        given()
                .log().all()
                .auth().preemptive().basic("admin", "senha") // declarando usuário e senha
        .when()
                .post("https://restapi.wcaquino.me/basicauth2")// URI inoperante durante a execução
        .then()
                .log().all()
                .statusCode(200)
                .body("status", is("logado"))
        ;
    }

    @Test
    @DisplayName("Deve efetuar autenticação cm Token JWT")
    public void fazerAutenticacaoComToken () {

        Map<String, String> login = new HashMap<String, String>(); // criando MAP, um tipo de lista que armazena PARES
        login.put("email", "madruga01@gmail.com");
        login.put("senha", "madruga123");

        //logando na API  extraindo o token
        String token = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(login) //body JSON com valores e atributos a serem registrados conforme a config do MAP
        .when()
                .post("https://barrigarest.wcaquino.me/signin")
        .then()
                .log().all()
                .statusCode(200)
                .extract().path("token")
        ;

        //obtendo as contas cadastradas
        given()
                .log().all()
                .header("Authorization", "JWT " + token) // declarando a chave de autenticação JWT
        .when()
                .get("https://barrigarest.wcaquino.me/contas")
        .then()
                .log().all()
                .statusCode(200)
                .body("nome", hasItem("Teste"))
                ;
    }

    @Test
    @DisplayName("Deve acessar a aplicação WEB")
    public void acessandoAplicacaoWeb () {

//        Map<String, String> login = new HashMap<String, String>(); // criando MAP, um tipo de lista que armazena PARES
//        login.put("email", "madruga01@gmail.com");
//        login.put("senha", "madruga123");

        //logando na API  extraindo o token
         String cookie = given()
                .log().all()
                .contentType(ContentType.URLENC.withCharset("UTF-8"))
                .formParam("email", "madruga01@gmail.com") // declarando email de login
                .formParam("senha", "madruga123") // declarando senha de login
        .when()
                .post("https://seubarriga.wcaquino.me/logar")
        .then()
                .log().all()
                .statusCode(200)
                 .extract().header("set-cookie")
                ;

        cookie = cookie.split("=")[1].split(";")[0];

        given()
                .log().all()
                .cookie("connect.sid", cookie)
        .when()
                .get("https://seubarriga.wcaquino.me/contas")
        .then()
                .log().all()
                .statusCode(200)
                .body("html.body.table.tbody.tr[0].td[0]", is("Teste"))

        ;
    }

}