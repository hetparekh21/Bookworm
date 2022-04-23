package com.example.bookworm;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements androidx.loader.app.LoaderManager.LoaderCallbacks<List<BookObject>> {

    private static final String TAG = "hehe";
    private static final String LOG_TAG = "NON";

    private BookAdapter mAdapter;

    String url = "https://www.googleapis.com/books/v1/volumes?q=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the {@link ListView} in the layout
        GridView BookListView = (GridView) findViewById(R.id.listview);

        BookListView.setEmptyView(findViewById(R.id.no_items));

        // Create a new adapter that takes an empty list of earthquakes as input
        ArrayList<BookObject> books= new ArrayList<BookObject>();
        mAdapter = new BookAdapter(this,books);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        BookListView.setAdapter(mAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected earthquake.
        BookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                BookObject bookObject = books.get(position);

                // Create a new intent to open the {@link NumbersActivity}
                Intent intent = new Intent(getApplicationContext(),BookDetailView.class);

                Bundle b = new Bundle();

                b.putParcelable("thumbnail",bookObject.getBookThumbnail());
                b.putString("title",bookObject.getTitle());
                b.putString("publisher",bookObject.getPublisher());
                b.putString("publishdate",bookObject.getPublishedDate());
                b.putString("description",bookObject.getDescription());
                b.putString("subtitle",bookObject.getSubtitle());
                b.putString("author",bookObject.getAuthors());

                intent.putExtras(b) ;
                // Start the new activity
                startActivity(intent);

            }
        });

        // you need to have a list of data that you want the spinner to display
        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("Search By >");
        spinnerArray.add("Title");
        spinnerArray.add("Author");
        spinnerArray.add("Subject");
        spinnerArray.add("Publisher");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = (Spinner) findViewById(R.id.spinner);
        sItems.setAdapter(adapter);
        ///////////////////////////////////////////////////////////////////////////////

        androidx.appcompat.widget.SearchView searchView = findViewById(R.id.search_bar);

        searchView.setSubmitButtonEnabled(true);

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {

                // Get a reference to the LoaderManager, in order to interact with loaders.
                LoaderManager loaderManager = getSupportLoaderManager();
                loaderManager.destroyLoader(0);

                TextView empty_view = (TextView) findViewById(R.id.no_items);
                empty_view.setText("");

                findViewById(R.id.progress_circular).setVisibility(View.VISIBLE);

                mAdapter.clear();
                // Get a reference to the ConnectivityManager to check state of network connectivity
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);

                // Get details on the currently active default data network
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                // If there is a network connection, fetch data
                if (networkInfo != null && networkInfo.isConnected()) {

                    // Initialize the loader. Pass in the int ID constant defined above and pass in null for
                    // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
                    // because this activity implements the LoaderCallbacks interface).
                    Log.e(TAG, "calling the init manager");
                    loaderManager.initLoader(0, null, MainActivity.this).forceLoad();

                } else {

                    findViewById(R.id.progress_circular).setVisibility(View.GONE);

                    findViewById(R.id.no_internet).setVisibility(View.VISIBLE);

                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }

        });

    }

    @NonNull
    @Override
    public Loader<List<BookObject>> onCreateLoader(int id, @Nullable Bundle args) {

        androidx.appcompat.widget.SearchView searchView1 =  findViewById(R.id.search_bar);
        String s = searchView1.getQuery().toString();

        Spinner sItems = (Spinner) findViewById(R.id.spinner);
        String searchPrefrence = sItems.getSelectedItem().toString();

        Log.e(TAG, "Inside onCreateLoader");
        return new BookLoader(this, url, s, searchPrefrence);

    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<BookObject>> loader, List<BookObject> data) {

        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        findViewById(R.id.progress_circular).setVisibility(View.GONE);

        Log.e(TAG, "Inside onLoadFinished: ");

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }

        TextView empty_view = (TextView) findViewById(R.id.no_items);
        empty_view.setText("No Items Found");

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<BookObject>> loader) {

        Log.e(TAG, "Inside onLoaderReset: ");

        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();

    }

}