package com.paxovision.rest.bdd.steps;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class RestDriver {
    protected  RequestSpecification requestSpecification = null;
    protected  Response response = null;

    public RestDriver() {
        requestSpecification = given()
                //.config(RestAssured.config().httpClient(HttpClientConfig.httpClientConfig().reuseHttpClientInstance()))
                .baseUri("http://strapi.shiftedtech.com")
                .port(80)
                .basePath("/")
                //.config(config().httpClient(httpClientConfig().reuseHttpClientInstance()))
                //.config(config().httpClient(httpClientConfig()))
                .log().all();
    }

    public RequestSpecification getRequestSpecification() {
        return requestSpecification;
    }

    public void setRequestSpecification(RequestSpecification requestSpecification) {
        this.requestSpecification = requestSpecification;
    }

    public Response getResponse() {
        return response;
        //return requestSpecification.then();
    }

    /*public void setResponse(Response response) {
        this.response = response;
    }*/

    public void get(String endPoint) {
        response =  requestSpecification.when().get(endPoint);
    }

}
