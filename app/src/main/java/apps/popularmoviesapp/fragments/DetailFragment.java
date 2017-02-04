package apps.popularmoviesapp.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import apps.popularmoviesapp.adapters.ReviewAdapter;
import apps.popularmoviesapp.adapters.TrailerAdapter;
import apps.popularmoviesapp.BuildConfig;
import apps.popularmoviesapp.R;
import apps.popularmoviesapp.models.FullMovie;
import apps.popularmoviesapp.models.Movie;
import apps.popularmoviesapp.models.Review;
import apps.popularmoviesapp.models.ReviewResponse;
import apps.popularmoviesapp.models.Trailer;
import apps.popularmoviesapp.models.TrailerResponse;
import apps.popularmoviesapp.network.Request;
import apps.popularmoviesapp.network.RetrofitSingleton;
import apps.popularmoviesapp.utilities.Constants;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import apps.popularmoviesapp.utilities.FavoriteMoviesProvider;

/**
 * Created by Kareem on 10/12/2016.
 */
public class DetailFragment extends Fragment implements ListView.OnScrollListener, ListView.OnTouchListener, ImageButton.OnClickListener{

    private View rootView;
    private ImageButton imageButton;
    private ImageView imageView;
    private ListView trailersListView;
    private ListView reviewsListView;
    private ScrollView scrollView;


    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;

    private ArrayList<Trailer> trailerResult = new ArrayList<Trailer>();
    private ArrayList<Review> reviewResult = new ArrayList<Review>();

    private Context context;
    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    private FullMovie fullMovieModel = new FullMovie();

    private Movie movieModel;
    private long movie_id;

    private FavoriteMoviesProvider favoriteMoviesProvider;

    boolean isFavorite = false;
    private String movieStr = null;
    private String trailerStr = null;
    private String reviewStr = null;

    public DetailFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this.getActivity();
        favoriteMoviesProvider = new FavoriteMoviesProvider();

        Bundle bundle = getArguments();

        movie_id = bundle.getLong("movie_id");
        isFavorite = bundle.getBoolean("isFavorite");
        movieStr = bundle.getString("movie");
        trailerStr = bundle.getString("trailer");
        reviewStr = bundle.getString("review");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        trailersListView = (ListView) rootView.findViewById(R.id.trailers_list);
        reviewsListView = (ListView) rootView.findViewById(R.id.reviews_list);
        imageView = (ImageView) rootView.findViewById(R.id.movie_poster);
        imageButton = (ImageButton) rootView.findViewById(R.id.favourite_btn);
        scrollView = (ScrollView) rootView.findViewById(R.id.main_scrollview);

        imageButton.setOnClickListener(this);
        reviewsListView.setOnScrollListener(this);
        reviewsListView.setOnTouchListener(this);

        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings && !isTablet()) {
            FragmentManager fragmentManager = this.getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_fragment, new SettingsFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Get favorite movies from sharedPreferences in offline mode
        if (isFavorite) {
            Gson gson = new Gson();
            Movie movie = gson.fromJson(movieStr, Movie.class);
            TrailerResponse trailerResponse = gson.fromJson(trailerStr, TrailerResponse.class);
            ReviewResponse reviewResponse = gson.fromJson(reviewStr, ReviewResponse.class);

            fullMovieModel.setMovie(movie);
            fullMovieModel.setTrailers(trailerResponse);
            fullMovieModel.setReviews(reviewResponse);

            movieModel = movie;
            fillMovieLayout(movie);

            gg(trailerResponse.getResults(), reviewResponse.getResults());

        }
        else {
            getMovieDetails();
            getMovieTrailers();
            getMovieReviews();
        }
    }

    @Override
    public void onClick(View v) {
        ImageButton image_btn = (ImageButton) v;
        image_btn.setSelected(!image_btn.isSelected());

        //Marked as favorite
        if (image_btn.isSelected()) {
            favoriteMoviesProvider.addToFavorite(getActivity().getApplicationContext(), fullMovieModel);
            Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT)
                    .show();

        }
        //Marked as non-favorite
        else {
            favoriteMoviesProvider.removeFromFavorite(getActivity().getApplicationContext(), fullMovieModel);
            Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (listAtTop(reviewsListView) && scrollState == SCROLL_STATE_IDLE)
            ((ScrollView) view.getParent().getParent()).pageScroll(View.FOCUS_UP);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {}

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // Disallow the touch request for parent scroll on touch of child view
        v.getParent().requestDisallowInterceptTouchEvent(true);
        return false;
    }

    //Fetch movie details from the tmdb api
    private void getMovieDetails() {
        Request request = RetrofitSingleton.newInstance().create(Request.class);
        Call<Movie> getMovieCall = request.getMovie(movie_id);
        getMovieCall.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Response<Movie> response, Retrofit retrofit) {
                movieModel = response.body();
                fullMovieModel.setMovie(movieModel);
                fillMovieLayout(movieModel);
            }

            @Override
            public void onFailure(Throwable t) {

                Toast.makeText(context, "Check your internet connection!", Toast.LENGTH_LONG).show();
                Log.e(LOG_TAG, "onFailure() Failure has occurred in requesting a movie with id: " + movie_id + " from Retrofit", t);
            }
        });

    }

    //Fetch movie trailers from the tmdb api
    private void getMovieTrailers() {
        Request request = RetrofitSingleton.newInstance().create(Request.class);
        Call<TrailerResponse> getMovieCall = request.getTrailer(movie_id);
        getMovieCall.enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Response<TrailerResponse> response, Retrofit retrofit) {
                trailerResult = response.body().getResults();
                TrailerResponse trailerResponse = response.body();
                fullMovieModel.setTrailers(trailerResponse);
                trailerAdapter = new TrailerAdapter(getActivity(), trailerResult);
                trailersListView.setAdapter(trailerAdapter);
                setDynamicListViewHeight(trailersListView);
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(context, "Check your internet connection!", Toast.LENGTH_LONG).show();
                Log.e(LOG_TAG, "onFailure() Failure has occurred in requesting trailers of a movie with id: " + movie_id + " from Retrofit", t);
            }
        });
    }

    private void gg(ArrayList<Trailer> trailers, ArrayList<Review> reviews){
        reviewAdapter = new ReviewAdapter(getActivity(), reviews);
        reviewsListView.setAdapter(reviewAdapter);
        setDynamicListViewHeight(reviewsListView);

        trailerAdapter = new TrailerAdapter(getActivity(), trailers);
        trailersListView.setAdapter(trailerAdapter);
        setDynamicListViewHeight(trailersListView);
    }

    //Fetch movie reviews from the tmdb api
    private void getMovieReviews() {
        Request request = RetrofitSingleton.newInstance().create(Request.class);
        Call<ReviewResponse> getMovieCall = request.getReview(movie_id);
        getMovieCall.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Response<ReviewResponse> response, Retrofit retrofit) {
                reviewResult = response.body().getResults();
                ReviewResponse reviewResponse = response.body();
                fullMovieModel.setReviews(reviewResponse);
                reviewAdapter = new ReviewAdapter(getActivity(), reviewResult);
                reviewsListView.setAdapter(reviewAdapter);
                setDynamicListViewHeight(reviewsListView);
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(context, "Check your internet connection!", Toast.LENGTH_LONG).show();
                Log.e(LOG_TAG, "onFailure() Failure has occurred in requesting reviews of a movie with id: " + movie_id + " from Retrofit", t);
            }
        });
    }

    //fill layout with the fetched information
    private void fillMovieLayout(Movie movieModel) {
        TextView movieTitle = (TextView) rootView.findViewById(R.id.movie_title);
        movieTitle.setText(movieModel.getTitle());

        boolean favorite = favoriteMoviesProvider.isFavorite(context, movieModel);
        imageButton.setSelected(favorite);

        Picasso.with(getActivity().getApplicationContext())
                .load(Constants.IMAGE_BASE_URL + movieModel.getPoster_path())
                .placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(imageView);

        DateFormat formatter = new SimpleDateFormat("yy-mm-dd");
        TextView movieYear = (TextView) rootView.findViewById(R.id.movie_year);
        try {
            Date date = formatter.parse(movieModel.getRelease_date());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            movieYear.setText(Integer.toString(calendar.get(Calendar.YEAR)));
        } catch (ParseException ex) {
        }


        TextView movieDuration = (TextView) rootView.findViewById(R.id.movie_duration);
        movieDuration.setText(String.format("%1$dmin", movieModel.getRuntime()));

        TextView movieRating = (TextView) rootView.findViewById(R.id.movie_rating);
        movieRating.setText(String.format("%1$.2f/10", movieModel.getVote_average()));

        TextView movieOverview = (TextView) rootView.findViewById(R.id.movie_overview);
        movieOverview.setText(movieModel.getOverview());
        scrollView.setVisibility(View.VISIBLE);
    }

    private boolean listAtTop(ListView listView) {
        if (listView.getChildCount() == 0) return true;
        return listView.getChildAt(0).getTop() == 0;
    }

    //Set list view height based on its children
    private boolean setDynamicListViewHeight(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }
    }

    private boolean isTablet() {
        return (this.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

}
