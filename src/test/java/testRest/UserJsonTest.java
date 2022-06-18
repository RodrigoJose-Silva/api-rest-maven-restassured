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

    @Test
    @DisplayName("Devo fazer verificações avançadas")
    public void verificacoesAvancadas () {

        given()
        .when()
                .get(baseUrl + "users")
        .then()
                .statusCode(200)
                .body("$", hasSize(3)) // validando a qtde de objetos na lista
                .body("age.findAll{it <= 25}.size()", is(2) ) //verifica na lista qtos valores no atributo "age" são menores ou = a 25
                .body("age.findAll{it >20 && it <=25}.size()", is(1)) //verifica na lista qtos valores no atributo "age" dentro da faixa informada
                .body("findAll{it.age >20 && it.age <= 25}.name", hasItem("Maria Joaquina")) //verifica na lista o valor no atributo "name" dentro da faixa informada do atributo "age"
                .body("findAll{it.age <= 25}[0].name", is("Maria Joaquina")) //verifica na lista o primeiro valor no atributo "name" dentro da faixa informada do atributo "age"
                .body("findAll{it.name.contains('n')}.name", hasItems("Maria Joaquina", "Ana Júlia")) //verifica na lista no atributo "name", se possui algum valor que contém a letra informada
                .body("findAll{it.name.length() > 10}.name", hasItems("João da Silva", "Maria Joaquina")) //verifica na lista no atributo "name", se possui algum com mais de 10 caracteres
                .body("name.collect{it.toUpperCase()}", hasItem("MARIA JOAQUINA")) // verifica o valor do atributo "name" e passa para caixal alta
                .body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}", hasItem("MARIA JOAQUINA")) //verifica em toda a lista no atributo "name", iniciado com "Maria' convertendo a validação para caixa alta
                .body("age.collect{it * 2}", hasItems(60, 50, 40)) // pega os valores do atributo "age" multiplica por 2 e valida na sequência
                .body("id.max()", is(3)) // verifica o maior valor do atributo "id" na lista
                .body("salary.min()", is(1234.5677F)) // verifica o menor valor do atributo "salary" da lista
                .body("salary.findAll{it != null}.sum()", is(closeTo(3734.5678F, 0.001))); // verifica a soma dos valores diferente de NULL do atributo "salary"
    }
}
