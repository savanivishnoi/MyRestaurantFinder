package cmpe.mobile.app.restaraunt.finder.restaurantfinder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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
        this.context = context.getApplicationContext();
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

        new DownloadImageTask(holder.restaurantImage).execute(mSearchResults.get(position).getImageUrl());
        new DownloadImageTask(holder.ratingsImage).execute(mSearchResults.get(position).getRatingImgUrl());
        holder.restaurantName.setText(mSearchResults.get(position).getName());
        holder.reviewCount.setText(mSearchResults.get(position).getReviewCount());

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

    private class DownloadImageTask extends AsyncTask<String, Void , Bitmap>{

        ImageView mImageView;

        private DownloadImageTask(ImageView imageView){
            mImageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String inputUrl = urls[0];
            Bitmap imageIcon =  null;

            try {
                InputStream in = new URL(inputUrl).openStream();
                imageIcon = BitmapFactory.decodeStream(in);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return imageIcon;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            mImageView.setImageBitmap(bitmap);
        }
    }
}
