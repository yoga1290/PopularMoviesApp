package com.appspot.yoga1290.popularmoviesapp;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.appspot.yoga1290.popularmoviesapp.model.Movie;
import com.appspot.yoga1290.popularmoviesapp.model.Review;
import com.appspot.yoga1290.popularmoviesapp.model.Trailer;
import com.appspot.yoga1290.popularmoviesapp.sync.PostReviewSyncBroadcastReciever;
import com.appspot.yoga1290.popularmoviesapp.sync.SyncService;
import com.appspot.yoga1290.popularmoviesapp.tasks.FetchMovieReviews;
import com.appspot.yoga1290.popularmoviesapp.ui.ReviewListAdapter;
import com.appspot.yoga1290.popularmoviesapp.ui.TrailerListAdapter;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private Movie movie;
    private PostReviewSyncBroadcastReciever postReviewSyncBroadcastReciever = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        String movieId = getIntent().getExtras().getString(DetailFragment.MOVIE_ID);

        ((DetailFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_detail)).display(movieId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();
        // registered iff it's initialized
//        if(postReviewSyncBroadcastReciever != null) {
//            registerReceiver(postReviewSyncBroadcastReciever,
//                    new IntentFilter(getResources().getString(R.string.post_review_sync_broadcast_reciever)));
//        }
    }
    @Override
    protected void onPause() {
        super.onPause();
//        if(postReviewSyncBroadcastReciever != null) {
//            unregisterReceiver(postReviewSyncBroadcastReciever);
//        }
    }
}
