package model;

import javafx.util.Pair;

import java.util.*;

/**
 * This class represents a user profile
 */
public class UserProfile {
    private static int userCount = 0;
    private int id;
    private final MovieRecommenderModel model;

    // Stores tid/action pairs.
    private HashMap<String, Integer> preferences = new HashMap<>();

    // Indicates how much the user likes each genre.
    private HashMap<ImdbEntry.Genre, Double> genreScores = new HashMap<>();

    // Indicates how likely the user likes each title.
    private List<Pair<String, Double>> titleScores;

    /**
     * Constructor
     * @param model mode that this user is associated with.
     */
    public UserProfile(MovieRecommenderModel model) {
        this.model = model;

        // Assign an ID.
        id = userCount;
        userCount++;

        // Initialize the preference mapping.
        for (ImdbEntry entry : model.imdbData)
            preferences.put(entry.tid, 0);

        // Initialize genre scores.
        for (ImdbEntry.Genre genre : ImdbEntry.Genre.values()) {
            genreScores.put(genre, 0.0);
        }
    }

    public void setPreference(String tid, int action) {
        preferences.put(tid, action);

        // Update the scores.
        generateGenreScores();
        generateTitleScores();
    }

    private void generateGenreScores() {
        // Reset the genre scores.
        for (ImdbEntry.Genre genre : ImdbEntry.Genre.values()) {
            genreScores.put(genre, 0.0);
        }

        // Calculate a score for each genre.
        for (ImdbEntry entry : model.imdbData) {
            for (ImdbEntry.Genre genre : ImdbEntry.Genre.values()) {
                genreScores.put(genre, genreScores.get(genre)
                        + (entry.genres.get(genre) * preferences.get(entry.tid)));
            }
        }
    }

    private void generateTitleScores() {
        // Initialize the score list.
        titleScores = new ArrayList<>();

        // Calculate a score for each movie.
        for (ImdbEntry entry : model.imdbData) {
            double score = 0;

            for (ImdbEntry.Genre genre : ImdbEntry.Genre.values()) {
                score += (entry.genres.get(genre) * model.weights.get(genre) * genreScores.get(genre));
            }

            titleScores.add(new Pair<String, Double>(entry.tid, score));
        }

        // Sort the list in descending order.
        Collections.sort(titleScores, Comparator.comparing(p -> -p.getValue()));
    }

    public List<Pair<String, Double>> getRecommendedTitles(int numberOfTitles) {
        List<Pair<String, Double>> recommendedTitles = new ArrayList<>();

        for (int i = 0; i < numberOfTitles; i++)
            recommendedTitles.add(titleScores.get(i));

        return recommendedTitles;
    }
}
