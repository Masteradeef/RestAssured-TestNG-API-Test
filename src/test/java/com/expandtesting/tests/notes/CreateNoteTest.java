package com.expandtesting.tests.notes;

import com.expandtesting.base.BaseTest;
import com.expandtesting.clients.NoteClient;
import com.expandtesting.models.request.NoteRequest;
import com.expandtesting.utils.AuthHelper;
import com.expandtesting.utils.DataGenerator;
import com.expandtesting.utils.ResponseValidator;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests for Create Note endpoint.
 */
public class CreateNoteTest extends BaseTest {

    private NoteClient noteClient;
    private AuthHelper authHelper;
    private String token;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        noteClient = new NoteClient();
        authHelper = new AuthHelper();
        token = authHelper.registerAndLogin();
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (authHelper != null) {
            authHelper.deleteAccount();
        }
    }

    @DataProvider(name = "noteCategories")
    public Object[][] noteCategories() {
        return new Object[][]{
                {"Home"},
                {"Work"},
                {"Personal"}
        };
    }

    @Test(groups = {"smoke"}, dataProvider = "noteCategories",
            description = "Create a note with each valid category should return 200")
    public void testCreateNoteWithValidCategories(String category) {
        NoteRequest request = new NoteRequest(
                DataGenerator.randomNoteTitle(),
                DataGenerator.randomNoteDescription(),
                category
        );

        Response response = noteClient.createNote(token, request);
        log.info("Create note ({}) response: {}", category, response.getBody().asString());

        ResponseValidator.assertSuccessResponse(response, 200);
        ResponseValidator.assertFieldNotNull(response, "data.id");
        ResponseValidator.assertFieldEquals(response, "data.category", category);
        ResponseValidator.assertFieldEquals(response, "data.completed", false);
    }

    @Test(groups = {"regression"}, description = "Create note without title should return 400")
    public void testCreateNoteWithoutTitle() {
        NoteRequest request = new NoteRequest(null, DataGenerator.randomNoteDescription(), "Home");
        Response response = noteClient.createNote(token, request);
        log.info("Create note (no title) response: {}", response.getBody().asString());

        ResponseValidator.assertStatusCode(response, 400);
    }

    @Test(groups = {"regression"}, description = "Create note without description should return 400")
    public void testCreateNoteWithoutDescription() {
        NoteRequest request = new NoteRequest(DataGenerator.randomNoteTitle(), null, "Work");
        Response response = noteClient.createNote(token, request);
        log.info("Create note (no description) response: {}", response.getBody().asString());

        ResponseValidator.assertStatusCode(response, 400);
    }

    @Test(groups = {"regression"}, description = "Create note with invalid category should return 400")
    public void testCreateNoteWithInvalidCategory() {
        NoteRequest request = new NoteRequest(
                DataGenerator.randomNoteTitle(), DataGenerator.randomNoteDescription(), "InvalidCategory");
        Response response = noteClient.createNote(token, request);
        log.info("Create note (invalid category) response: {}", response.getBody().asString());

        ResponseValidator.assertStatusCode(response, 400);
    }

    @Test(groups = {"regression"}, description = "Create note without auth token should return 401")
    public void testCreateNoteWithoutToken() {
        NoteRequest request = new NoteRequest(
                DataGenerator.randomNoteTitle(), DataGenerator.randomNoteDescription(), "Home");
        Response response = noteClient.createNote("", request);
        log.info("Create note (no token) response: {}", response.getBody().asString());

        ResponseValidator.assertStatusCode(response, 401);
    }
}

