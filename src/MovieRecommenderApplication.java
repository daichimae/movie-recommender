import model.MovieRecommenderModel;

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
        System.out.println(model.movies.get(146));
    }
}
