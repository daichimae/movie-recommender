package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * This class represents the model of the application. This class stores the
 * dataset and user profiles.
 */
public class MovieRecommenderModel {
    private static final String datasetFileName = "imdb.csv";
    public List<ImdbEntry> imdbData = new ArrayList<>(); // Make this private
    public HashMap<ImdbEntry.Genre, Double> weights = new HashMap<>(); // private

    /**
     * Constructor
     */
    public MovieRecommenderModel() {
        loadDataset();
        generateWeightVector();
        normalizeGenreAttributes();
    }

    /**
     * Read the IMDb dataset.
     */
    private void loadDataset() {
        String datasetDirectoryPath = System.getProperty("user.dir") + "/assets/datasets/";
        String delimiter = ",";
        String regex = "(?<!\\\\)" + Pattern.quote(delimiter);

        try (BufferedReader br = new BufferedReader(
                new FileReader(datasetDirectoryPath + datasetFileName))) {
            br.readLine(); // Discard the header.
            String line = br.readLine();
            while (line != null) {
                imdbData.add(new ImdbEntry(line.split(regex)));
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calculate a weight for each genre.
     */
    private void generateWeightVector() {
        // Initialize the weight vector.
        for (ImdbEntry.Genre genre : ImdbEntry.Genre.values()) {
            weights.put(genre, 0.0);
        }

        // Get the frequency for each genre.
        for (ImdbEntry entry : imdbData) {
            for (ImdbEntry.Genre genre : ImdbEntry.Genre.values()) {
                if (entry.genres.get(genre) > 0)
                    weights.put(genre, weights.get(genre) + 1);
            }
        }

        // Calculate weights.
        for (ImdbEntry.Genre genre : ImdbEntry.Genre.values()) {
            weights.put(genre, Math.log10(imdbData.size() / weights.get(genre)));
        }
    }

    private void normalizeGenreAttributes() {
        for (ImdbEntry entry : imdbData) {
            if (entry.numberOfGenres == 0)
                continue;
            for (ImdbEntry.Genre genre : ImdbEntry.Genre.values()) {
                if (entry.genres.get(genre) > 0)
                    entry.genres.put(genre, 1 / Math.sqrt(entry.numberOfGenres));
            }
        }
    }

    public ImdbEntry getEntryByTid(String tid) {
        for (ImdbEntry entry : imdbData) {
            if (entry.tid.equals(tid))
                return entry;
        }

        return null;
    }

    public ArrayList<ImdbEntry> getEntriesFromYear(int year){
        ArrayList<ImdbEntry> entries = new ArrayList<ImdbEntry>();
        for (ImdbEntry entry : imdbData){
            if (entry.year == year)
                entries.add(entry);
        }
        return entries;
    }

    // for test use only
    public String getTitleByTid(String tid) {
        for (ImdbEntry entry : imdbData) {
            if (Objects.equals(entry.tid, tid))
                return entry.title;
        }

        return "No title found";
    }

    // for test use only
    public String getRandomTitle() {
        Random rand = new Random();
        return imdbData.get(rand.nextInt(imdbData.size())).tid;
    }

    /**
     * Count the number of titles that have the same genres as the given title.
     * @param tid
     * @return
     */
    public int countTitlesWithSameGenres(String tid) {
        int count = 0;
        ImdbEntry imdbEntry = null;

        for (ImdbEntry entry : imdbData) {
            if (entry.tid.equals(tid))
                imdbEntry = entry;
        }

        boolean hasSameGenres;

        for (ImdbEntry entry : imdbData) {
            hasSameGenres = true;

            for (ImdbEntry.Genre genre : ImdbEntry.Genre.values()) {
                if (Double.compare(entry.genres.get(genre), imdbEntry.genres.get(genre)) != 0) {
                    hasSameGenres = false;
                    break;
                }
            }

            if(hasSameGenres)
                count++;
        }

        return count - 1;
    }
}
