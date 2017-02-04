package apps.popularmoviesapp.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import apps.popularmoviesapp.R;
import apps.popularmoviesapp.models.Review;

/**
 * Created by Kareem on 15/12/2016.
 */
public class ReviewAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<Review> result;
    private Holder holder;
    private Review review;

    public ReviewAdapter(Context context, ArrayList<Review> result) {
        this.context = context;
        this.result = result;
    }

    public int getCount() {
        return result.size();
    }

    public Review getItem(int position) {
        return result.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        review = result.get(position);
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView =  inflater.inflate(R.layout.reviews_list_item, null);
            holder = new Holder();
            holder.author = (TextView) convertView.findViewById(R.id.author);
            holder.content = (TextView) convertView.findViewById(R.id.content);

            convertView.setTag(holder);

            holder.author.setText(review.getAuthor());
            holder.content.setText(Html.fromHtml(review.getContent()));
        }
        else {
            holder = (Holder) convertView.getTag();
        }

        return convertView;
    }

    private static class Holder {
        TextView author;
        TextView content;
    }
}
