package com.paxovision.rest.strapi;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.given;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StrapiRestaurantTest {

    @BeforeAll
    public void setUp(){
        RestAssured.baseURI = "http://strapi.shiftedtech.com";
        RestAssured.port = 80;
        RestAssured.basePath = "/";
    }

    @Test
    public void searchStatusCodeTest1() {
        given()
            .log().all()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
        .when()
            .get("/restaurants")
        .then()
            .log().all()
            .statusCode(200);
    }

    @Test
    public void jsonPathNodeExists() {
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get("/restaurants")
                .then()
                .log().all()
                .statusCode(200)
                .body("[0]",hasKey("id"))
                .body("[0]",hasKey("name"))
                .body("[0]",hasKey("description"))
                .body("[0]",hasKey("created_at"))
                .body("[0]",hasKey("updated_at"))
                .body("[0]",hasKey("categories"))
                .body("[0].categories.size()",greaterThan(0))
                .body("[0].categories[0]",hasKey("id"))
                .body("[0].categories[0]",hasKey("name"))
                .body("[0].categories[0]",hasKey("created_at"))
                .body("[0].categories[0]",hasKey("updated_at"))
        ;
    }

    @Test
    public void jsonPathSize() {
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get("/restaurants")
                .then()
                .log().all()
                .statusCode(200)
                .body("$",hasSize(49))
                .body("$.size()",equalTo(49))
                .body("[0].categories",hasSize(2))
                .body("[0].categories.size()",equalTo(2));
    }

    @Test
    public void searchStatusCodeTest2() {
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get("/restaurants")
                .then()
                .log().all()
                .statusCode(200)
                .body("[0].id",equalTo(2))
                .body("[0].categories[0].name",equalTo("Fast food"));
    }

    @Test
    public void searchStatusCodeTest3() {
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get("/restaurants")
                .then()
                .log().all()
                .statusCode(200)
                .body("$",hasSize(49))
                .body("$.size()",equalTo(49))
                .body("[0].categories",hasSize(2))
                .body("[0].categories.size()",equalTo(2));
    }

    @Test
    public void searchStatusCodeTest4() {
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get("/restaurants")
                .then()
                .log().all()
                .statusCode(200)
                .body("id",hasSize(49));
    }

    @Test
    public void searchStatusCodeTest5() {
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get("/restaurants")
                .then()
                .log().all()
                .statusCode(200)
                .body("[0]",hasKey("categories"))
                .body("[0].categories[0]",hasKey("id"))
                .body("[0].categories.size()",equalTo(2))
                .body("[3]",hasKey("categories"))
                .body("[3].categories.size()",equalTo(0))
                .body("[3].categories[4]",not(hasKey("id")));
    }

    @Test
    public void searchStatusCodeTest6() {
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get("/restaurants")
                .then()
                .log().all()
                .statusCode(200)
                .body("[0]",hasKey("id"))
                .body("[0]",hasKey("name"))
                .body("[0]",hasKey("description"))
                .body("[0]",hasKey("created_at"))
                .body("[0]",hasKey("updated_at"))
                .body("[0]",hasKey("categories"))
                .body("[0].categories.size()",greaterThan(0))
                .body("[0].categories[0]",hasKey("id"))
                .body("[0].categories[0]",hasKey("name"))
                .body("[0].categories[0]",hasKey("created_at"))
                .body("[0].categories[0]",hasKey("updated_at"))
        ;
    }


    @AfterAll
    public void tearDown(){

    }

}
