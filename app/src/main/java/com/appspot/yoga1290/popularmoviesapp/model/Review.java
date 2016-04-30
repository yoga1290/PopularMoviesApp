package com.appspot.yoga1290.popularmoviesapp.model;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import org.json.JSONException;
import org.json.JSONObject;

public class Review
{
    private ContentResolver contentResolver;
    public static final int

            VIDEOID = 1,

    AUTHOR = 2,

    CONTENT = 3,

    URL = 4,

    ID=0;
    public static final String
            TABLE_NAME="Review",
            DATABASE_NAME="yoga1290.popularMovies";
    public static final String SQL_CREATE="CREATE TABLE Review ( id TEXT PRIMARY KEY,movieId TEXT,author TEXT,content TEXT,url TEXT);";
    public static final String SQL_DROP="DROP TABLE IF EXISTS Review";

    public static final String PROJECTION[]=new String[]{"Id"
            ,"movieId"
            ,"author"
            ,"content"
            ,"url"  };

    public static final String AUTHORITY = "com.appspot.yoga1290.popularmoviesapp";
    //TODO:getApplicationContext().getResources().getString(R.string.contentAuthority)?
    public static final Uri ReviewURI=Uri.parse("content://"+AUTHORITY+"/"+TABLE_NAME);


    private String id;
    private String movieId;
    private String author;
    private String content;
    private String url;

    public Review(ContentResolver contentResolver)
    {
        this.contentResolver=contentResolver;
    }
    public Review(JSONObject json, String movieId) throws JSONException
    {

        this.movieId = movieId;
        this.author = json.getString("author");
        this.content = json.getString("content");
        this.url = json.getString("url");
    }
    public Review(){
    }
    public static Review[] getFromCursor(Cursor c) {

        Review result[] = new Review[c.getCount()];
        for(int i=0;i<c.getCount();i++) {
            c.moveToNext();
            result[i] = new Review();

            result[i].id = c.getString(0);
            result[i].movieId = c.getString(1);
            result[i].author = c.getString(2);
            result[i].content = c.getString(3);
            result[i].url = c.getString(4);
        }
        return result;
    }


    public static Review[] findByMovieId(ContentResolver contentResolver, String movieId){
        return getFromCursor(contentResolver.query(
                ReviewURI,
                PROJECTION,
                "movieId =?",
                new String[]{ movieId }
                ,null));
    };


    public String save(ContentResolver contentResolver) {
        ContentValues cv=new ContentValues();

        cv.put("id", id );
        cv.put("movieId", movieId );

        cv.put("author", author );

        cv.put("content", content );
        cv.put("url", url );

        Uri id=contentResolver.insert(ReviewURI,cv);
        return id.getLastPathSegment();
    }
    public static int deleteByMovieId(ContentResolver contentResolver, String movieId) {

        return contentResolver.delete(
                ReviewURI,
                "movieId =?",
                new String[]{ movieId });
    }

    public String getAuthor() {
        return author;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }
}
