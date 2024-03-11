package tests.lombokmodel;
import model.lombok.LoginBodyLombokModel;
import model.lombok.LoginResponseLombokModel;
import org.junit.jupiter.api.Test;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;

public class ReqresinTests {

    @Test
    void loginTest() {
        LoginBodyLombokModel loginBody = new LoginBodyLombokModel();
        loginBody.setEmail("eve.holt@reqres.in");
        loginBody.setPassword("cityslicka");

        LoginResponseLombokModel loginResponse =
                step("Get authorization data", () ->
                        given()
                                .log().uri()
                                .log().body()
                                .contentType(JSON)
                                .body(loginBody)
                                .when()
                                .post("https://reqres.in/api/login")
                                .then()
                                .log().status()
                                .log().body()
                                .statusCode(200)
                                .extract().as(LoginResponseLombokModel.class));

        step("Verify authorization response", () ->
                assertThat(loginResponse.getToken()).isEqualTo("QpwL5tke4Pnpja7X4"));
        //    .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    void loginTestWithoutPassword() {
        LoginBodyLombokModel loginBody = new LoginBodyLombokModel();
        loginBody.setEmail("eve.holt@reqres.in");

        LoginResponseLombokModel loginResponse =
                step("Get authorization data, only email", () ->
                        given()
                                .log().uri()
                                .contentType(JSON)
                                .body(loginBody)
                                .when()
                                .post("https://reqres.in/api/login")
                                .then()
                                .log().status()
                                .log().body()
                                .statusCode(400)
                                .extract().as(LoginResponseLombokModel.class));

        step("Verify authorization response - \"Missing password\"", () ->
                assertThat(loginResponse.getError()).isEqualTo("Missing password"));
    }

    @Test
    void loginTestWithoutData() {
        step("Get \"Unsupported Media Type\" without data", () -> {
            given()
                    .log().uri()
                    .when()
                    .post("https://reqres.in/api/login")
                    .then()
                    .log().status()
                    .log().body()
                    .statusCode(415)
                    .statusLine("HTTP/1.1 415 Unsupported Media Type");
        });
    }

    @Test
    void loginTestWithoutEmail() {
        LoginBodyLombokModel loginBody = new LoginBodyLombokModel();
        loginBody.setPassword("qwerty123");

        LoginResponseLombokModel loginResponse =
                step("Get authorization data, only password", () ->
                        given()
                                .log().uri()
                                .body(loginBody)
                                .contentType(JSON)
                                .when()
                                .post("https://reqres.in/api/login")
                                .then()
                                .log().status()
                                .log().body()
                                .statusCode(400)
                                .extract().as(LoginResponseLombokModel.class));

        step("Verify authorization response - \"Missing email or username\"", () ->
                assertThat(loginResponse.getError()).isEqualTo("Missing email or username"));
    }

    @Test
    void loginTestWithUncreatedUser() {
        LoginBodyLombokModel loginBody = new LoginBodyLombokModel();
        loginBody.setEmail("zdarova");
        loginBody.setPassword("pass");

        LoginResponseLombokModel loginResponse =
                step("Get uncreated user data", () ->
                        given()
                                .log().uri()
                                .log().body()
                                .contentType(JSON)
                                .body(loginBody)
                                .when()
                                .post("https://reqres.in/api/login")
                                .then()
                                .log().status()
                                .log().body()
                                .statusCode(400)
                                .extract().as(LoginResponseLombokModel.class));

        step("Verify authorization response - \"user not found\"", () ->
                assertThat(loginResponse.getError()).isEqualTo("user not found"));
    }
}
