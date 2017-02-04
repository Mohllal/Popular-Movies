package apps.popularmoviesapp.network;

import apps.popularmoviesapp.BuildConfig;
import apps.popularmoviesapp.models.FullResponse;
import apps.popularmoviesapp.models.Movie;
import apps.popularmoviesapp.models.ReviewResponse;
import apps.popularmoviesapp.models.TrailerResponse;
import apps.popularmoviesapp.utilities.Constants;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Kareem on 26/11/2015.
 */
public interface Request {
    @GET("discover/movie?api_key=" + Constants.API_KEY)
    public Call<FullResponse> getFullResponse(@Query("sort_by") String sort_by, @Query("page")long page);

    @GET("movie/{id}?api_key=" + Constants.API_KEY)
    Call<Movie> getMovie(@Path("id") long id);

    @GET("movie/{id}/videos?api_key=" + Constants.API_KEY)
    public Call<TrailerResponse> getTrailer(@Path("id") long id);

    @GET("movie/{id}/reviews?api_key=" + Constants.API_KEY)
    public Call<ReviewResponse> getReview(@Path("id") long id);

}
