package models;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;

public class StoreModel {

    Response response;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://localhost:3000";
    }

    @After
    public void tearDown() {

    }
}
