package cmpe.mobile.app.restaraunt.finder.restaurantfinder;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchActivityFragment extends Fragment {

    ListView listView;
    SearchResultsAdapter mSearchResultsAdapter;
    ArrayList<SearchResults> mSearchResults;
    public static final String QUERY_URL = "SearchActivityFragment.QUERY_URL";
    public SearchActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = (String)getArguments().getString(QUERY_URL) + "radius_filter=17000&limit=20&";
        SearchAsyncTask task = new SearchAsyncTask();
        task.execute(url);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View searchView = inflater.inflate(R.layout.fragment_search,container,false);

        listView = (ListView)searchView.findViewById(R.id.searchList);
        mSearchResults = new ArrayList<>();

        return searchView;
    }

    public static SearchActivityFragment getUrl(String url){
        Bundle args = new Bundle();
        args.putString(QUERY_URL,url);

        SearchActivityFragment searchActivityFragment = new SearchActivityFragment();
        searchActivityFragment.setArguments(args);

        return searchActivityFragment;
    }

    public class SearchAsyncTask extends AsyncTask<String ,Void , Boolean> {

        @Override
        protected Boolean doInBackground(String... url) {

            String CONSUMER_KEY = "by4s6bw5RrnHgff5hqRZ-g";
            String CONSUMER_SECRET = "DIKfyKsz8aa5COaG8Lky4usv2u0";
            String ACCESS_TOKEN = "rwLWR5I7w0PVMoGJasOu3xAYWFpwNPsH";
            String TOKEN_SECRET = "ZGESy3izFg8hbxalnKSoHv1bfGc";

            OAuthConsumer consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY,
                    CONSUMER_SECRET);
            consumer.setTokenWithSecret(ACCESS_TOKEN, TOKEN_SECRET);


            // create an HTTP request to a protected resource

            String requestUrl = String.valueOf(url[0]);
            Log.i("OAuthTestURL", requestUrl);
            HttpGet request = new HttpGet("https://api.yelp.com/v2/search?" + requestUrl);



            try {
                // sign the request
                consumer.sign(request);
                Log.i("URL_used",request.getURI().toString());
                HttpClient httpClient = new DefaultHttpClient();
                HttpResponse response = httpClient.execute(request);
                int responseStatus = response.getStatusLine().getStatusCode();
                Log.i("OauthTest", String.valueOf(response.getStatusLine()));
                if(responseStatus == 200) {
                    String json_string = EntityUtils.toString(response.getEntity());
                    JSONObject jsonObject = new JSONObject(json_string);
                    Log.i("OauthTest", String.valueOf(response.getStatusLine()));
                    Log.i("OauthTest",jsonObject.toString());
                    JSONArray jsonArray =jsonObject.getJSONArray("businesses");

                    for(int i = 0 ;i < jsonArray.length(); i ++){
                        SearchResults searchResults = new SearchResults();
                        JSONObject requiredObject =  jsonArray.getJSONObject(i);
                        searchResults.setName(requiredObject.getString("name"));
                        //searchResults.setDisplayAddress(createList(requiredObject.getJSONArray("display_address")));
                        //searchResults.setCategories(createList(requiredObject.getJSONArray("categories")));
                        searchResults.setImageUrl(requiredObject.getString("image_url").replace('\\',' ').trim());
                        searchResults.setReviewCount(requiredObject.getInt("review_count"));
                        searchResults.setRatingImgUrl(requiredObject.getString("rating_img_url").replace('\\',' ').trim());
                        searchResults.setSnippetText(requiredObject.getString("snippet_text"));

                        mSearchResults.add(searchResults);
                    }
                  return true;

                }

            }catch (OAuthMessageSignerException e) {
                e.printStackTrace();
            } catch (OAuthExpectationFailedException e) {
                e.printStackTrace();
            } catch (OAuthCommunicationException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return false;
        }

        private List<String> createList(JSONArray jsonArray){
            if(jsonArray.length() > 0){
                ArrayList<String> requiredList = new ArrayList<>();
                for(int i = 0 ;i < jsonArray.length(); i ++){
                    try {
                        requiredList.add(jsonArray.get(i).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
            return null;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if(result.equals(true)){
                Log.i("Testing_mSearchResults",mSearchResults.get(1).getName());
                SearchResultsAdapter searchResultsAdapter = new SearchResultsAdapter
                        (getActivity(),R.layout.search_result_row, mSearchResults );
            }else{
                Toast.makeText(getContext(),"No results to show", Toast.LENGTH_SHORT).show();
            }

        }
    }

}
