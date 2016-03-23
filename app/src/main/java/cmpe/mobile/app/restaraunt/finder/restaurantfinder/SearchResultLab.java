package cmpe.mobile.app.restaraunt.finder.restaurantfinder;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by saipranesh on 22-Mar-16.
 */
public class SearchResultLab {

    private ArrayList<SearchResults> mSearchResults;

    private static SearchResultLab sSearchResultLab;
    private Context mAppContext;

    private SearchResultLab(Context appContext){
        mAppContext = appContext;
        mSearchResults = new ArrayList<>();
    }

    public static SearchResultLab getSearchResultLab(Context context){
        if(sSearchResultLab == null){
            sSearchResultLab = new SearchResultLab(context.getApplicationContext());
        }
        return sSearchResultLab;
    }

    public ArrayList<SearchResults> getSearchResults(){
        return mSearchResults;
    }

}
