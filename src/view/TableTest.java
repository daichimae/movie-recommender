package view;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Pair;
import model.ImdbEntry;
import model.MovieRecommenderModel;
import model.UserProfile;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TableTest {

    @FXML
    private TextField filterField;
    @FXML
    private Button nextButton;
    @FXML
    private Button backButton;
    @FXML
    private Label selectedMovies;
    @FXML
    private TableView<ImdbEntry> selectedTable;
    @FXML
    private TableView<ImdbEntry> movieTable;
    @FXML
    private TableColumn<ImdbEntry, String> likedColumn;
    @FXML
    private TableColumn<ImdbEntry, String> titleColumn;
    @FXML
    private TableColumn<ImdbEntry, String> ratingColumn;
    @FXML
    private TableColumn<ImdbEntry, String> yearColumn;

    private Set<ImdbEntry> likedMovies = new HashSet<>();
    private Set<ImdbEntry> dislikedMovies = new HashSet<>();


    private ObservableList<ImdbEntry> allMovies = FXCollections.observableArrayList();
    private ObservableList<ImdbEntry> selectedMoviesList = FXCollections.observableArrayList();

    private MovieRecommenderModel model;

    /**
     * Just add some sample data in the constructor.
     */
    public TableTest() {
        model = new MovieRecommenderModel();
        ArrayList<ImdbEntry> movies = model.getEntriesFromYear(2010);
        for (ImdbEntry m : model.imdbData){
            allMovies.add(m);
        }

    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     *
     * Initializes the table columns and sets up sorting and filtering.
     */
    @FXML
    private void initialize() {
        // 0. Initialize the columns.
        likedColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().title));
        titleColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().title));
        yearColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(String.valueOf(cellData.getValue().year)));
        ratingColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(String.valueOf(cellData.getValue().imdbRating)));

        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<ImdbEntry> filteredData = new FilteredList<>(allMovies, p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(movie -> {
                // If filter text is empty, display all movies.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (movie.title.toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<ImdbEntry> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(movieTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        movieTable.setItems(sortedData);

        // Add a movie to liked table when double clicked
        movieTable.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent e){
                if (e.getClickCount() == 2){
                    if (!selectedMoviesList.contains(movieTable.getSelectionModel().getSelectedItem())){
                        selectedMoviesList.add(movieTable.getSelectionModel().getSelectedItem());
                        selectedTable.setItems(selectedMoviesList);
                    }
                }
            }
        });

        selectedTable.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent e){
                if (e.getClickCount() == 2){
                    selectedMoviesList.remove(selectedTable.getSelectionModel().getSelectedItem());
                    selectedTable.setItems(selectedMoviesList);
                }
            }
        });
        nextButton.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent e){
                if (selectedMovies.getText().equals("Liked Movies")){
                    //save off liked movies, reset table and start doing disliked
                    selectedMovies.setText("Disliked Movies");
                    selectedMovies.setTextFill(Color.RED);
                    likedMovies.addAll(selectedMoviesList);
                    selectedMoviesList.clear();
                    selectedMoviesList.addAll(dislikedMovies);
                    selectedTable.setItems(selectedMoviesList);
                    nextButton.setText("Finish");
                    backButton.setVisible(true);
                }
                else{
                    dislikedMovies.addAll(selectedMoviesList);
                    UserProfile curUser = new UserProfile(model);
                    for (ImdbEntry movie : likedMovies){
                        curUser.setPreference(movie.tid, 1);
                    }
                    for (ImdbEntry movie : dislikedMovies){
                        curUser.setPreference(movie.tid, -1);
                    }
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setTitle("Results");
                    a.setHeaderText("Recommended Movies");
                    String results = "Results:\n";
                    for (Pair<String, Double> tidScores : curUser.getRecommendedTitles(5)){
                        String rating = new DecimalFormat("##.#").format(tidScores.getValue() * 100) + '%';
                        results += model.getTitleByTid(tidScores.getKey()) + ": " + rating + "\n";
                    }
                    a.setContentText(results);
                    //a.setContentText("liked: " + likedMovies + "\ndisliked: " + dislikedMovies);
                    a.show();
                }
            }
        });
        // go back to liked stuff, repopulate list with previously liked movies
        backButton.setOnMouseClicked(new EventHandler<MouseEvent>(){
           @Override
           public void handle(MouseEvent e){
               dislikedMovies.addAll(selectedMoviesList);
               nextButton.setText("Next");
               selectedMovies.setText("Liked Movies");
               selectedMovies.setTextFill(Paint.valueOf("#37eb00"));
               selectedMoviesList.clear();
               selectedMoviesList.addAll(likedMovies);
               selectedTable.setItems(selectedMoviesList);
               backButton.setVisible(false);
           }
        });
    }
}