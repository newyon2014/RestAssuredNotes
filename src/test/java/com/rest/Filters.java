package com.rest;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import static io.restassured.RestAssured.given;

public class Filters {
    @Test
    public void loggingFilter() throws FileNotFoundException {
		 PrintStream fileOutPutStream = new PrintStream(new File("restAssured.log"));
		 given().
                baseUri("https://postman-echo.com").
				filter(new RequestLoggingFilter(LogDetail.BODY,fileOutPutStream)).
                filter(new ResponseLoggingFilter(LogDetail.STATUS,fileOutPutStream)).
     //           log().all().
        when().
                get("/get").
        then().
   //             log().all().
                assertThat().
                statusCode(200);
    }
}
