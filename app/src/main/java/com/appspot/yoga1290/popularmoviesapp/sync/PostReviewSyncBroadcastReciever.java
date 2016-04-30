package com.appspot.yoga1290.popularmoviesapp.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.appspot.yoga1290.popularmoviesapp.model.Movie;
import com.appspot.yoga1290.popularmoviesapp.ui.ReviewListAdapter;
import com.appspot.yoga1290.popularmoviesapp.model.Review;

/**
 * Created by yoga1290 on 4/1/16.
 */
public class PostReviewSyncBroadcastReciever extends BroadcastReceiver {

    private ReviewListAdapter reviewListAdapter;

    public PostReviewSyncBroadcastReciever(ReviewListAdapter reviewListAdapter) {
        this.reviewListAdapter = reviewListAdapter;
    }
    @Override
    public void onReceive(Context context, Intent intent) {

        String movieId = intent.getExtras().getString(SyncService.MOVIE_ID);
        reviewListAdapter.setReviews(
                Review.findByMovieId(context.getContentResolver(), movieId));
        Movie movie[] = Movie.findById(context.getContentResolver(), movieId);
        if(movie.length>0) {
            Toast.makeText(context, movie[0].getTitle() + " review updated", Toast.LENGTH_LONG).show();
        }
    }
}
