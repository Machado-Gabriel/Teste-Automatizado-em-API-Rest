package modulos.produtos;

import dataFactory.ProdutoDataFactory;
import dataFactory.UsuarioDataFactory;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
import pojo.*;

@DisplayName("Testes de API Rest do módulo de Produto")
public class ProdutoTest {
    private String token;

    @BeforeEach // antes de cada teste faça:
    public void beforeeach() {
        //configurando os dados da API Rest da Lojinha
        baseURI = "http://165.227.93.41";

        basePath = "/lojinha"; // ou lojinha bugada

        this.token = given()
                .contentType(ContentType.JSON) //Enum
                .body(UsuarioDataFactory.criarUsuarioAdministrador)
            .when()
                .post("/v2/login")


            .then()
                .extract()
                    .path("data.token");

    }

    @Test
    //primeiro caso de teste
    @DisplayName("Validar que o valor do produto igual a 00.00 não é permitido")

    public void testValidarLimitesZeradosValorProduto(){
        //tentar inserir produto com valor 0 e validar mensagem de erro

                given()
                    .contentType(ContentType.JSON)
                    .header("token", this.token)
                    .body(ProdutoDataFactory.criarProdutoComumComValorIgualA(0.00))
                .when()
                    .post("/v2/produtos")

                .then()
                    .assertThat()
                        .body("error", equalTo("O valor do produto deve estar entre R$ 0,01 e R$ 7.000,00"))
                        .statusCode(422);

    }
    @Test
    @DisplayName("Validar que o valor do produto acima de 7000 não é permitido")

    public void testValidarValorProdutoAcima(){
        //tentar inserir produto com valor acima de 7000 e validar mensagem de erro

        given()
                .contentType(ContentType.JSON)
                .header("token", this.token)
                .body(ProdutoDataFactory.criarProdutoComumComValorIgualA(7000.01))
        .when()
            .post("/v2/produtos")
        .then()
            .assertThat()
                .body("error", equalTo("O valor do produto deve estar entre R$ 0,01 e R$ 7.000,00"))
                .statusCode(422);

    }



}
