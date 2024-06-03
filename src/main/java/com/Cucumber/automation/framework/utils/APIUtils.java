package com.Cucumber.automation.framework.utils;

import com.Cucumber.automation.framework.constants.AppConstants;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.ThreadContext;
import org.json.JSONObject;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class APIUtils {
    private Logging log;
    //    public Response response;
    private String body;
    private RequestSpecification request;



    public APIUtils() {
        ThreadContext.pop();
        ThreadContext.push(this.getClass().getSimpleName());
        request = this.headerSetup();
    }

    public APIUtils(AppConstants.Api.AuthenticationType authType) {
        ThreadContext.pop();
        ThreadContext.push(this.getClass().getSimpleName());

        switch (authType) {
            case Basic:
                request = this.headerSetupBasic(AppConstants.Api.API_USERNAME, AppConstants.Api.API_PASSWORD);
                break;
            case Digest:
                request = this.headerSetupDigest(AppConstants.Api.API_USERNAME, AppConstants.Api.API_PASSWORD);
                break;
            case Token:
                request = this.headerSetup(AppConstants.Api.API_TOKEN);
                break;
            default:
                log.error("INVALID API AUTHENTICATION TYPE");
                break;
        }

    }

    public APIUtils(AppConstants.Api.AuthenticationType authType, AppConstants.Api.GrantType grantType) {
        ThreadContext.pop();
        ThreadContext.push(this.getClass().getSimpleName());

        if (authType == AppConstants.Api.AuthenticationType.OAuth2) {
            switch (grantType) {
                case Client_Credentials:
                    request = this.headerSetup(getAccessTokenWithClientCredentials());
                    break;
                case Authorization_Code:
                    request = this.headerSetup(getAccessTokenWithAuthorizationCode());
                    break;
                case Password:
                    request = this.headerSetup(getAccessTokenWithPassword());
                    break;
                default:
                    log.error("INVALID GRANT TYPE PASSED FOR API AUTHENTICATION");
                    break;
            }
        } else {
            log.error("INVALID API AUTHENTICATION TYPE");
        }
    }

    /**
     * @Description: This method is used to return a Bearer-token for API calls
     * by using the token configuration details in the properties file.
     */
    private String getAccessTokenWithClientCredentials() {
        String accessToken = null;
        String tokenType = null;

        log.info("Trying to generate API Access token...");
        Response response = given().auth().preemptive()
                .basic(AppConstants.Api.API_USERNAME, AppConstants.Api.API_PASSWORD)
                .contentType("application/x-www-form-urlencoded")
                .formParam("grant_type", AppConstants.Api.API_GRANT_TYPE)
                .formParam("scope", AppConstants.Api.API_SCOPE)
                .when()
                .post(AppConstants.Api.API_ACCESS_TOKEN_URL);

        log.info("Access token generated successfully.");
        JSONObject jsonObject = new JSONObject(response.getBody().asString());
        accessToken = jsonObject.getString("access_token");
        tokenType = jsonObject.getString("token_type");

        return (tokenType + " " + accessToken);
    }

    /**
     * @Description: This method is used to return a Bearer-token for API calls
     * by using the token configuration details in the properties file.
     */
    private String getAccessTokenWithPassword() {
        String accessToken = null;
        String tokenType = null;

        log.info("Trying to generate API Access token...");
        Response response = given().auth().preemptive()
                .basic(AppConstants.Api.API_CLIENT_ID, AppConstants.Api.API_CLIENT_SECRET)
                .contentType("application/x-www-form-urlencoded")
                .formParam("grant_type", AppConstants.Api.API_GRANT_TYPE)
                .formParam("scope", AppConstants.Api.API_SCOPE)
                .formParam("username", AppConstants.Api.API_USERNAME)
                .formParam("password", AppConstants.Api.API_PASSWORD)
                .when()
                .post(AppConstants.Api.API_ACCESS_TOKEN_URL);

        log.info("Access token generated successfully.");
        JSONObject jsonObject = new JSONObject(response.getBody().asString());
        accessToken = jsonObject.getString("access_token");
        tokenType = jsonObject.getString("token_type");

        return (tokenType + " " + accessToken);
    }

    /**
     * @Description: This method is used to return a Bearer-token for API calls
     * by using the token configuration details in the properties file.
     */
    private String getAccessTokenWithAuthorizationCode() {
        String accessToken = null;
        String tokenType = null;

        log.info("Trying to generate API Access token...");
        Response response = given()
                .header("Authorization", AppConstants.Api.API_CLIENT_ID, AppConstants.Api.API_CLIENT_SECRET)
                .contentType("application/x-www-form-urlencoded")
                .formParam("grant_type", AppConstants.Api.API_GRANT_TYPE)
                .formParam("redirect_uri", AppConstants.Api.API_REDIRECT_URL)
                .formParam("response_type", AppConstants.Api.API_RESPONSE_TYPE)
                .formParam("code", AppConstants.Api.API_AUTHORIZATION_CODE)
                .formParam("client_id", AppConstants.Api.API_CLIENT_ID)
                .formParam("client_secret", AppConstants.Api.API_CLIENT_SECRET)
                .when()
                .post(AppConstants.Api.API_ACCESS_TOKEN_URL);

        log.info("Access token generated successfully.");
        JSONObject jsonObject = new JSONObject(response.getBody().asString());
        accessToken = jsonObject.getString("access_token");
        tokenType = jsonObject.getString("token_type");

        return (tokenType + " " + accessToken);
    }

    /**
     * @Description: This is the common header setup to be used in all API calls with no authentication
     */
    private RequestSpecification headerSetup() {
        log.info("Setting up API call header (without authorization)...");
        return given().header("Content-Type", ContentType.JSON)
                .header("Accept", ContentType.JSON)
                .when();
    }

    /**
     * @Description: This is the common header setup to be used in all API calls with Bearer or OAuth2.0
     * authentication
     */
    private RequestSpecification headerSetup(String accessToken) {
        log.info("Setting up API call header without authorization...");
        return given().header("Content-Type", ContentType.JSON)
                .header("Accept", ContentType.JSON)
                .header("Authorization", accessToken)
                .when();
    }

    /**
     * @Description: This is the common header setup to be used in all API calls with Basic authentication
     */
    private RequestSpecification headerSetupBasic(String userName, String password) {
        log.info("Setting up API call header without authorization...");
        return given().header("Content-Type", ContentType.JSON)
                .header("Accept", ContentType.JSON)
                .auth()
                .preemptive()
                .basic(userName, password)
                .when();
    }

    /**
     * @Description: This is the common header setup to be used in all API calls with Basic authentication
     */
    private RequestSpecification headerSetupDigest(String userName, String password) {
        log.info("Setting up API call header without authorization...");
        return given().header("Content-Type", ContentType.JSON)
                .header("Accept", ContentType.JSON)
                .auth()
                .digest(userName, password)
                .when();
    }

    /**
     * @param url      = BASE_URI + endpoint
     * @param queryParams = Map of all the query parameters for the API call
     * @return Rest-Assured Response
     * @Description: This method will make a GET call and return the response data
     */
    public Response getAPIResponse(String url, Map<String, Object> queryParams) {
        Response response = null;

        log.info("Executing GET API call...");
//        RequestSpecification request = this.headerSetup();
        RequestSpecification[] when = {request};
        try {
            if (queryParams != null)
                queryParams.forEach((key, value) -> when[0].queryParam(key, value));

            response = when[0].get(url);
            log.info("GET API call executed successfully.");

            response.then().extract().response();
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get data from API", e);
        }

    }

    /**
     * @param url      = BASE_URI + endpoint
     * @param queryMap = Map of all the query parameters for the API call
     * @return Status-code as integer
     * @Description: This method will return the status code of the GET API call
     */
    public int getAPIStatusCode(String url, Map<String, Object> queryMap) {
        Response response = null;

        log.info("Trying to execute GET API call...");
//        RequestSpecification request = this.headerSetup();
        RequestSpecification[] when = {request};

        if (queryMap != null)
            queryMap.forEach((key, value) -> when[0] = when[0].queryParam(key, value));

        response = when[0].get(url);
        log.info("GET API call executed successfully.");

        return response.getStatusCode();
    }

    /**
     * @param url          = BASE_URI + endpoint
     * @param requestBody = the request body in JSON format
     * @param queryMap     = Map of all the query parameters for the API call
     * @return Rest-Assured response
     * @Description: This method will make a POST call with request body and query parameters
     * and return the response message
     */
    public Response postAPIResponse(String url, String requestBody, Map<String, Object> queryMap) {
        Response response = null;

        log.info("Trying to execute POST API call...");
//        RequestSpecification request = this.headerSetup();
        RequestSpecification[] when = {request};
        try {
            if (queryMap != null)
                queryMap.forEach((key, value) -> when[0] = when[0].queryParam(key, value));

            response = when[0].body(requestBody).post(url);
            log.info("POST API call executed successfully.");

            response.then().extract().response();
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get data from API", e);
        }
    }

    /**
     * @param url          = BASE_URI + endpoint
     * @param requestBody = the request body in JSON format
     * @param queryMap     = Map of all the query parameters for the API call
     * @return Status-code as integer
     * @Description: This method will return the status-code of POST call with request body and query parameters
     */
    public int postAPIStatusCode(String url, String requestBody, Map<String, Object> queryMap) {
        Response response = null;

        log.info("Trying to execute POST API call...");
//        RequestSpecification request = this.headerSetup();
        RequestSpecification[] when = {request};

        if (queryMap != null)
            queryMap.forEach((key, value) -> when[0] = when[0].queryParam(key, value));

        response = when[0].body(requestBody).post(url);
        log.info("POST API call executed successfully.");

        return response.getStatusCode();
    }

    /**
     * @param url          = BASE_URI + endpoint
     * @param requestBody = the request body in JSON format
     * @param queryMap     = Map of all the query parameters for the API call
     * @return Rest-Assured response
     * @Description: This method will make a PUT call with request body and query parameters
     * and return the response message
     */
    public Response putAPIResponse(String url, String requestBody, Map<String, Object> queryMap) {
        Response response = null;

        log.info("Trying to execute POST API call...");
//        RequestSpecification request = this.headerSetup();
        RequestSpecification[] when = {request};
        try {
            if (queryMap != null)
                queryMap.forEach((key, value) -> when[0] = when[0].queryParam(key, value));

            response = when[0].body(requestBody).put(url);
            log.info("PUT API call executed successfully.");

            response.then().extract().response();
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get data from API", e);
        }
    }

    /**
     * @param url          = BASE_URI + endpoint
     * @param requestBody = the request body in JSON format
     * @param queryMap     = Map of all the query parameters for the API call
     * @return Status-code as integer
     * @Description: This method will return the status-code of PUT call with request body and query parameters
     */
    public int putAPIStatusCode(String url, String requestBody, Map<String, Object> queryMap) {
        Response response = null;

        log.info("Trying to execute POST API call...");
//        RequestSpecification request = this.headerSetup();
        RequestSpecification[] when = {request};

        if (queryMap != null)
            queryMap.forEach((key, value) -> when[0] = when[0].queryParam(key, value));

        response = when[0].body(requestBody).put(url);
        log.info("PUT API call executed successfully.");

        return response.getStatusCode();
    }

    /**
     * @param url          = BASE_URI + endpoint
     * @param requestBody = the request body in JSON format
     * @param queryMap     = Map of all the query parameters for the API call
     * @return Rest-Assured response
     * @Description: This method will make a PATCH call with request body and query parameters
     * and return the response message
     */
    public Response patchAPIResponse(String url, String requestBody, Map<String, Object> queryMap) {
        Response response = null;

        log.info("Trying to execute POST API call...");
//        RequestSpecification request = this.headerSetup();
        RequestSpecification[] when = {request};
        try {
            if (queryMap != null)
                queryMap.forEach((key, value) -> when[0] = when[0].queryParam(key, value));

            response = when[0].body(requestBody).patch(url);
            log.info("PATCH API call executed successfully.");

            response.then().extract().response();
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get data from API", e);
        }
    }

    /**
     * @param url          = BASE_URI + endpoint
     * @param requestBody = the request body in JSON format
     * @param queryMap     = Map of all the query parameters for the API call
     * @return Status-code as integer
     * @Description: This method will return the status-code of PATCH call with request body and query parameters
     */
    public int patchAPIStatusCode(String url, String requestBody, Map<String, Object> queryMap) {
        Response response = null;

        log.info("Trying to execute POST API call...");
//        RequestSpecification request = this.headerSetup();
        RequestSpecification[] when = {request};

        if (queryMap != null)
            queryMap.forEach((key, value) -> when[0] = when[0].queryParam(key, value));

        response = when[0].body(requestBody).patch(url);
        log.info("PATCH API call executed successfully.");

        return response.getStatusCode();
    }

    /**
     * @param url          = BASE_URI + endpoint
     * @param requestBody = the request body in JSON format
     * @param queryMap     = Map of all the query parameters for the API call
     * @return Rest-Assured response
     * @Description: This method will make a DELETE call with request body and query parameters
     * and return the response message
     */
    public Response deleteAPIResponse(String url, String requestBody, Map<String, Object> queryMap) {
        Response response = null;

        log.info("Trying to execute POST API call...");
//        RequestSpecification request = this.headerSetup();
        RequestSpecification[] when = {request};
        try {
            if (queryMap != null)
                queryMap.forEach((key, value) -> when[0] = when[0].queryParam(key, value));

            response = when[0].body(requestBody).delete(url);
            log.info("DELETE API call executed successfully.");

            response.then().extract().response();
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get data from API", e);
        }
    }

    /**
     * @param url          = BASE_URI + endpoint
     * @param requestBody = the request body in JSON format
     * @param queryMap     = Map of all the query parameters for the API call
     * @return Status-code as integer
     * @Description: This method will return the status-code of DELETE call with request body and query parameters
     */
    public int deleteAPIStatusCode(String url, String requestBody, Map<String, Object> queryMap) {
        Response response = null;

        log.info("Trying to execute POST API call...");
//        RequestSpecification request = this.headerSetup();
        RequestSpecification[] when = {request};

        if (queryMap != null)
            queryMap.forEach((key, value) -> when[0] = when[0].queryParam(key, value));

        response = when[0].body(requestBody).delete(url);
        log.info("DELETE API call executed successfully.");

        return response.getStatusCode();
    }

}
