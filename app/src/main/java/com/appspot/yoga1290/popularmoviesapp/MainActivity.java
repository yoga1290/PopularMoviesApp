package com.appspot.yoga1290.popularmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.appspot.yoga1290.popularmoviesapp.model.Movie;
import com.appspot.yoga1290.popularmoviesapp.ui.OnMenuItemClickListener;

public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener {

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.detail_container) != null) {
            mTwoPane = true;
        } else {
            mTwoPane = false;
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        OnMenuItemClickListener mListener = (OnMenuItemClickListener) getSupportFragmentManager().findFragmentById(R.id.fragment_main);
        mListener.OnMenuItemClick(item);

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMovieClick(Movie movie) {


        if(!mTwoPane) {
            startActivity(
                    new Intent(this, DetailActivity.class)
                            .putExtra(DetailFragment.MOVIE_ID, movie.getId()));
        } else {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.

            DetailFragment fragment = new DetailFragment();
            Bundle args = new Bundle();
            args.putString(DetailFragment.MOVIE_ID, movie.getId());
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_container, fragment, "DetailFragment")
                    .commit();
        }

    }
}
