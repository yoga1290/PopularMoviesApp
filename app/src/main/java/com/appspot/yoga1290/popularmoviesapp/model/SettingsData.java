package com.appspot.yoga1290.popularmoviesapp.model;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import org.json.JSONException;
import org.json.JSONObject;

public class SettingsData
{
    private ContentResolver contentResolver;
    public static final String
            TABLE_NAME="SettingsData",
            DATABASE_NAME="yoga1290.popularMovies";
    public static final String SQL_CREATE="CREATE TABLE SettingsData ( id INTEGER PRIMARY KEY,TopRatedPageOffsetSync INTEGER,PopularPageOffsetSync INTEGER,MaxMovieRecords INTEGER,MaxReviewRecords INTEGER, SortBy INTEGER);";
    public static final String SQL_DROP="DROP TABLE IF EXISTS SettingsData";

    public static final String PROJECTION[]=new String[]{"id"
            ,"TopRatedPageOffsetSync"
            ,"PopularPageOffsetSync"
            ,"MaxMovieRecords"
            ,"MaxReviewRecords"
            ,"SortBy"};

    public static final String AUTHORITY = "com.appspot.yoga1290.popularmoviesapp";
    //TODO:getApplicationContext().getResources().getString(R.string.contentAuthority)?
    public static final Uri SettingsDataURI=Uri.parse("content://"+AUTHORITY+"/"+TABLE_NAME);


    private int TopRatedPageOffsetSync;
    private int PopularPageOffsetSync;
    private int MaxMovieRecords;
    private int MaxReviewRecords;
    private int SortBy = 0;

    public SettingsData(ContentResolver contentResolver)
    {
        this.contentResolver=contentResolver;
    }
    public SettingsData(JSONObject json) throws JSONException
    {
        this.TopRatedPageOffsetSync = json.getInt("TopRatedPageOffsetSync");
        this.PopularPageOffsetSync = json.getInt("PopularPageOffsetSync");
        this.MaxMovieRecords = json.getInt("MaxMovieRecords");
        this.MaxReviewRecords = json.getInt("MaxReviewRecords");
    }
    public SettingsData(){
    }
    public static SettingsData[] getFromCursor(Cursor c) {
        if(c==null) return new SettingsData[]{};

        SettingsData result[] = new SettingsData[c.getCount()];
        for(int i=0;i<c.getCount();i++) {
            c.moveToNext();
            result[i] = new SettingsData();

            result[i].TopRatedPageOffsetSync = c.getInt(1);
            result[i].PopularPageOffsetSync = c.getInt(2);
            result[i].MaxMovieRecords = c.getInt(3);
            result[i].MaxReviewRecords = c.getInt(4);
            result[i].SortBy = c.getInt(5);
        }
        return result;
    }

    public void setContentResolver(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public static SettingsData[] get(ContentResolver contentResolver){
        try {
            return getFromCursor(contentResolver.query(
                    SettingsDataURI,
                    PROJECTION,
                    "id = ?",
                    new String[]{"1"}
                    , null));
        }catch(Exception e){}
        return new SettingsData[]{};
    };


    public String save(ContentResolver contentResolver) {
        ContentValues cv=new ContentValues();

        cv.put("id", "1");
        cv.put("TopRatedPageOffsetSync", TopRatedPageOffsetSync );

        cv.put("PopularPageOffsetSync", PopularPageOffsetSync );

        cv.put("MaxMovieRecords", MaxMovieRecords );

        cv.put("MaxReviewRecords", MaxReviewRecords );

        cv.put("SortBy", SortBy );

        Uri id=contentResolver.insert(SettingsDataURI,cv);
        return id.getLastPathSegment();
    }


    public int getMaxMovieRecords() {
        return MaxMovieRecords;
    }


    public int getMaxReviewRecords() {
        return MaxReviewRecords;
    }


    public int getPopularPageOffsetSync() {
        return PopularPageOffsetSync;
    }

    public static String getSqlDrop() {
        return SQL_DROP;
    }


    public int getTopRatedPageOffsetSync() {
        return TopRatedPageOffsetSync;
    }

    public void setMaxMovieRecords(int maxMovieRecords) {
        MaxMovieRecords = maxMovieRecords;
    }

    public void setMaxReviewRecords(int maxReviewRecords) {
        MaxReviewRecords = maxReviewRecords;
    }

    public void setPopularPageOffsetSync(int popularPageOffsetSync) {
        PopularPageOffsetSync = popularPageOffsetSync;
    }

    public void setTopRatedPageOffsetSync(int topRatedPageOffsetSync) {
        TopRatedPageOffsetSync = topRatedPageOffsetSync;
    }

    public int getSortBy() {
        return SortBy;
    }

    public void setSortBy(int sortBy) {
        SortBy = sortBy;
    }
}
