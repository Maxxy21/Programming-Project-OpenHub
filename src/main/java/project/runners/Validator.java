package project.runners;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.*;

/**
 * The Validator class validates all the JSON files against
 * their respective JSON Schemas. The class implements the
 * {@link Runnable} interface so that it waits before the needed files are
 * produced before execution
 *
 * @author Maxwell Aboagye
 * @author Lukas Berger
 */

public class Validator implements Runnable {
    Logger logger = LogManager.getLogger();

    /**
     * This method iterates over the files in the results folder
     * and returns the filenames starting with "Activity_"
     *
     * @return List of the activity filenames
     */
    private List<String> activityPathFinder() {
        List<String> activityFiles = new ArrayList<>();
        File file = new File("results");

        List<String> filenames = Arrays.asList(Objects.requireNonNull(file.list()));
        filenames.stream()
                .filter(filename -> filename.startsWith("Activity_"))
                .forEach(activityFiles::add);
        return activityFiles;
    }

    /**
     * This method loads both the json file and it's schema, compiles them
     * and runs the validation check on the json file
     * to check if validated against its schema is valid or not.
     *
     * @param jsonSchema the schema pathname
     * @param validJson  the json pathname
     * @throws IOException if file cannot be read
     */
    public boolean jsonValidator(String jsonSchema, String validJson) throws IOException {
        try {
            File fileJsonSchema = new File(jsonSchema);
            File fileValidJson = new File(validJson);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode schemaNode = mapper.readTree(new FileReader(fileJsonSchema));
            JsonNode validNode = mapper.readTree(new FileReader(fileValidJson));

            JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
            JsonSchema schema = factory.getSchema(schemaNode);
            Set<ValidationMessage> errors = schema.validate(validNode);
            if (errors.size() == 0) {
                logger.debug(fileValidJson.getName() + " validated successfully against " + fileJsonSchema.getName());
                return true;
            } else {
                logger.error(fileValidJson.getName() + " failed validation against " + fileJsonSchema.getName());
                errors.forEach(logger::error);
                return false;
            }
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    /**
     * This method validates each json file in the result folder
     * produced by the program against their respective schemas.
     */
    public void validator() {
        try {
            jsonValidator("src\\main\\resources\\Analysis.schema.json", "results\\analysis.json");

            for (String filename : activityPathFinder()) {
                jsonValidator("src\\main\\resources\\Activities.schema.json", "results\\" + filename);
            }
        } catch (NullPointerException | IOException e) {
            logger.error("Json file not found.\n");
        }
    }

    @Override
    public void run() {
        validator();
    }
}
