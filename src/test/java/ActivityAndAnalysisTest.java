import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;
import project.mappers.ActivitiesFromJson;
import project.runners.ActivitiesAnalysis;

import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * This is the test class for the {@link ActivitiesAnalysis} class
 *
 * @author Maxwell Aboagye
 * @author Berger Lukas
 */
public class ActivityAndAnalysisTest {

    @Test
    void MapActivitiesTestWithContent() throws Exception {
        Gson gson = new Gson();
        Type type = new TypeToken<ActivitiesFromJson>() {
        }.getType();
        String activitiesContent = ApiDataFetcherTest.readActivitiesTestFile();
        ActivitiesFromJson activitiesFromJson = gson.fromJson(activitiesContent, type);

        assertNotNull(ActivitiesAnalysis.MapActivitiesFromJsonToActivity(activitiesFromJson));
    }



}
