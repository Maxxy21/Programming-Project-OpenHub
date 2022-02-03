package project.runners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is the main class of the application.
 *
 * @author Maxwell Aboagye
 * @author Lukas Berger
 */
public class Runner {
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        try {
            ActivitiesAnalysis calculations = new ActivitiesAnalysis();
            Validator validator = new Validator();

            logger.info("Execution started.");

            Thread thread1 = new Thread(calculations, "Writer");
            Thread thread2 = new Thread(validator, "DataValidator");

            thread1.start();
            thread1.join();
            thread2.start();
            thread2.join();
        } catch (Exception e) {
            logger.debug(e::toString, e);
        }
        logger.info("Execution ended.\n");
    }
}
