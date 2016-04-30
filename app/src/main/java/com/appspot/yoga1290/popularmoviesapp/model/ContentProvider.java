package com.appspot.yoga1290.popularmoviesapp.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import com.appspot.yoga1290.popularmoviesapp.model.*;

public class ContentProvider extends android.content.ContentProvider {

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private SQLiteDatabase database;
    private String DATABASE_NAME="yoga1290.popularMovies";
    private int DATABASE_VERSION=3
            ;

    /*
    Called when the database is created for the first time.
    */
    @Override
    public boolean onCreate() {
        SQLiteOpenHelper dbHelper=new SQLiteOpenHelper(getContext(),DATABASE_NAME, null,DATABASE_VERSION) {
            @Override
            public void onCreate(SQLiteDatabase sqLiteDatabase) {
                try {
                    sqLiteDatabase.execSQL(SettingsData.SQL_CREATE);
                    sqLiteDatabase.execSQL(Trailer.SQL_CREATE);
                    sqLiteDatabase.execSQL(Movie.SQL_CREATE);
                    sqLiteDatabase.execSQL(Review.SQL_CREATE);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
                sqLiteDatabase.execSQL(Movie.SQL_DROP);
                sqLiteDatabase.execSQL(SettingsData.SQL_DROP);
                sqLiteDatabase.execSQL(Trailer.SQL_DROP);
                sqLiteDatabase.execSQL(SettingsData.SQL_DROP);

                onCreate(sqLiteDatabase);
            }

            @Override
            public void onDowngrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
                sqLiteDatabase.execSQL(Movie.SQL_DROP);
                sqLiteDatabase.execSQL(SettingsData.SQL_DROP);
                sqLiteDatabase.execSQL(Trailer.SQL_DROP);
                sqLiteDatabase.execSQL(SettingsData.SQL_DROP);

                onCreate(sqLiteDatabase);
            }
        };
        database = dbHelper.getWritableDatabase();
        return (database != null);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        return database.query(uri.getLastPathSegment(),projection,selection,selectionArgs,null,null,sortOrder);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id=database.insertWithOnConflict(uri.getLastPathSegment(), null, values, SQLiteDatabase.CONFLICT_REPLACE);
        return Uri.parse(uri.toString()+"/"+id);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        return database.update(uri.getLastPathSegment(),values,selection,selectionArgs);
    }
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return database.delete(uri.getLastPathSegment(),selection,selectionArgs);
    }
}
