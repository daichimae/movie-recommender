package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * This class represents the model of the application. This class stores the
 * dataset and user profiles.
 */
public class MovieRecommenderModel {
    private static final String datasetFileName = "imdb.csv";
    public List<ImdbEntry> movies; // Make this private

    public MovieRecommenderModel() {
        movies = new ArrayList<>();
        loadDataset();
    }

    private void loadDataset() {
        String datasetDirectoryPath = System.getProperty("user.dir") + "/assets/datasets/";
        String delimiter = ",";
        String regex = "(?<!\\\\)" + Pattern.quote(delimiter);

        try (BufferedReader br = new BufferedReader(
                new FileReader(datasetDirectoryPath + datasetFileName))) {
            br.readLine();
            String line = br.readLine();
            while (line != null) {
                movies.add(new ImdbEntry(line.split(regex)));
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
