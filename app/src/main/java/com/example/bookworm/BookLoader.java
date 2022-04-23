package com.example.bookworm;

import android.content.Context;
import android.util.Log;

import java.util.List;

public class BookLoader extends androidx.loader.content.AsyncTaskLoader<List<BookObject>> {

    private String mUrl;
    private static final String TAG = "hehe";
    private String s ;
    private String userprefrences ;

    /**
     * @param context
     * @deprecated
     */
    public BookLoader(Context context , String url ,String s , String userprefrence) {
        super(context);
        mUrl = url;
        this.s = s ;
        this.userprefrences = userprefrence ;
    }

    @Override
    public List<BookObject> loadInBackground() {

        Log.e(TAG, "Inside loadInBackground (retrieving earthquake data)" );

        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.

        return QueryUtils.fetchBookData(mUrl , s, userprefrences);

    }
}
