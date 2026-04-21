package com.expandtesting.tests.notes;

import com.expandtesting.base.BaseTest;
import com.expandtesting.clients.NoteClient;
import com.expandtesting.models.request.NoteRequest;
import com.expandtesting.utils.AuthHelper;
import com.expandtesting.utils.DataGenerator;
import com.expandtesting.utils.ResponseValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Tests for Delete Note endpoint.
 */
public class DeleteNoteTest extends BaseTest {

    private NoteClient noteClient;
    private AuthHelper authHelper;
    private String token;
    private String noteId;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        noteClient = new NoteClient();
        authHelper = new AuthHelper();
        token = authHelper.registerAndLogin();

        // Create note to delete
        NoteRequest request = new NoteRequest(
                DataGenerator.randomNoteTitle(), DataGenerator.randomNoteDescription(), "Home");
        Response createResponse = noteClient.createNote(token, request);
        noteId = createResponse.jsonPath().getString("data.id");
        Assert.assertNotNull(noteId, "Note ID should not be null after creation");
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (authHelper != null) {
            authHelper.deleteAccount();
        }
    }

    @Test(groups = {"regression"}, description = "Delete a note without auth token should return 401")
    public void testDeleteNoteWithoutToken() {
        Response response = noteClient.deleteNote("", noteId);
        log.info("Delete note (no token) response: {}", response.getBody().asString());

        ResponseValidator.assertStatusCode(response, 401);
    }

    @Test(groups = {"regression"}, description = "Delete a note with valid token should return 200",
            dependsOnMethods = {"testDeleteNoteWithoutToken"})
    public void testDeleteNoteSuccess() {
        Response response = noteClient.deleteNote(token, noteId);
        log.info("Delete note response: {}", response.getBody().asString());

        ResponseValidator.assertSuccessResponse(response, 200);
        ResponseValidator.assertMessage(response, "Note successfully deleted");
    }

    @Test(groups = {"regression"}, description = "GET deleted note should return 404",
            dependsOnMethods = {"testDeleteNoteSuccess"})
    public void testGetDeletedNoteReturns404() {
        Response response = noteClient.getNoteById(token, noteId);
        log.info("Get deleted note response: {}", response.getBody().asString());

        ResponseValidator.assertStatusCode(response, 404);
    }

    @Test(groups = {"regression"}, description = "Delete a non-existent note should return 404")
    public void testDeleteNonExistentNote() {
        Response response = noteClient.deleteNote(token, "000000000000000000000000");
        log.info("Delete non-existent note response: {}", response.getBody().asString());

        ResponseValidator.assertStatusCode(response, 404);
    }
}

