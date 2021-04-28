package com.paxovision.rest.bdd.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.paxovision.rest.utils.FlatMapUtil;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.messages.internal.com.google.gson.JsonObject;
import io.restassured.internal.path.json.JSONAssertion;
import io.restassured.path.json.JsonPath;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.validator.GenericValidator;
import org.assertj.core.api.SoftAssertions;
import org.hamcrest.Matchers;
import org.json.JSONArray;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.assertj.core.api.Assertions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

public class RestCommonSteps extends  StepBase{
    @Given("contentType is {string}")
    public void content_type_is(String contentType) {
        requestSpecification.contentType(contentType);
    }
    @Given("acceptType is {string}")
    public void accept_type_is(String acceptType) {
        requestSpecification.accept(acceptType);
    }
    @When("we do get with the endpoint {string}")
    public void we_do_get_with_the_endpoint(String endPoint) {
        //response =  requestSpecification.when().get(endPoint);
        RestDriverFactory.getInstance().getDriver().get(endPoint);
        response = RestDriverFactory.getInstance().getDriver().getResponse();
    }
    @Then("verify statusCode is {string}")
    public void verify_status_code_is(String statusCode) {
        response.then()
                .log().all()
                .statusCode(Integer.parseInt(statusCode));

    }
    @Then("body with path {string} hasKey {string}")
    public void body_path_has_key(String jsonPath, String key) {
        response.then().body(jsonPath, Matchers.hasKey(key));
    }
    @Then("body with path hasKeys")
    public void body_path_has_keys(io.cucumber.datatable.DataTable dataTable) {

        List<List<String>> rows = dataTable.asLists(String.class);
        for (List<String> columns : rows) {
            body_path_has_key(columns.get(0), columns.get(1));
        }
    }
    @Then("body with path {string} is greaterThan {string}")
    public void body_path_is_greater_than(String jsonPath, String value) {
        response.then().body(jsonPath, Matchers.greaterThan(Integer.parseInt(value)));
    }

    //@Then("body with path {string} has value")
/*    public void body_with_path_has_value_doc_string(String jsonPath, String docString) {
        Object value = response.jsonPath().get(jsonPath);
        if(value instanceof LinkedHashMap){
            JSONObject actualObject = new JSONObject((LinkedHashMap)value);
            JSONObject expected  = new JSONObject(docString);
            JSONAssert.assertEquals(expected,actualObject,false);
        }
        if(value instanceof ArrayList){
            JSONArray actualArray = new JSONArray(((ArrayList<?>) value).toArray());
            JSONArray expected  = new JSONArray(docString);
            JSONAssert.assertEquals(expected,actualArray,false);
        }
    }*/
    @Then("body with path {string} has value")
    public void body_with_path_has_value_doc_string_ex(String jsonPath, String docString) {

        Object value = response.jsonPath().get(jsonPath);

        Map<String,Object> actualObject = null;
        Map<String,Object> expectedObject = null;

        if(value instanceof LinkedHashMap){
            actualObject = FlatMapUtil.flatten(value);
            JSONObject expected  = new JSONObject(docString);
            expectedObject = FlatMapUtil.flatten(expected.toMap());
        }
        if(value instanceof ArrayList){
            actualObject = FlatMapUtil.flatten(value);
            JSONArray actualArray = new JSONArray(((ArrayList<?>) value).toArray());
            JSONArray expected  = new JSONArray(docString);
            expectedObject = FlatMapUtil.flatten(expected.toList());
        }
        try {
            //Map<String,Object> actualObject = FlatMapUtil.flatten(value);
            //System.out.println(actualObject);
            //JSONObject expected  = new JSONObject(docString);
           // Map<String,Object> expectedObject = FlatMapUtil.flatten(expected.toMap());
            //System.out.println(expectedObject);
            Set<String> expectedKeys = expectedObject.keySet();
            // use SoftAssertions instead of direct assertThat methods
            SoftAssertions softly = new SoftAssertions();

            for (String key : expectedKeys) {
                System.out.println("Processing: " + key );
                softly.assertThat(actualObject.containsKey(key)).as("Expecting key => " + key + " exists").isTrue();

                Object actualValue = null;
                if(actualObject.containsKey(key)) {
                    Object expectedValue = expectedObject.get(key);
                    actualValue = actualObject.get(key);
                    String assertionMsg = "Expecting key => " + key + ": value => " + expectedValue;
                    if (expectedValue.toString().contentEquals("#Number")) {
                        softly.assertThat(NumberUtils.isCreatable(actualValue.toString())).as(assertionMsg).isTrue();
                    } else if (expectedValue.toString().contentEquals("#String")) {
                        softly.assertThat(actualValue instanceof String).as(assertionMsg).isTrue();
                    } else if (expectedValue.toString().contentEquals("#Boolean")) {
                        softly.assertThat(actualValue instanceof Boolean).as(assertionMsg).isTrue();
                    } else if (expectedValue.toString().contentEquals("#NotNull")) {
                        softly.assertThat(actualValue != null).as(assertionMsg).isTrue();
                    } else if (expectedValue.toString().contentEquals("#Null")) {
                        softly.assertThat(actualValue == null).as(assertionMsg).isTrue();
                    } else if (expectedValue.toString().contentEquals("#DateTime")) {
                        Instant instant = Instant.parse(actualValue.toString());
                        Date date = Date.from(instant);
                        softly.assertThat(date != null).as(assertionMsg).isTrue();
                    } else if (expectedValue.toString().startsWith("#DateTime#")) {
                        String dateTimeFormat = expectedValue.toString().replace("#DateTime#", "");
                        SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat);
                        try {
                            Date date = sdf.parse(actualValue.toString());
                            softly.assertThat(date != null).as(assertionMsg).isTrue();
                        } catch (ParseException e) {
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }
                    } else if (expectedValue.toString().startsWith("#RegEx#")) {
                        String pattern = expectedValue.toString().replace("#RegEx#", "");
                        softly.assertThat(expectedValue.toString()).as(assertionMsg).matches(pattern);
                    } else {
                        softly.assertThat(actualValue).as(assertionMsg).isEqualTo(expectedValue);
                    }
                }
            }
            softly.assertAll();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Then("body with path {string} has value equal to {string}")
    public void body_with_path_has_value_equal_to(String jsonPath, String value) {
        response.then().body(jsonPath, Matchers.equalTo(value));
    }
    @Then("body with path {string} has int value equal to {string}")
    public void body_with_path_has_int_value_equal_to(String jsonPath, String value) {
        Integer intValue = Integer.parseInt(value);
        response.then().body(jsonPath, Matchers.equalTo(intValue));
    }

    @Then("body with path has int values equal to")
    public void body_with_path_has_int_values_equal_to(io.cucumber.datatable.DataTable dataTable) {
        List<List<String>> rows = dataTable.asLists(String.class);
        for (List<String> columns : rows) {
            body_with_path_has_int_value_equal_to(columns.get(0), columns.get(1));
        }
    }
    @Then("body with path has values equal to")
    public void body_with_path_has_values_equal_to(io.cucumber.datatable.DataTable dataTable) {
        List<List<String>> rows = dataTable.asLists(String.class);
        for (List<String> columns : rows) {
            body_with_path_has_value_equal_to(columns.get(0), columns.get(1));
        }
    }

    @Then("body with path {string} has value {string}")
    public void body_with_path_has_value(String jsonPath, String value) {
           Object actual = response.jsonPath().get(jsonPath);

            String assertionMsg = "Expecting key => " + jsonPath + ": value => " + value;

           if(value.contentEquals("#Number")){
            assertThat(NumberUtils.isCreatable(actual.toString())).as(assertionMsg).isTrue();
           }
           else if(value.contentEquals("#String")){
               assertThat(actual instanceof  String).as(assertionMsg).isTrue();
           }
           else if(value.contentEquals("#Boolean")){
               assertThat(actual instanceof  Boolean).as(assertionMsg).isTrue();
           }
           else if(value.contentEquals("#Array")){
               assertThat(actual instanceof ArrayList).as(assertionMsg).isTrue();
           }
           else if(value.contentEquals("#Object")){
               assertThat(actual instanceof LinkedHashMap).as(assertionMsg).isTrue();
           }
           else if(value.contentEquals("#NotNull")){
               assertThat(actual != null).as(assertionMsg).isTrue();
           }
           else if(value.contentEquals("#Null")){
               assertThat(actual == null).as(assertionMsg).isTrue();
           }
           else if(value.contentEquals("#DateTime")){
               Instant instant = Instant.parse(actual.toString());
               Date date = Date.from(instant);
               assertThat(date != null).as(assertionMsg).isTrue();
           }
           else if(value.startsWith("#DateTime#")){
               String dateTimeFormat = value.replace("#DateTime#","");
               SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat);
               try {
                   Date date = sdf.parse(actual.toString());
                   assertThat(date != null).as(assertionMsg).isTrue();
               } catch (ParseException e) {
                   e.printStackTrace();
                   throw new RuntimeException(e);
               }
           }
           else if(value.startsWith("#RegEx#")){
               String pattern = value.replace("#RegEx#","");
               assertThat(actual.toString()).as(assertionMsg).matches(pattern);
           }
           else{
               assertThat(actual.toString()).as(assertionMsg).isEqualTo(value);
           }

    }



    public class TextNodeComparator implements Comparator<JsonNode>
    {
        @Override
        public int compare(JsonNode o1, JsonNode o2) {
            if (o1.equals(o2)) {
                return 0;
            }
            if ((o1 instanceof TextNode) && (o2 instanceof TextNode)) {
                String s1 = ((TextNode) o1).asText();
                String s2 = ((TextNode) o2).asText();
                if (s1.equalsIgnoreCase(s2)) {
                    return 0;
                }
            }
            return 1;
        }
    }
}
