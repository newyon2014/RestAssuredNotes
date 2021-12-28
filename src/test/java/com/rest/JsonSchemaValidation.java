package com.rest;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

// Maven dependency JsonSchemaValidator need to be imported in POM.xml
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class JsonSchemaValidation {

    @Test
    public void validateJsonSchema() {
        given().
                baseUri("https://postman-echo.com").
                log().all().
        when().
                get("/get").
        then().
                log().all().
                assertThat().
                statusCode(200).
				// Keep the .json file under the class path. 
				// This also be placed under the resources folder - src/main/resources or src/test/resources
				// This file is generated via jsonschemanet.com by generating the schema for get request.
                body(matchesJsonSchemaInClasspath("EchoGet.json")); 
    }
}
