package com.appspot.yoga1290.popularmoviesapp.model;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import org.json.JSONException;
import org.json.JSONObject;

public class Movie
{
    private ContentResolver contentResolver;
    public static final String
            TABLE_NAME="Movie",
            DATABASE_NAME="yoga1290.popularMovies";
    public static final String SQL_CREATE="CREATE TABLE Movie ( id INTEGER PRIMARY KEY, poster_path TEXT,overview TEXT,release_date TEXT,genre_ids TEXT,original_title TEXT,original_language TEXT,title TEXT,backdrop_path TEXT,popularity REAL,vote_average REAL,vote_count INTEGER,is_favorite INTEGER);";
    public static final String SQL_DROP="DROP TABLE IF EXISTS Movie";

    public static final String PROJECTION[]=new String[]{"id"
            ,"poster_path"
            ,"overview"
            ,"release_date"
            ,"genre_ids"
            ,"original_title"
            ,"original_language"
            ,"title"
            ,"backdrop_path"
            ,"popularity"
            ,"vote_average"
            ,"vote_count"
            ,"is_favorite"};

    public static final String AUTHORITY = "com.appspot.yoga1290.popularmoviesapp";
    //TODO:getApplicationContext().getResources().getString(R.string.contentAuthority)?

    public static final Uri MovieURI=Uri.parse("content://"+AUTHORITY+"/"+TABLE_NAME);


    private String poster_path;
    private String overview;
    private String id;
    private String release_date;
    private String genre_ids;
    private String original_title;
    private String original_language;
    private String title;
    private String backdrop_path;
    private int vote_count;
    private double popularity;
    private double vote_average;
    private boolean isFavorite;

    private String jsonString;
    public Movie(ContentResolver contentResolver)
    {
        this.contentResolver=contentResolver;
    }
    public Movie(JSONObject json) throws JSONException
    {
        this.id = json.getString("id");

        this.poster_path = json.getString("poster_path");
        this.overview = json.getString("overview");
        this.release_date = json.getString("release_date");
        this.genre_ids = json.getString("genre_ids");
        this.original_title = json.getString("original_title");
        this.original_language = json.getString("original_language");
        this.title = json.getString("title");
        this.backdrop_path = json.getString("backdrop_path");
        this.vote_count = json.getInt("vote_count");
        this.popularity = json.getDouble("popularity");
        this.vote_average = json.getDouble("vote_average");
        this.isFavorite = false;

        this.jsonString = json.toString();
    }
    public Movie(){
    }
    public static Movie[] getFromCursor(Cursor c) {
        if(c==null) return new Movie[]{};
        Movie result[] = new Movie[c.getCount()];
        for(int i=0;i<c.getCount();i++) {
            c.moveToNext();
            result[i] = new Movie();

            result[i].id = c.getString(0);
            result[i].poster_path = c.getString(1);
            result[i].overview = c.getString(2);
            result[i].release_date = c.getString(3);
            result[i].genre_ids = c.getString(4);
            result[i].original_title = c.getString(5);
            result[i].original_language = c.getString(6);
            result[i].title = c.getString(7);
            result[i].backdrop_path = c.getString(8);
            result[i].vote_count = c.getInt(11);
            result[i].popularity = c.getDouble(9);
            result[i].vote_average = c.getDouble(10);

            result[i].isFavorite = (c.getInt(11) == 1);
        }
        return result;
    }

    public static Movie[] getAll(ContentResolver contentResolver) {
        return getFromCursor(contentResolver.query(
                MovieURI,
                PROJECTION,null,null
                , null));
    }
    public static Movie[] getAllOrderBy(ContentResolver contentResolver, String orderBy) {
        return getFromCursor(contentResolver.query(
                MovieURI,
                PROJECTION,null,null
                , orderBy));
    }
    public static Movie[] findById(ContentResolver contentResolver, String id) {
        return getFromCursor(contentResolver.query(
                MovieURI,
                PROJECTION,
                "id = ?",
                new String[]{id}
                , null));
    }
    public static Movie[] findByIsFavorite(ContentResolver contentResolver) {
        return getFromCursor(contentResolver.query(
                MovieURI,
                PROJECTION,
                "is_favorite = ?",
                new String[]{"1"}
                , null));
    }


    public String save(ContentResolver contentResolver) {
        ContentValues cv=new ContentValues();

        cv.put("id", id );
        cv.put("poster_path", poster_path );

        cv.put("overview", overview );

        cv.put("release_date", release_date );

        cv.put("genre_ids", genre_ids );

        cv.put("original_title", original_title );

        cv.put("original_language", original_language );

        cv.put("title", title );

        cv.put("backdrop_path", backdrop_path );

        cv.put("popularity", popularity );

        cv.put("vote_average", vote_average );

        cv.put("vote_count", vote_count );

        cv.put("is_favorite", isFavorite ? 1:0);

        Uri id=contentResolver.insert(MovieURI,cv);
        return id.getLastPathSegment();
    }

    public void setContentResolver(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }


    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getGenre_ids() {
        return genre_ids;
    }


    public static Uri getMovieURI() {
        return MovieURI;
    }


    public String getOriginal_language() {
        return original_language;
    }


    public String getOriginal_title() {
        return original_title;
    }


    public String getOverview() {
        return overview;
    }

    public double getPopularity() {
        return popularity;
    }


    public String getPoster_path() {
        return poster_path;
    }

    public static String[] getPROJECTION() {
        return PROJECTION;
    }


    public String getRelease_date() {
        return release_date;
    }

    public static String getSqlCreate() {
        return SQL_CREATE;
    }

    public static String getSqlDrop() {
        return SQL_DROP;
    }

    public static String getTableName() {
        return TABLE_NAME;
    }


    public String getTitle() {
        return title;
    }

    public double getVote_average() {
        return vote_average;
    }


    public int getVote_count() {
        return vote_count;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return this.jsonString;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }
}
