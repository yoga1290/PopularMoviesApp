package com.appspot.yoga1290.popularmoviesapp.tasks;

import android.content.Context;
import android.util.Log;

import com.appspot.yoga1290.popularmoviesapp.Constants;
import com.appspot.yoga1290.popularmoviesapp.model.Movie;
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
public class FetchPopularMovies {

    public static void sync(Context context) {
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

//                https://api.themoviedb.org/3/movie/271110/videos?api_key=Constants.API_KEY
            URL url = new URL("https://api.themoviedb.org/3/movie/popular?api_key="+ Constants.API_KEY+"&page="+pageOffset);

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
                JSONObject json = new JSONObject(buffer.toString());
                JSONArray results= json.getJSONArray("results");
                for (int i=0;i<results.length(); i++) {
                    Movie movie = new Movie(results.getJSONObject(i));
                    movie.save(context.getContentResolver());
                    getTrailersByMovieId(context, movie.getId());

                    // cache poster image
//                    Picasso
//                            .with(context)
//                            .load("http://image.tmdb.org/t/p/w185/" + movie.getPoster_path());
//                    Picasso
//                            .with(context)
//                            .load("http://image.tmdb.org/t/p/w342/" + movie.getPoster_path());
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            Log.i(FetchPopularMovies.class.getSimpleName(), e.getLocalizedMessage());
        }
    }



    public static void getTrailersByMovieId(Context context, String movieId) {

//                https://api.themoviedb.org/3/movie/271110/videos?api_key=
        try{
            URL url = new URL("https://api.themoviedb.org/3/movie/"+movieId+"/videos?api_key=" + Constants.API_KEY);

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
                JSONObject json = new JSONObject(buffer.toString());
                JSONArray results= json.getJSONArray("results");
                for (int i=0;i<results.length(); i++) {
                    Trailer trailer = new Trailer(results.getJSONObject(i));
                    trailer.setMovieId(movieId);
                    trailer.save(context.getContentResolver());
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            Log.i(FetchPopularMovies.class.getSimpleName(), e.getLocalizedMessage());
        }
    }
}
