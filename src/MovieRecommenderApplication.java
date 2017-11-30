import javafx.util.Pair;
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
     * Test the Model. For test use only.
     * @param model model to be tested
     */
    static private void testModel(MovieRecommenderModel model) {
        // Create a user profile, like The matrix, American Beauty, dislike
        // Independence Day and show top 5 recommendations and their scores.
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

        // Create another user profile, randomly like or dislike 100 movies and
        // show the set of scores and count each score's occurrences.
        System.out.println("Set of scores:");
        Random random = new Random();
        UserProfile user2 = new UserProfile(model);
        // Randomly like or dislike 100 movies.
        for (int i = 0; i < 100; i++) {
            String tid = model.getRandomTitle();
            user2.setPreference(tid, random.nextBoolean() ? 1 : -1);
        }

        titles = user1.getRecommendedTitles(model.imdbData.size());
        double score = 1;
        int count = 0;
        for (Pair<String, Double> title : titles) {
            count++;
            if (score > title.getValue()) {
                score = title.getValue();
                System.out.println(score + " (" + count + ")");
                count = 0;
            }
        }
        System.out.println("------------------------------------------");

        // Randomly pick 100 titles and get the average count of the titles that
        // have the same genres as the picked titles.
        int totalCount = 0;
        for (int i = 0; i < 100; i++) {
            String tid = model.getRandomTitle();
            totalCount += model.countTitlesWithSameGenres(tid);
        }
        System.out.println("Average duplicate count: " + (totalCount / 100));
    }
}
