package apps.popularmoviesapp.models;

import java.util.ArrayList;

/**
 * Created by Kareem on 1/12/2016.
 */
public class ReviewResponse {
    private long id;
    private long page;
    private ArrayList<Review> results = new ArrayList<Review>();
    private long totalPages;
    private long totalResults;

    public long getId() {
        return id;
    }

    public long getPage() {
        return page;
    }

    public ArrayList<Review> getResults() {
        return results;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public long getTotalResults() {
        return totalResults;
    }
}
