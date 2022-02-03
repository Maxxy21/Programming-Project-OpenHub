import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import project.runners.ApiDataFetcher;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This is the test class for the {@link ApiDataFetcher} class
 *
 * @author Maxwell Aboagye
 * @author Berger Lukas
 */
public class ApiDataFetcherTest {

    @Test
    public void fetchApiContentTest() throws Exception {
        int pagesize = ApiDataFetcher.getInputData("src\\test\\resources\\inputTest.txt");
        StringBuilder content = ApiDataFetcher.fetchApiContent(pagesize);
        JsonObject actual = JsonParser.parseString(content.toString()).getAsJsonObject();
        JsonObject expected = JsonParser.parseString(readActivitiesTestFile()).getAsJsonObject();

        assertTrue(actual.isJsonObject());
        assertTrue(expected.isJsonObject());
    }

    @Test
    public void getInputDataTest() {
        int pageSize = ApiDataFetcher.getInputData("src\\test\\resources\\inputTest.txt");
        assertEquals(10, pageSize);
    }

    @Test
    public void getInputDataTestNonExistentFile() {
        int pageSize = ApiDataFetcher.getInputData("");
        assertEquals(0, pageSize);
    }


    public static String readActivitiesTestFile() throws Exception {
        return new String(Files.readAllBytes(Paths.get("src\\test\\resources\\activitiesTest.json")));
    }
}
