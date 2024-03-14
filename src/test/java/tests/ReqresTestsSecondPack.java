package tests;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

public class ReqresTestsSecondPack {

    @Test
    void createUser() {

        String userData = "{\"name\":\"neo\",\"job\":\"chosenone\"}";

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
                .body("job", is("chosenone"));

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
