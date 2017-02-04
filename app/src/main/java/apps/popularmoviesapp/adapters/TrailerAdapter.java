package apps.popularmoviesapp.adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import apps.popularmoviesapp.BuildConfig;
import apps.popularmoviesapp.R;
import apps.popularmoviesapp.models.Trailer;
import apps.popularmoviesapp.utilities.Constants;

/**
 * Created by Kareem on 15/12/2016.
 */
public class TrailerAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<Trailer> result;
    private Holder holder;

    public TrailerAdapter(Context context, ArrayList<Trailer> result) {
        this.context = context;
        this.result = result;
    }

    public int getCount() {
        return result.size();
    }

    public Trailer getItem(int position) {
        return result.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView =  inflater.inflate(R.layout.trailers_list_item, null);
            holder = new Holder();
            holder.image = (ImageButton) convertView.findViewById(R.id.play_trailer);
            holder.name = (TextView) convertView.findViewById(R.id.trailer_name);

            convertView.setTag(holder);

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + result.get(position).getKey()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                    catch (ActivityNotFoundException ex) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.YOUTUBE_BASE_URL + "?v=" + result.get(position).getKey()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });
        }
        else {
            holder = (Holder) convertView.getTag();
        }

        holder.name.setText(result.get(position).getName());

        return convertView;
    }

    private static class Holder {
        ImageButton image;
        TextView name;
    }
}
