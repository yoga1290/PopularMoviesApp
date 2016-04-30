package com.appspot.yoga1290.popularmoviesapp.model;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import org.json.JSONException;
import org.json.JSONObject;

public class Trailer
{
    private ContentResolver contentResolver;
    public static final String
            TABLE_NAME="Trailer",
            DATABASE_NAME="yoga1290.popularMovies";
    public static final String SQL_CREATE="CREATE TABLE Trailer ( id TEXT PRIMARY KEY,movieId TEXT,key TEXT,name TEXT,site TEXT,type TEXT,size TEXT);";
    public static final String SQL_DROP="DROP TABLE IF EXISTS Trailer";

    public static final String PROJECTION[]=new String[]{"id"
            ,"movieId"
            ,"key"
            ,"name"
            ,"site"
            ,"type"
            ,"size"  };

    public static final String AUTHORITY = "com.appspot.yoga1290.popularmoviesapp";
    //TODO:getApplicationContext().getResources().getString(R.string.contentAuthority)?
    public static final Uri TrailerURI=Uri.parse("content://"+AUTHORITY+"/"+TABLE_NAME);


    private String id;
    private String movieId;
    private String key;
    private String name;
    private String site;
    private String type;
    private String size;

    public Trailer(ContentResolver contentResolver)
    {
        this.contentResolver=contentResolver;
    }
    public Trailer(JSONObject json) throws JSONException
    {
        this.id = json.getString("id");
        this.key = json.getString("key");
        this.name = json.getString("name");
        this.site = json.getString("site");
        this.type = json.getString("type");
        this.size = json.getString("size");
    }
    public Trailer(){
    }
    public static Trailer[] getFromCursor(Cursor c) {
        if(c==null) return new Trailer[]{};
        Trailer result[] = new Trailer[c.getCount()];
        for(int i=0;i<c.getCount();i++) {
            c.moveToNext();
            result[i] = new Trailer();

            result[i].id = c.getString(0);


            result[i].movieId = c.getString(1);
            result[i].key = c.getString(2);
            result[i].name = c.getString(3);
            result[i].site = c.getString(4);
            result[i].type = c.getString(5);
            result[i].size = c.getString(6);
        }
        return result;
    }


    public static Trailer[] findByMovieId(ContentResolver contentResolver, String movieId){
        return getFromCursor(contentResolver.query(
                TrailerURI,
                PROJECTION,
                "movieId =?",
                new String[]{movieId}
                , null));
    };


    public String save(ContentResolver contentResolver) {
        ContentValues cv=new ContentValues();

        cv.put("movieId", movieId );
        cv.put("id", id );

        cv.put("key", key );

        cv.put("name", name );

        cv.put("site", site );

        cv.put("type", type );

        cv.put("size", size );

        Uri id=contentResolver.insert(TrailerURI,cv);
        return id.getLastPathSegment();
    }


    public int deleteByMovieId(String movieId) {

        return contentResolver.delete(
                TrailerURI,
                "movieId =?",
                new String[]{ movieId });
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }
}
