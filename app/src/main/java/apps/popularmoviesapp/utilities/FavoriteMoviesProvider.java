package apps.popularmoviesapp.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.google.gson.Gson;
import java.util.ArrayList;
import apps.popularmoviesapp.BuildConfig;
import apps.popularmoviesapp.models.FullMovie;
import apps.popularmoviesapp.models.Movie;

/**
 * Created by Kareem on 20/12/2016.
 */
public class FavoriteMoviesProvider {

    private ArrayList<FullMovie> result;

    public FavoriteMoviesProvider() {
    }

    public ArrayList<FullMovie> loadFavorites(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String favoritesString = sharedPreferences.getString(Constants.FAVORITES_PREFERENCE_KEY, null);

        if (favoritesString == null)
            return null;


        result = new ArrayList<FullMovie>();

        String[] splitter = favoritesString.split("\\|");

        for (String element : splitter) {
            if (!element.equals("")) {
                try {
                    Gson gson = new Gson();
                    FullMovie movie = gson.fromJson(element, FullMovie.class);
                    result.add(movie);
                } catch (Exception ex) {
                }
            }
        }
        return result;
    }

    public void addToFavorite(Context context, FullMovie movie) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String favoritesString = sharedPreferences.getString(Constants.FAVORITES_PREFERENCE_KEY, null);

        Gson gson = new Gson();
        String gsonStr = gson.toJson(movie);

        if (favoritesString == null) {
            favoritesString = gsonStr + "|";
        } else {
            favoritesString += gsonStr + "|";
        }

        saveSharedPreferences(context, favoritesString);
    }

    public void removeFromFavorite(Context context, FullMovie movie) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String favoritesString = sharedPreferences.getString(Constants.FAVORITES_PREFERENCE_KEY, null);

        Gson gson = new Gson();
        String gsonStr = gson.toJson(movie);

        if (favoritesString == null) {
            return;
        } else {
            favoritesString = favoritesString.replace(gsonStr, "");
        }
        saveSharedPreferences(context, favoritesString);
    }

    public boolean isFavorite(Context context, Movie movie) {

        ArrayList<FullMovie> tempResult = loadFavorites(context);
        if (tempResult != null) {
            for (FullMovie fullMovie : tempResult) {
                if(fullMovie.getMovie().equals( movie) )
                    return true;
            }
            return false;
        }
        return false;
    }

    private void saveSharedPreferences(Context context, String favorites) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(Constants.FAVORITES_PREFERENCE_KEY, favorites);
        editor.commit();
    }
}
