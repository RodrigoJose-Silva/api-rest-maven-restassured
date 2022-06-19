package testRest;

import org.apache.http.util.Asserts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class FileTest {

    @BeforeAll
    public static void setup () {
        baseURI = "http://restapi.wcaquino.me";
    }

    @Test
    @DisplayName("Deve obrigar o envido de arquivo - 'UPLOAD'")
    public void envioDeArquivoDeveSerObrigatorio () {

        given()
                .log().all()
        .when()
                .post("/upload")
        .then()
                .log().all()
                .statusCode(404)  // neste caso conforme os Status Code, o certo seria 400, porém a API está funcional com o status code recebido
                .body("error", is("Arquivo não enviado"))
        ;
    }

    @Test
    @DisplayName("Deve enviar o arquivo com sucesso - 'UPLOAD'")
    public void envioDeArquivoComSucesso () {

        given()
                .log().all()
                .multiPart("arquivo", new File("src/fileResource/Historias_de_Usuario.pdf")) // declarando onde está o arquivo a ser enviado
        .when()
                .post("/upload")
        .then()
                .log().all()
                .statusCode(200)
                .contentType("application/json")
                .body("name", is("Historias_de_Usuario.pdf"))
        ;
    }

    @Test
    @DisplayName("Não deve enviar o arquivo com tamanho maior do que foi configurado - 'UPLOAD'")
    public void envioDeArquivoComTamanhoExcedido() {

        given()
                .log().all()
                .multiPart("arquivo", new File("src/fileResource/Mindset.pdf")) // declarando onde está o arquivo a ser enviado
        .when()
                .post("/upload")
        .then()
                .log().all()
                .time(lessThan(5000L)) // determinando o tempo máximo para devolver o RESPONSE
                .statusCode(413)
        ;
    }

    @Test
    @DisplayName("Deve fazer o download do arquivo")
    public void fazerDownload () throws IOException {
        byte[] image = given()// declarando o tipo de nome da variável que ira receber o arquivo
                .log().all()
        .when()
                .get("/download")
        .then()
                //
                .statusCode(200)
                .extract().asByteArray(); // extraindo o arquivo
        File imagem = new File("src/fileResource/file.jpg"); // (Java.io) declarando local de salvar o download
        OutputStream out = new FileOutputStream(imagem); // (Java.io) onde passara para escrever o array no arquivo , necessário add uma exceção ao método
        out.write(image); // passando o array byte, necessário add nova exceção ao método
        out.close();
}
}
