package apps.popularmoviesapp.models;

import java.util.ArrayList;

/**
 * Created by Kareem on 15/11/2016.
 */
public class Movie {
    private String poster_path;
    private boolean adult;
    private String overview;
    private String release_date;
    private ArrayList<Long> genre_ids = new ArrayList<Long>();
    private long id;
    private String original_title;
    private String title;
    private String backdrop_path;
    private double popularity;
    private int runtime;
    private int vote_count;
    private boolean video;
    private double vote_average;

    public String getPoster_path() {
        return poster_path;
    }

    public boolean isAdult() {
        return adult;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public ArrayList<Long> getGenre_ids() {
        return genre_ids;
    }

    public long getId() {
        return id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getTitle() {
        return title;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public double getPopularity() {
        return popularity;
    }

    public int getVote_count() {
        return vote_count;
    }

    public boolean isVideo() {
        return video;
    }

    public double getVote_average() {
        return vote_average;
    }

    public int getRuntime() {
        return runtime;
    }

    public boolean equals(Movie movie){
        return (this.id == movie.getId());
    }
}
