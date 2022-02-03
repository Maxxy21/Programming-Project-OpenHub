import org.junit.jupiter.api.Test;
import project.runners.Validator;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This is the test class for the {@link Validator} class
 *
 * @author Maxwell Aboagye
 * @author Berger Lukas
 */

public class DataValidatorTest {

    @Test
    public void jsonValidatorTest() throws IOException {
        Validator validator = new Validator();
        boolean isJsonValid = validator.jsonValidator("src\\test\\resources\\analysisTest.schema.json", "src\\test\\resources\\analysisTest.json");
        assertTrue(isJsonValid);
    }

    @Test
    public void jsonValidatorTestWrongFilePath() throws IOException {
        Validator validator = new Validator();
        boolean isJsonValid = validator.jsonValidator("wrongpath", "wrongpath");
        assertFalse(isJsonValid);
    }
}

