package com.paxovision.rest.bdd.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.config;
import static io.restassured.config.HttpClientConfig.httpClientConfig;
public class Hooks extends StepBase{
    @Before
    public void setUp(Scenario scenario) {
        //RestAssured.baseURI = "http://strapi.shiftedtech.com";
        //RestAssured.port = 80;
        //RestAssured.basePath = "/";
        //RestAssured.config(config().httpClient(httpClientConfig()));

        if(requestSpecification == null) {
            requestSpecification = RestDriverFactory.getInstance().getDriver().getRequestSpecification();
            //requestSpecification = given()
            requestSpecification
                    //.config(RestAssured.config().httpClient(HttpClientConfig.httpClientConfig().reuseHttpClientInstance()))
                    .baseUri("http://strapi.shiftedtech.com")
                    .port(80)
                    .basePath("/")
                    //.config(config().httpClient(httpClientConfig().reuseHttpClientInstance()))
                    //.config(config().httpClient(httpClientConfig()))
                    .log().all();
        }
    }

    @After
    public void tearDown(Scenario scenario) throws Exception {
        //requestSpecification.
        //RequestSpecification requestSpecification = null;
        //Response response = null;
        RestDriverFactory.getInstance().quite();
    }

}
