package com.orangeHRM.utilities;

import io.restassured.RestAssured;
import io.restassured.response.Response;


public class APIUtility {

    //method to send get request
    public static Response sendGetRequest(String endpoint) {
        return RestAssured.get(endpoint);
    }

    // method to send post request
    public static Response sendPostRequest(String endPoint, String payload) {
        return RestAssured.given().header("Content-Type", "application/json")
                .body(payload).post(endPoint);
    }

    // validating the status code
    public static boolean validateResponseStatusCode(Response response, int statuscode) {
        return response.getStatusCode() == statuscode;
    }

    // extract the value from the response
    public static String getJsonValue(Response response,String key){
        return response.jsonPath().getString(key);
    }
}





