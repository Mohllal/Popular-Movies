package apps.popularmoviesapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import apps.popularmoviesapp.BuildConfig;
import apps.popularmoviesapp.R;
import apps.popularmoviesapp.models.Movie;
import apps.popularmoviesapp.utilities.Constants;

/**
 * Created by Kareem on 27/11/2016.
 */
public class MoviesAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<Movie> result;
    private Holder holder;

    public MoviesAdapter(Context context, ArrayList<Movie> result) {
        this.context = context;
        this.result = result;
    }

    public int getCount() {
        return result.size();
    }

    public Movie getItem(int position) {
        return result.get(position);
    }

    public long getItemId(int position) {
        return result.get(position).getId();
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView =  inflater.inflate(R.layout.grid_item, null);
            holder = new Holder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.grid_row);
            convertView.setTag(holder);
        }
        else {
            holder = (Holder) convertView.getTag();
        }

        Picasso.with(context).load(Constants.IMAGE_BASE_URL + result.get(position).
                getPoster_path()).fit().placeholder(R.drawable.placeholder).error(R.drawable.placeholder)
                .into(holder.imageView);


        return convertView;
    }

    private static class Holder{
        ImageView imageView;
    }
}
