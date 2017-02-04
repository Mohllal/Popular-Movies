package apps.popularmoviesapp.models;

import java.util.ArrayList;

/**
 * Created by Kareem on 1/12/2016.
 */
public class TrailerResponse {
    private long id;

    private ArrayList<Trailer> results = new ArrayList<Trailer>();

    public ArrayList<Trailer> getResults() {
        return results;
    }

    public long getId() {
        return id;
    }

}
