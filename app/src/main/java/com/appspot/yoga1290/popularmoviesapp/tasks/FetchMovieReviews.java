package com.appspot.yoga1290.popularmoviesapp.tasks;

import android.content.Context;
import android.nfc.Tag;
import android.util.Log;

import com.appspot.yoga1290.popularmoviesapp.Constants;
import com.appspot.yoga1290.popularmoviesapp.model.Movie;
import com.appspot.yoga1290.popularmoviesapp.model.Review;
import com.appspot.yoga1290.popularmoviesapp.model.SettingsData;
import com.appspot.yoga1290.popularmoviesapp.model.Trailer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by yoga1290 on 4/1/16.
 */
public class FetchMovieReviews {

    public static void fetchByMovieId(Context context, String movieId) {
        try {
            int pageOffset = 1, expectedRecordsPerPage = 20;

            SettingsData settingsData[] = SettingsData.get(context.getContentResolver());
            if(settingsData.length>0) {
                //TODO:
                int maxMovieRecords = settingsData[0].getMaxMovieRecords();

                pageOffset = settingsData[0].getTopRatedPageOffsetSync();
                // start w a new page next Sync:
                settingsData[0].setTopRatedPageOffsetSync(pageOffset + 1);
                settingsData[0].setContentResolver(context.getContentResolver());
                settingsData[0].save(context.getContentResolver());
            } else {
                SettingsData settings = new SettingsData(context.getContentResolver());
                settings.setTopRatedPageOffsetSync(2);
                settings.save(context.getContentResolver());
            }

//                https://api.themoviedb.org/3/movie/271110/reviews?api_key=e24792482bd990d914cb38bf110f1b79
            URL url = new URL("https://api.themoviedb.org/3/movie/" + movieId + "/reviews?api_key=" + Constants.API_KEY);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            if (buffer.length()>0) {

                Log.i(FetchMovieReviews.class.getSimpleName(), buffer.toString());

                JSONObject json = new JSONObject(buffer.toString());
                JSONArray results= json.getJSONArray("results");
                for (int i=0;i<results.length(); i++) {
                    Review review = new Review(results.getJSONObject(i), movieId);
                    review.save(context.getContentResolver());
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            Log.i(FetchMovieReviews.class.getSimpleName(), e.getLocalizedMessage());
        }
    }
}
