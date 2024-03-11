package tests;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import helpers.DateToStringConverter;
import model.lombok.CreateUserBodyLombokModel;
import model.lombok.CreateUserResponseLombokModel;
import org.junit.jupiter.api.Test;
import java.util.Date;

public class ReqresTestsSecondPack {

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

        String userData = "{\"name\":\"neo\"}";


        given()
                .contentType(JSON)
                .body(userData)
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", is("neo"))
                .body("id", greaterThan("0"));
    }

    @Test
    void changeUserJob() {

        String userData = "{\"name\":\"neo\",\"job\":\"newbie\"}";


        given()
                .contentType(JSON)
                .body(userData)
                .when()
                .put("https://reqres.in/api/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("name", is("neo"))
                .body("job", is("newbie"));
    }

    @Test
    void changeUserJobWithoutName() {

        String userData = "{\"job\":\"newbie\"}";


        given()
                .contentType(JSON)
                .body(userData)
                .log().all()
                .when()
                .put("https://reqres.in/api/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("job", is("newbie"));
    }

    @Test
    void changeUserWrongParameter() {

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
    }

    @Test
    void deleteUserData() {

        String userData = "{\"name\":\"neo\",\"job\":\"chosenone\"}";

        given()
                .contentType(JSON)
                .log().all()
                .body(userData)
                .when()
                .delete("https://reqres.in/api/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(204);
    }
}
