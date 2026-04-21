package com.expandtesting.clients;

import com.expandtesting.config.EndpointConstants;
import com.expandtesting.models.request.NoteRequest;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * API client for Notes endpoints.
 */
public class NoteClient extends BaseApiClient {

    public Response getAllNotes(String token) {
        return given()
                .spec(baseSpec)
                .header(EndpointConstants.AUTH_TOKEN_HEADER, token)
                .when()
                .get(EndpointConstants.NOTES)
                .then()
                .extract().response();
    }

    public Response createNote(String token, NoteRequest request) {
        return given()
                .spec(baseSpec)
                .header(EndpointConstants.AUTH_TOKEN_HEADER, token)
                .body(request)
                .when()
                .post(EndpointConstants.NOTES)
                .then()
                .extract().response();
    }

    public Response getNoteById(String token, String noteId) {
        return given()
                .spec(baseSpec)
                .header(EndpointConstants.AUTH_TOKEN_HEADER, token)
                .pathParam("id", noteId)
                .when()
                .get(EndpointConstants.NOTES_BY_ID)
                .then()
                .extract().response();
    }

    public Response updateNote(String token, String noteId, NoteRequest request) {
        return given()
                .spec(baseSpec)
                .header(EndpointConstants.AUTH_TOKEN_HEADER, token)
                .pathParam("id", noteId)
                .body(request)
                .when()
                .put(EndpointConstants.NOTES_BY_ID)
                .then()
                .extract().response();
    }

    public Response patchNote(String token, String noteId, Map<String, Object> fields) {
        return given()
                .spec(baseSpec)
                .header(EndpointConstants.AUTH_TOKEN_HEADER, token)
                .pathParam("id", noteId)
                .body(fields)
                .when()
                .patch(EndpointConstants.NOTES_BY_ID)
                .then()
                .extract().response();
    }

    public Response deleteNote(String token, String noteId) {
        return given()
                .spec(baseSpec)
                .header(EndpointConstants.AUTH_TOKEN_HEADER, token)
                .pathParam("id", noteId)
                .when()
                .delete(EndpointConstants.NOTES_BY_ID)
                .then()
                .extract().response();
    }
}

