package com.example.bookworm;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

    public static final String LOG_TAG = "MY-NAME";


    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    public static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * get Image bitmap
     */

    private static Bitmap getBitmap(String imageurl) throws IOException {

        String newurl = "https:" + imageurl.substring(5);
        Log.e(LOG_TAG, "getBitmap: " + imageurl);
        URL urlConnection = new URL(newurl);
        HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
        connection.setDoInput(true);
        connection.connect();
        InputStream input = connection.getInputStream();
        Bitmap myBitmap = BitmapFactory.decodeStream(input);
        return myBitmap;

    }

    /**
     * get authors from the json array
     */

    private static String getAuthors(JSONArray authorArray) throws JSONException {

        String Authors = null;
        StringBuilder temp = new StringBuilder();

        for (int i = 0; i < authorArray.length(); i++) {

            temp.append(authorArray.getString(i));
            temp.append(" ,");

        }

        Authors = temp.toString();
        return Authors;
    }


    /**
     * Query the Google Books API dataset and return a list of {@link BookObject} objects.
     */
    public static List<BookObject> fetchBookData(String requestUrl, String s, String preference) {
        // Create URL object
        URL url = createUrl(requestUrl, s, preference);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<BookObject> bookObjects = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link Earthquake}s
        return bookObjects;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    public static URL createUrl(String stringUrl, String s, String userpreference) {
        URL url = null;

        String prefrence = findprefrence(userpreference);

        if (prefrence.isEmpty()) {

            stringUrl = stringUrl + s + "&projection=lite" + "&startIndex=0&maxResults=12";

        } else {

            stringUrl = stringUrl + s + "+" + prefrence + "&projection=lite" + "&startIndex=0&maxResults=12";

        }

        Log.e(LOG_TAG, "createUrl: " + stringUrl);
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        Log.e(LOG_TAG, stringUrl);
        return url;
    }

    /**
     * to find the user preference for the search
     */
    private static String findprefrence(String userprefrence) {

        switch (userprefrence) {

            case "Title":
                return "intitle";

            case "Author":
                return "inauthor";

            case "Subject":
                return "subject";

            case "Publisher":
                return "inpublisher";

            default:
                return "";

        }

    }

    /**
     * Return a list of {@link BookObject} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<BookObject> extractFeatureFromJson(String BookJson) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(BookJson)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        List<BookObject> bookObjects = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(BookJson);

            // Extract the JSONArray associated with the key called "features",
            // which represents a list of features (or earthquakes).
            JSONArray BooksArray = baseJsonResponse.getJSONArray("items");

            // For each earthquake in the earthquakeArray, create an {@link Earthquake} object
            for (int i = 0; i < BooksArray.length(); i++) {

                // Get a single book at position i within the list of earthquakes
                JSONObject currentBook = BooksArray.getJSONObject(i);

                // from volume info get title, subtitle , authors array , publisher , publishdate , description , image links
                JSONObject VolumeInfo = currentBook.getJSONObject("volumeInfo");

                String title = null;

                JSONArray array = null;

                String authors = null;

                String publisher = null;

                String publishdate = null;

                String description = null;

                String subtitle = null;

                Bitmap imagethumbnail = null;

                try {

                    title = VolumeInfo.getString("title");

                    array = VolumeInfo.getJSONArray("authors");

                    authors = getAuthors(array);

                    imagethumbnail = getBitmap(VolumeInfo.getJSONObject("imageLinks").getString("smallThumbnail"));

                    description = VolumeInfo.getString("description");

                    publisher = VolumeInfo.getString("publisher");

                    subtitle = VolumeInfo.getString("subtitle");

                    publishdate = VolumeInfo.getString("publishedDate");

                } catch (JSONException e) {

                    Log.e(LOG_TAG, "extractFeatureFromJson: not present problem");
                } finally {

                    // Create a new {@link Earthquake} object with the magnitude, location, time,
                    // and url from the JSON response.
                    BookObject book = new BookObject(imagethumbnail, title, publisher, publishdate, description, authors, subtitle);

                    // Add the new {@link Earthquake} to the list of earthquakes.
                    bookObjects.add(book);

                }

            }

        } catch (JSONException | IOException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return bookObjects;
    }

}
