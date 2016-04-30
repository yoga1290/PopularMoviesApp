package com.appspot.yoga1290.popularmoviesapp.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.appspot.yoga1290.popularmoviesapp.R;
import com.appspot.yoga1290.popularmoviesapp.model.Movie;
import com.appspot.yoga1290.popularmoviesapp.model.SearchResult;
import com.squareup.picasso.Picasso;

public class  GridAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater li;
    private Movie movies[];
    public GridAdapter(Context context, Movie movies[]) {
        this.context = context;
        this.movies = movies;
        li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void setData(Movie movies[]) {
        this.movies = movies;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return movies.length;
    }

    @Override
    public Object getItem(int position) {
        return movies[position];
    }

    @Override
    public long getItemId(int position) {
        Log.d("GridAdapter", "getItemId" + position);
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String imageURL = "http://image.tmdb.org/t/p/w185/";
        imageURL += movies[position].getPoster_path();

        View v = li.inflate(R.layout.grid_item, null);
        ImageView imageView = (ImageView) v.findViewById(R.id.gridItemImageView);
        Picasso
                .with(context)
                .load(imageURL)
                .into(imageView);

        return v;
    }
}