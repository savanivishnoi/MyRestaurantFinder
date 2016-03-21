package cmpe.mobile.app.restaraunt.finder.restaurantfinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by saipranesh on 21-Mar-16.
 */
public class SearchResultsAdapter extends ArrayAdapter<SearchResults> {

    ArrayList<SearchResults> mSearchResults;
    int resource ;
    Context context;
    LayoutInflater view;

    public SearchResultsAdapter(Context context, int resource, ArrayList<SearchResults> searchResults) {
        super(context, resource, searchResults);
        mSearchResults = searchResults;
        this.resource = resource;
        this.context = context;
        view = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if(convertView == null) {
            convertView = view.inflate(resource,null);
            holder = new ViewHolder();

            holder.ratingsImage = (ImageView)convertView.findViewById(R.id.rating_image);
            holder.restaurantImage = (ImageView)convertView.findViewById(R.id.restaurant_image);
            holder.displayAddress = (TextView)convertView.findViewById(R.id.display_address);
            holder.restaurantName = (TextView)convertView.findViewById(R.id.restaurant_name);
            holder.displayCategories = (TextView)convertView.findViewById(R.id.categories);
            holder.reviewCount = (TextView)convertView.findViewById(R.id.review_count);

            convertView.setTag(holder);
        }else{
            holder  = (ViewHolder)convertView.getTag();

        }
        //holder.ratingsImage.setImageResource();
        holder.restaurantName.setText(mSearchResults.get(position).getName());

        return convertView;
    }

    static class ViewHolder{
        public ImageView restaurantImage;
        public ImageView ratingsImage;
        public TextView restaurantName;
        public TextView reviewCount;
        public TextView displayAddress;
        public TextView displayCategories;
    }
}
