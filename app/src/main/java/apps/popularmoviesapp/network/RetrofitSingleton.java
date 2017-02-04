package apps.popularmoviesapp.network;


import apps.popularmoviesapp.BuildConfig;
import apps.popularmoviesapp.utilities.Constants;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Kareem on 26/11/2015.
 */
public class RetrofitSingleton {
    private static Retrofit retrofit;
    private RetrofitSingleton(){}

    public static Retrofit newInstance() {
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
