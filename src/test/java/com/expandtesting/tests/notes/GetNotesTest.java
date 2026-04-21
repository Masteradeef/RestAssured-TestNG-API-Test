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

import java.util.List;

/**
 * Tests for Get Notes endpoints (list & by ID).
 */
public class GetNotesTest extends BaseTest {

    private NoteClient noteClient;
    private AuthHelper authHelper;
    private String token;
    private String noteId;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        noteClient = new NoteClient();
        authHelper = new AuthHelper();
        token = authHelper.registerAndLogin();

        // Create a note to use in GET by ID tests
        NoteRequest request = new NoteRequest(
                DataGenerator.randomNoteTitle(), DataGenerator.randomNoteDescription(), "Work");
        Response createResponse = noteClient.createNote(token, request);
        noteId = createResponse.jsonPath().getString("data.id");
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (authHelper != null) {
            authHelper.deleteAccount();
        }
    }

    @Test(groups = {"smoke"}, description = "Get all notes should return 200 and a non-null list")
    public void testGetAllNotes() {
        Response response = noteClient.getAllNotes(token);
        log.info("Get all notes response: {}", response.getBody().asString());

        ResponseValidator.assertSuccessResponse(response, 200);
        List<?> notes = response.jsonPath().getList("data");
        Assert.assertNotNull(notes, "Data list should not be null");
        Assert.assertFalse(notes.isEmpty(), "At least one note should exist");
    }

    @Test(groups = {"smoke"}, description = "Get note by valid ID should return 200 and correct fields")
    public void testGetNoteById() {
        Response response = noteClient.getNoteById(token, noteId);
        log.info("Get note by id response: {}", response.getBody().asString());

        ResponseValidator.assertSuccessResponse(response, 200);
        ResponseValidator.assertFieldEquals(response, "data.id", noteId);
        ResponseValidator.assertFieldNotNull(response, "data.title");
    }

    @Test(groups = {"regression"}, description = "Get note by non-existent ID should return 404")
    public void testGetNoteByNonExistentId() {
        Response response = noteClient.getNoteById(token, "000000000000000000000000");
        log.info("Get note (not found) response: {}", response.getBody().asString());

        ResponseValidator.assertStatusCode(response, 404);
    }

    @Test(groups = {"regression"}, description = "Get all notes without auth token should return 401")
    public void testGetAllNotesWithoutToken() {
        Response response = noteClient.getAllNotes("");
        log.info("Get all notes (no token) response: {}", response.getBody().asString());

        ResponseValidator.assertStatusCode(response, 401);
    }
}

