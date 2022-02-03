package project.runners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class fetches webpage content
 * from the Open Data Hub API
 *
 * @author Maxwell Aboagye
 * @author Berger Lukas
 */
public class ApiDataFetcher {
    private static final Logger logger = LogManager.getLogger();

    /**
     * This method fetches the content of the desired
     * number of activities from the Open Data Hub API
     *
     * @return content of the web page
     */
    public static StringBuilder fetchApiContent(int pagesize) {
        StringBuilder content = null;
        try {
            BufferedReader reader;
            logger.debug("Getting data from the API....");
            if (pagesize <= 0) {
                logger.error("Number of activities locations requested is not valid!");
            } else {
                logger.info(pagesize + " activity " + ((pagesize == 1) ? "location" : "locations") + " ordered.");
            }
            URL url = new URL(String.format("https://tourism.opendatahub.bz.it/api/Activity?pagenumber=1&pagesize=%d", pagesize));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int status = connection.getResponseCode();
            logger.debug("Connection status: " + status);
            if (status > 299) {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            } else
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            logger.error("No input.txt file found");
        } catch (IOException e) {
            logger.error("This file cannot be read.");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return content;
    }

    /**
     * This method reads from a file and returns the number of
     * activity locations to be ordered
     * @param filename the input file pathname
     * @return an integer; pageszie to be used by {@link #fetchApiContent(int)}
     */
    public static int getInputData(String filename) {
        int pagesize = 0;
        try {
            BufferedReader reader;
            reader = new BufferedReader(new FileReader(filename));
            pagesize = Integer.parseInt(reader.readLine().trim());
            reader.close();
        } catch (IOException e) {
            logger.error("File not found");
        }
        return pagesize;
    }
}
