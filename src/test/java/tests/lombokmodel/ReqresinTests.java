package tests.lombokmodel;

import model.lombok.LoginBodyLombokModel;
import model.lombok.LoginResponseLombokModel;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

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
    void negativeloginTest() {

        given()
                .log().uri()
                .body("123")
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing email or username"));
    }

    @Test
    void unSupportedMediaTypeTest() {
        given()
                .log().uri()
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(415);
    }

    @Test
    void missingEmailOrUsernameTest() {
        given()
                .log().uri()
                .body("123")
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing email or username"));
    }

    @Test
    void missingPasswordTest() {
        String data = "{ \"email\": \"eve.holt@reqres.in\"}";

        given()
                .log().uri()
                .contentType(JSON)
                .body(data)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));
    }
}
