package project.runners;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

import project.mappers.Activity;
import project.mappers.Analysis;
import project.mappers.AnalysisRegions;
import project.mappers.ActivitiesFromJson;
import project.mappers.apiClasses.Item;

import static project.runners.ApiDataFetcher.fetchApiContent;
import static project.runners.ApiDataFetcher.getInputData;

/**
 * This class reads Activity data from
 * @see ApiDataFetcher#fetchApiContent(int)
 * and analyzes them.
 * @author Berger Lukas
 * @author Maxwell Aboagye
 */

public class ActivitiesAnalysis implements Runnable {
    private static final Logger logger = LogManager.getLogger();

    /**
     * This method maps the activities from the Json content to a list
     * and removes any unnecessary characters from the String
     *
     * @param activitiesFromJson an object of the {@link ActivitiesFromJson} class
     * @return an activity list of the mapped activities from the Json content
     */
    public static List<Activity> MapActivitiesFromJsonToActivity(ActivitiesFromJson activitiesFromJson) {
        List<Activity> activityList = new ArrayList<>();

        for (Item item : activitiesFromJson.Items) {
            Activity activity = new Activity();
            if (item.Detail.en != null) {
                activity.id = item.Id;
                if (item.Detail.en.Title != null) {
                    activity.name = item.Detail.en.Title;
                }
                if (item.Detail.en.BaseText != null) {
                    activity.description = item.Detail.en.BaseText.replaceAll("<[^>]*>|&.*?;|[\\r\\n]", "")
                            .replace("&nbsp;", " ");
                }
                if (item.LocationInfo.RegionInfo != null) {
                    if (item.LocationInfo.RegionInfo.Name != null) {
                        activity.region = item.LocationInfo.RegionInfo.Name.en;
                    }
                }
            } else if (item.Detail.it != null) {
                activity.id = item.Id;
                if (item.Detail.it.Title != null) {
                    activity.name = item.Detail.it.Title;
                }
                if (item.Detail.it.BaseText != null) {
                    activity.description = item.Detail.it.BaseText.replaceAll("<[^>]*>|&.*?;|[\\r\\n]", "")
                            .replace("&nbsp;", " ");
                }
                if (item.LocationInfo.RegionInfo != null) {
                    if (item.LocationInfo.RegionInfo.Name != null) {
                        activity.region = item.LocationInfo.RegionInfo.Name.it;
                    }
                }
            } else {
                activity.id = item.Id;
                activity.name = item.Detail.de.Title;
                activity.description = item.Detail.de.BaseText.replaceAll("<[^>]*>|&.*?;|[\\r\\n]", "")
                        .replace("&nbsp;", " ");

                if (item.LocationInfo.RegionInfo != null) {
                    if (item.LocationInfo.RegionInfo.Name != null) {
                        activity.region = item.LocationInfo.RegionInfo.Name.de;
                    }
                }
            }
            activity.types = new ArrayList<>();
            item.ODHTags.forEach(odhTag -> activity.types.add(odhTag.Id));
            activity.hasGpsTrack = item.GpsTrack != null;
            activityList.add(activity);
        }
        return activityList;
    }

    /**
     * @param activityList an object of {@link Activity} class
     * @return an analyzed list of activity types and tracked activity IDs
     */
    private static Analysis AddActivitiesTypesAndTrackedActivityIdsToAnalysis(List<Activity> activityList) {
        Analysis analysis = new Analysis();
        analysis.trackedActivityIds = new ArrayList<>();
        analysis.activitiesTypes = new LinkedHashMap<>();
        for (Activity activity : activityList) {
            if (activity.hasGpsTrack) {
                analysis.trackedActivityIds.add(activity.id);
            }
            List<String> types = activity.types;
            for (String type : types) {
                int counter;
                if (analysis.activitiesTypes.get(type) == null) {
                    counter = 1;
                } else {
                    counter = analysis.activitiesTypes.get(type);
                    counter++;
                }
                analysis.activitiesTypes.put(type, counter);
            }
        }
        return analysis;
    }

    /**
     * @param analysis an object of the {@link Analysis} class
     * @param items    an object of the {@link Item} class
     */
    public static void AddRegionAnalysis(Analysis analysis, List<Item> items) {
        Map<String, Integer> activityCountGroupedByRegion = new LinkedHashMap<>();
        for (Item item : items) {
            if (item.LocationInfo != null && item.LocationInfo.RegionInfo != null) {
                if (item.LocationInfo.RegionInfo.Id != null) {
                    if (!activityCountGroupedByRegion.containsKey(item.LocationInfo.RegionInfo.Id)) {
                        activityCountGroupedByRegion.put(item.LocationInfo.RegionInfo.Id, 1);
                    } else {
                        int counter = activityCountGroupedByRegion.get(item.LocationInfo.RegionInfo.Id);
                        counter++;
                        activityCountGroupedByRegion.put(item.LocationInfo.RegionInfo.Id, counter);
                    }
                }
            }
        }
        int max = 0;
        int min = -1;
        for (Map.Entry<String, Integer> region : activityCountGroupedByRegion.entrySet()) {
            if (region.getValue() > max) {
                max = region.getValue();
                analysis.regionsWithMostActivities = new AnalysisRegions();
                analysis.regionsWithMostActivities.regionIds = new ArrayList<>();
                analysis.regionsWithMostActivities.numberOfActivities = max;
                analysis.regionsWithMostActivities.regionIds.add(region.getKey());
            } else if (region.getValue() == max) {
                analysis.regionsWithMostActivities.regionIds.add(region.getKey());
            }
            if (region.getValue() < min || min == -1) {
                min = region.getValue();
                analysis.regionsWithLeastActivities = new AnalysisRegions();
                analysis.regionsWithLeastActivities.regionIds = new ArrayList<>();
                analysis.regionsWithLeastActivities.numberOfActivities = min;
                analysis.regionsWithLeastActivities.regionIds.add(region.getKey());
            } else if (region.getValue() == min) {
                analysis.regionsWithMostActivities.regionIds.add(region.getKey());
            }
        }
    }

    /**
     * @param activityList an object of {@link Activity} class
     * @throws IOException if there's an IO error
     */
    private static void writeActivitiesToFile(List<Activity> activityList) throws IOException {
        int count = 0;
        for (Activity activity : activityList) {
            writeObjectToFile("results/Activity_" + activity.id + ".json", activity);
            count++;
        }
        logger.info(count + " activity " + ((count == 1) ? "location" : "locations") + " retrieved.");
    }

    /**
     * @param fileName pathname of the file to be written.
     * @param object   an object of {@link Object} class.
     * @throws IOException if an IO error occurs
     */
    private static void writeObjectToFile(String fileName, Object object) throws IOException {
        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        File file = new File(fileName);
        file.getParentFile().mkdir();
        Writer writer = new FileWriter(fileName);
        gson.toJson(object, writer);
        writer.flush();
        writer.close();
    }

    /**
     * This static method deserializes the Json string
     * from {@link ActivitiesFromJson} into the needed type
     * using Gson and writes the activity and the analysis
     * to json file
     */
    public static void writeFiles() {
        Gson gson = new Gson();
        Type type = new TypeToken<ActivitiesFromJson>() {
        }.getType();
        try {
            int pagesize = getInputData("src\\main\\resources\\input.txt");
            ActivitiesFromJson activitiesFromJson = gson.fromJson(fetchApiContent(pagesize).toString(), type);
            List<Activity> activityList = MapActivitiesFromJsonToActivity(activitiesFromJson);
            writeActivitiesToFile(activityList);
            logger.debug("Writing the activities.json files....");

            Analysis analysis = AddActivitiesTypesAndTrackedActivityIdsToAnalysis(activityList);
            AddRegionAnalysis(analysis, activitiesFromJson.Items);
            logger.debug("Writing the analysis.json file....");
            writeObjectToFile("results/analysis.json", analysis);
        } catch (NullPointerException | IOException e) {
            logger.error("File not found.");
        } catch (NumberFormatException e) {
            logger.error("Please insert only an integer value.");
        }
    }

    @Override
    public void run() {
        writeFiles();
    }
}
