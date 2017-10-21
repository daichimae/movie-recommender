package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * This class represents an IMDb entry.
 */
public class ImdbEntry {
    public static enum Genre {
        ACTION, ADULT, ADVENTURE, ANIMATION, BIOGRAPHY, COMEDY, CRIME,
        DOCUMENTARY, DRAMA, FAMILY, FANTASY, FILMNOIR, GAMESHOW, HISTORY,
        HORROR, MUSIC, MUSICAL, MYSTERY, NEWS, REALITYTV, ROMANCE, SCIFI, SHORT,
        SPORT, TALKSHOW, THRILLER, WAR, WESTERN;
    }

    public String fn;
    public String tid;
    public String title;
    public List<String> wordsInTitle;
    public String url;
    public float imdbRating;
    public int ratingCount;
    public int duration;
    public int year;
    public String type;
    public int numberOfWins;
    public int numberOfNominations;
    public int numberOfPhotos;
    public int numberOfNewsArticles;
    public int numberOfUserReviews;
    public int numberOfGenres;
    public HashMap<Genre, Double> genres;

    public ImdbEntry(String[] entry) {
        fn = entry[0];
        tid = entry[1];
        title = entry[2];
        wordsInTitle = new ArrayList<>(Arrays.asList(entry[3].split(" ")));
        url = entry[4];
        imdbRating = entry[5].length() == 0 ? -1 : Float.parseFloat(entry[5]);
        ratingCount = entry[6].length() == 0 ? -1 : Integer.parseInt(entry[6]);
        duration = entry[7].length() == 0 ? -1 : Integer.parseInt(entry[7]);
        year = entry[8].length() == 0 ? -1 : Integer.parseInt(entry[8]);
        type = entry[9];
        numberOfWins = entry[10].length() == 0 ? -1 : Integer.parseInt(entry[10]);
        numberOfNominations = entry[11].length() == 0 ? -1 : Integer.parseInt(entry[11]);
        numberOfPhotos = entry[12].length() == 0 ? -1 : Integer.parseInt(entry[12]);
        numberOfNewsArticles = entry[13].length() == 0 ? -1 : Integer.parseInt(entry[13]);
        numberOfUserReviews = entry[14].length() == 0 ? -1 : Integer.parseInt(entry[14]);
        numberOfGenres = entry[15].length() == 0 ? -1 : Integer.parseInt(entry[15]);

        genres = new HashMap<>();
        int index = 16;
        for (Genre genre : Genre.values()) {
            genres.put(genre, entry[index].length() == 0 ? 0.0 : Double.parseDouble(entry[index]));
            index++;
        }
    }

    @Override
    public String toString() {
        List<Genre> genres = new ArrayList<Genre>();
        for (Genre genre : Genre.values()) {
            if (this.genres.get(genre) == 1)
                genres.add(genre);
        }

        return "Title: " + title + ", " + genres;
    }
}
