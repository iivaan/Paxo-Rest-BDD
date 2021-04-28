package com.paxovision.rest.bdd.steps;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class StepBase {
    //protected static RequestSpecification requestSpecification = null;
    //protected static Response response = null;

    protected  RequestSpecification requestSpecification = RestDriverFactory.getInstance().getDriver().getRequestSpecification();;
    protected  Response response = null;


}
