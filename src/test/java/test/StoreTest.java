package test;

import pageModel.StoreModel;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import org.junit.Assert;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class StoreTest extends StoreModel {

    String jsonBody = "{ \"id\": 1, \"author\": \"John Smith\", \"title\": \"Reliability of late night\n" +
            "deployments\" }";
    String incorrectJsonBody = "{ \"id\": 1, \"title\": \"Reliability of late night\n" +
            "deployments\" }";
    String emptyFieldJsonBody = "{ \"id\": 1, \"author\": \"\", \"title\": \"Reliability of late night\n" +
            "deployments\" }";

    /*
     1. Verify that the API starts with an empty store.
     At the beginning of a test case, there should be no books stored on the server.
     */
    @Test
    public void bookStoreTest() {
        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .get("/books")
                .then()
                .extract()
                .response();
        assertEquals(response.statusCode(),200);
        //Assert.assertNotNull(response.getBody().jsonPath().getList("id").get(0));
    }

    /*
     2. Verify that title and author are required fields.
     PUT on /api/books/ should return an error Field '<field_name>' is required.
     */
    @Test
    public void fieldNameRequiredTest() {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(incorrectJsonBody)
                .put("/books/1")
                .then()
                .extract()
                .response();
        Assert.assertEquals("Field '<field_name>' cannot be empty", response.getBody().jsonPath().getString("error"));
    }

    /*
     3. Verify that title and author cannot be empty.
     PUT on /api/books/ should return an error Field '<field_name>' cannot be empty.
     */
    @Test
    public void fieldNameNoEmptyTest() {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(emptyFieldJsonBody)
                .put("/books/1")
                .then()
                .extract()
                .response();
        Assert.assertEquals("Field '<field_name>' cannot be empty", response.getBody().jsonPath().getString("error"));
    }

    /*
     4. Verify that the id field is read−only.
     You shouldn't be able to send it in the PUT request to /api/books/.
     */
    @Test
    public void errorWithIDTest() {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .put("/books/1")
                .then()
                .statusCode(400)
                .extract()
                .response();
    }

    /*
     5. Verify that you can create a new book via PUT.
     The book should be returned in the response.
     GET on /api/books/<book_id>/ should return the same book
     */
    @Test
    public void createNewBookTest() {
        Response response = given()
                .contentType(ContentType.JSON)
                .get("books/1")
                .then()
                .extract()
                .response();
        Assert.assertEquals(1, response.getBody().jsonPath().getInt("id"));
        Assert.assertEquals("John Smith", response.getBody().jsonPath().getString("author"));
        Assert.assertEquals("SRE 101", response.getBody().jsonPath().getString("title"));
    }

    /*
     6. Verify that you cannot create a duplicate book
     First request should response success code
     Second request should response error code
     */
    @Test
    public void duplicateBookTest() {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .put("books/1")
                .then()
                .statusCode(200)
                .extract()
                .response();
        Assert.assertEquals("Field 'author' cannot be empty", response.getBody().jsonPath().getString("error"));
    }
}