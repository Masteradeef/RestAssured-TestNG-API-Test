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

import java.util.HashMap;
import java.util.Map;

/**
 * Tests for Update Note endpoints (PUT full update & PATCH partial).
 */
public class UpdateNoteTest extends BaseTest {

    private NoteClient noteClient;
    private AuthHelper authHelper;
    private String token;
    private String noteId;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        noteClient = new NoteClient();
        authHelper = new AuthHelper();
        token = authHelper.registerAndLogin();

        // Create a fresh note to update
        NoteRequest request = new NoteRequest(
                DataGenerator.randomNoteTitle(), DataGenerator.randomNoteDescription(), "Personal");
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

    @Test(groups = {"regression"}, description = "Full update (PUT) of a note should return 200 with updated fields")
    public void testFullUpdateNote() {
        String newTitle = DataGenerator.randomNoteTitle();
        String newDescription = DataGenerator.randomNoteDescription();

        NoteRequest request = new NoteRequest(newTitle, newDescription, "Home");
        request.setCompleted(false);

        Response response = noteClient.updateNote(token, noteId, request);
        log.info("Full update note response: {}", response.getBody().asString());

        ResponseValidator.assertSuccessResponse(response, 200);
        ResponseValidator.assertFieldEquals(response, "data.title", newTitle);
        ResponseValidator.assertFieldEquals(response, "data.description", newDescription);
        ResponseValidator.assertFieldEquals(response, "data.category", "Home");
    }

    @Test(groups = {"regression"}, description = "PATCH note to toggle completed to true should return 200",
            dependsOnMethods = {"testFullUpdateNote"})
    public void testToggleNoteCompleted() {
        Map<String, Object> patchBody = new HashMap<>();
        patchBody.put("completed", true);

        // Also required by API: title, description, category for PATCH
        patchBody.put("title", DataGenerator.randomNoteTitle());
        patchBody.put("description", DataGenerator.randomNoteDescription());
        patchBody.put("category", "Work");

        Response response = noteClient.patchNote(token, noteId, patchBody);
        log.info("Patch note (toggle completed) response: {}", response.getBody().asString());

        ResponseValidator.assertSuccessResponse(response, 200);
        ResponseValidator.assertFieldEquals(response, "data.completed", true);
    }

    @Test(groups = {"regression"}, description = "Update note without auth token should return 401")
    public void testUpdateNoteWithoutToken() {
        NoteRequest request = new NoteRequest(
                DataGenerator.randomNoteTitle(), DataGenerator.randomNoteDescription(), "Home");
        request.setCompleted(false);

        Response response = noteClient.updateNote("", noteId, request);
        log.info("Update note (no token) response: {}", response.getBody().asString());

        ResponseValidator.assertStatusCode(response, 401);
    }
}

