package apps.popularmoviesapp.models;

import java.util.ArrayList;

/**
 * Created by Kareem on 20/02/2016.
 */
public class FullMovie {
    private Movie movie;
    private TrailerResponse trailers;
    private ReviewResponse reviews;

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public TrailerResponse getTrailers() {
        return trailers;
    }

    public void setTrailers(TrailerResponse trailers) {
        this.trailers = trailers;
    }

    public ReviewResponse getReviews() {
        return reviews;
    }

    public void setReviews(ReviewResponse reviews) {
        this.reviews = reviews;
    }
}
