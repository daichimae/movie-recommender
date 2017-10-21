import javafx.util.Pair;
import model.ImdbEntry;
import model.MovieRecommenderModel;
import model.UserProfile;

import java.util.List;
import java.util.Random;

/**
 * A movie recommender application.
 */
public class MovieRecommenderApplication {
    public static void main(String[] args) {
        MovieRecommenderModel model = new MovieRecommenderModel();
        testModel(model);
    }

    /**
     * Test the Model. For test use only and to be deleted.
     * @param model model to be tested
     */
    static private void testModel(MovieRecommenderModel model) {
        System.out.println(model.imdbData.get(146));
        System.out.println("------------------------------------------");

        // Test weight creation.
        for (ImdbEntry.Genre genre : ImdbEntry.Genre.values()) {
            System.out.println(genre + ": " + model.weights.get(genre));
        }
        System.out.println("------------------------------------------");

        // Test score calculation.
        UserProfile user1 = new UserProfile(model);
        user1.setPreference("tt0133093", 1); // Like The Matrix
        user1.setPreference("tt0169547", 1); // Like American Beauty
        user1.setPreference("tt0116629", -1); // Dislike Independence Day

        System.out.println("Recommended titles:");
        List<Pair<String, Double>> titles = user1.getRecommendedTitles(5);
        for (Pair<String, Double> title : titles) {
            System.out.println(model.getTitleByTid(title.getKey()) + " (" + title.getValue() + ")");
        }
        System.out.println("------------------------------------------");
        System.out.println("Set of scores:");
        Random random = new Random();
        UserProfile user2 = new UserProfile(model);
        // Randomly like or dislike 1000 movies.
        for (int i = 0; i < 1000; i++)
            user2.setPreference(model.getRandomTitle(), random.nextBoolean() ? 1 : -1);

        titles = user1.getRecommendedTitles(model.imdbData.size());
        double score = 1;
        for (Pair<String, Double> title : titles) {
            if (score > title.getValue()) {
                score = title.getValue();
                System.out.println(score);
            }
        }
    }
}
