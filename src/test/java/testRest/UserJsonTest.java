package testRest;

import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.request;
import static org.hamcrest.Matchers.*;

public class UserJsonTest {

    private String baseUrl = "http://restapi.wcaquino.me/";

    @Test
    @DisplayName("Deve verificar primeiro item")
    public void primeiroItem () {

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

        given()
        .when()
                .get(baseUrl + "users/2")
        .then()
                .statusCode(200)
                .body("name", containsString("Joaquina")) // verificação do primeiro nivel do response
                .body("endereco.rua", is("Rua dos bobos")); // verificação do segundo nivel do response
    }

    @Test
    @DisplayName("Deve verificar JSON com lista")
    public void verificarListaJson () {

        given()
        .when()
                .get(baseUrl + "users/3")
        .then()
                .statusCode(200)
                .body("name", containsString("Ana")) // verificação do primeiro nivel do response
                .body("filhos", hasSize(2)) // verificando o tamanho da lista
                .body("filhos[0].name", is("Zezinho")) // verificando o valor do primeiro atributo da lista
                .body("filhos[1].name", is("Luizinho")) // verificando o valor do segundo atributo da lista
                .body("filhos.name", hasItem("Zezinho")) // verificando que na lista existe um determinado valor
                .body("filhos.name", hasItems("Zezinho", "Luizinho")); // verificando que na lista existem determinados valores
    }

    @Test
    @DisplayName("Deve verificar a mensagem de erro 'Usuário Inexistente'")
    public void validaMensagemDeErro () {

        given()
        .when()
                .get(baseUrl + "users/4")
        .then()
                .statusCode(404)
                .body("error", is("Usuário inexistente")) // validando mensagem de ERRO
                ;
    }

    @Test
    @DisplayName("Deve verificar a lista na raiz")
    public void verificaLista () {

        given()
        .when()
                .get(baseUrl + "users")
        .then()
                .statusCode(200)
                .body("$", hasSize(3)) // validando a qtde de objetos na lista
                .body("", hasSize(3)) // validando a qtde de objetos na lista - diferença da verificação anterior que neste caso não é obrigatório declarar o "$"
                .body("name", hasItems("João da Silva", "Maria Joaquina", "Ana Júlia")) // verificando os valores do atributo "name" da lista
                .body("age[1]", is(25)) // verificando o valor do atributo "age" do segundo objeto da lista
                .body("filhos.name", hasItems(Arrays.asList("Zezinho", "Luizinho"))) // validando valores do atributo "name" da lista do segundo
                .body("salary", contains(1234.5677f, 2500, null)) // validando os valores do atributo "salary" na lista raiz
                ;
    }
}
