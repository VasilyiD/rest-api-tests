package tests.lombokmodel;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;

import helpers.DateToStringConverter;
import model.lombok.CreateUserBodyLombokModel;
import model.lombok.CreateUserResponseLombokModel;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class ReqresTestsSecondPackLombok {

    @Test
    void createUser() {
        CreateUserBodyLombokModel userBody = new CreateUserBodyLombokModel();
        DateToStringConverter dtsc = new DateToStringConverter();
        userBody.setName("neo");
        userBody.setJob("chosenone");

        CreateUserResponseLombokModel userResponse =
                step("Create user with name and job", () ->
                        given()
                                .contentType(JSON)
                                .body(userBody)
                                .when()
                                .post("https://reqres.in/api/users")
                                .then()
                                .log().status()
                                .log().body()
                                .statusCode(201)
                                .extract().as(CreateUserResponseLombokModel.class));

        step("Verify created user data", () -> {
            assertThat(userResponse.getName()).isEqualTo(userBody.getName());
            assertThat(userResponse.getJob()).isEqualTo(userBody.getJob());
            assertThat(userResponse.getId()).isGreaterThan("0");
            assertThat(userResponse.getCreatedAt()).contains(dtsc.convertDateToString(new Date()));
        });
    }

    @Test
    void createUserWithoutJob() {

        CreateUserBodyLombokModel userBody = new CreateUserBodyLombokModel();
        DateToStringConverter dtsc = new DateToStringConverter();
        userBody.setName("neo");

        CreateUserResponseLombokModel userResponse =
                step("Create user without job", () ->
                        given()
                                .contentType(JSON)
                                .body(userBody)
                                .when()
                                .post("https://reqres.in/api/users")
                                .then()
                                .log().status()
                                .log().body()
                                .statusCode(201)
                                .extract().as(CreateUserResponseLombokModel.class));

        step("Verify created user data without job", () -> {
            assertThat(userResponse.getName()).isEqualTo(userBody.getName());
            assertThat(userResponse.getJob()).isEqualTo(null);
            assertThat(userResponse.getId()).isGreaterThan("0");
            assertThat(userResponse.getCreatedAt()).contains(dtsc.convertDateToString(new Date()));
        });
    }

    @Test
    void changeUserJob() {

        CreateUserBodyLombokModel userBody = new CreateUserBodyLombokModel();
        DateToStringConverter dtsc = new DateToStringConverter();
        userBody.setName("neo");
        userBody.setJob("newbie");

        CreateUserResponseLombokModel userResponse =
                step("Change user job", () ->
                        given()
                                .contentType(JSON)
                                .body(userBody)
                                .when()
                                .put("https://reqres.in/api/users/2")
                                .then()
                                .log().status()
                                .log().body()
                                .statusCode(200)
                                .extract().as(CreateUserResponseLombokModel.class));

        step("Verify user job changed", () -> {
            assertThat(userResponse.getName()).isEqualTo(userBody.getName());
            assertThat(userResponse.getJob()).isEqualTo(userBody.getJob());
            assertThat(userResponse.getUpdatedAt()).contains(dtsc.convertDateToString(new Date()));
        });

    }

    @Test
    void changeUserJobWithoutName() {

        CreateUserBodyLombokModel userBody = new CreateUserBodyLombokModel();
        DateToStringConverter dtsc = new DateToStringConverter();
        userBody.setJob("newbie");

        CreateUserResponseLombokModel userResponse =
                step("Change user job without Name", () ->
                        given()
                                .contentType(JSON)
                                .body(userBody)
                                .log().all()
                                .when()
                                .put("https://reqres.in/api/users/2")
                                .then()
                                .log().status()
                                .log().body()
                                .statusCode(200)
                                .extract().as(CreateUserResponseLombokModel.class));

        step("Verify changed job without name", () -> {
            assertThat(userResponse.getJob()).isEqualTo(userBody.getJob());
            assertThat(userResponse.getName()).isEqualTo(userBody.getName());
            assertThat(userResponse.getUpdatedAt()).contains(dtsc.convertDateToString(new Date()));
        });
    }

    @Test
    void changeUserWrongParameter() {
        step("Change user wrong parameter, expected code 400", () -> {
            String wrongParameter = "{\"hello\":\"world\"}";

            given()
                    .contentType(JSON)
                    .log().all()
                    .body(wrongParameter)
                    .when()
                    .put("https://reqres.in/api/users/2")
                    .then()
                    .log().status()
                    .log().body()
                    .statusCode(400);
        });
    }

    @Test
    void deleteUserData() {

        CreateUserBodyLombokModel userBody = new CreateUserBodyLombokModel();
        userBody.setName("neo");
        userBody.setJob("chosenone");

                step("Delete user data with name and job", () -> {
                        given()
                                .contentType(JSON)
                                .log().all()
                                .body(userBody)
                                .when()
                                .delete("https://reqres.in/api/users/2")
                                .then()
                                .log().status()
                                .log().body()
                                .statusCode(204)
                                .statusLine("HTTP/1.1 204 No Content");
    });
}
}