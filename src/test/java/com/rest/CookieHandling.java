package com.rest;

import io.restassured.RestAssured;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.session.SessionFilter;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CookieHandling {

    @BeforeClass
    public void beforeClass(){
        RestAssured.requestSpecification = new RequestSpecBuilder().
                setRelaxedHTTPSValidation().
                setBaseUri("https://localhost:8081").
                build();
    }

   @Test
    public void form_authentication_using_csrf_token_cookie_example_1(){
        SessionFilter filter = new SessionFilter();
        given().
                auth().form("dan", "dan123", new FormAuthConfig("/signin", "txtUsername", "txtPassword").
                withAutoDetectionOfCsrf()).
                filter(filter).
                log().all().
        when().
                get("/login").
        then().
                log().all().
                assertThat().
                statusCode(200);

        System.out.println("Session id = " + filter.getSessionId());

        given().
                cookie("JSESSIONID", filter.getSessionId()).
                log().all().
        when().
                get("/profile/index").
        then().
                log().all().
                assertThat().
                statusCode(200).
                body("html.body.div.p", equalTo("This is User Profile\\Index. Only authenticated people can see this"));
    }

    @Test
    public void form_authentication_using_csrf_token_cookie_example_2(){
        SessionFilter filter = new SessionFilter();
        given().
                auth().form("dan", "dan123", new FormAuthConfig("/signin", "txtUsername", "txtPassword").
                withAutoDetectionOfCsrf()).
                filter(filter).
                log().all().
        when().
                get("/login").
        then().
                log().all().
                assertThat().
                statusCode(200);

        System.out.println("Session id = " + filter.getSessionId());
		
		// Option I - Single Cookie
        Cookie cookie = new Cookie.Builder("JSESSIONID", filter.getSessionId())
		               .setSecured(true)
                       .setHttpOnly(true)
					   .setComment("my cookie")
					   .build();
		
		// Option II - Multiple Cookies 			   
        Cookie cookie1 = new Cookie.Builder("dummy", "dummyValue").build();
        Cookies cookies = new Cookies(cookie, cookie1);

        given().
                cookie(cookie).   // Option I   
                cookies(cookies). // Option II
                log().all().
        when().
                get("/profile/index").
        then().
                log().all().
                assertThat().
                statusCode(200).
                body("html.body.div.p", equalTo("This is User Profile\\Index. Only authenticated people can see this"));
    }

    @Test
    public void fetch_single_cookie(){
        Response response = given().
                log().all().
        when().
                get("/profile/index").
        then().
                log().all().
                assertThat().
                statusCode(200).
                extract().
                response();

        System.out.println(response.getCookie("JSESSIONID"));
        System.out.println(response.getDetailedCookie("JSESSIONID")); // Include Attribute like HttpOnly or Secure or not etc 
    }

    @Test
    public void fetch_multiple_cookies(){
        Response response = given().
                log().all().
        when().
                get("/profile/index").
        then().
                log().all().
                assertThat().
                statusCode(200).
                extract().
                response();
         // Option I - Name and Value only
        Map<String, String> cookies = response.getCookies();

        for(Map.Entry<String, String> entry: cookies.entrySet()){
            System.out.println("cookie name = " + entry.getKey());
            System.out.println("cookie value = " + entry.getValue());
        }

        // Option II - Include Attribute like HttpOnly or Secure or not etc 
        Cookies cookies1 = response.getDetailedCookies();
        List<Cookie> cookieList = cookies1.asList();

        for(Cookie cookie: cookieList){
            System.out.println("cookie = " + cookie.toString());
        }
    }
}
