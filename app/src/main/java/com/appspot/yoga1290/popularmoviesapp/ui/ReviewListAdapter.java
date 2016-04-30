package com.appspot.yoga1290.popularmoviesapp.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appspot.yoga1290.popularmoviesapp.R;
import com.appspot.yoga1290.popularmoviesapp.model.Review;

/**
 * Created by yoga1290 on 4/2/16.
 */
public class ReviewListAdapter extends BaseAdapter {

    private Review reviews[];
    private LayoutInflater li;
    public Review[] getReviews() {
        return reviews;
    }
    public void setReviews(Review[] reviews) {
        this.reviews = reviews;
        this.notifyDataSetChanged();
    }

    public ReviewListAdapter(Context context, Review reviews[]) {
        this.reviews = reviews;
        li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return reviews.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout rootView = (RelativeLayout) li.inflate(R.layout.review_item, null);
        ((TextView) rootView.findViewById(R.id.review_author)).setText(reviews[position].getAuthor());
        ((TextView) rootView.findViewById(R.id.review_content)).setText(reviews[position].getContent());
        return rootView;
    }
}