package com.appspot.yoga1290.popularmoviesapp.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import com.appspot.yoga1290.popularmoviesapp.ui.GridAdapter;
import com.appspot.yoga1290.popularmoviesapp.model.Movie;

/**
 * Created by yoga1290 on 4/1/16.
 */
public class PostSyncBroadcastReciever extends BroadcastReceiver {

    private GridAdapter gridAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public PostSyncBroadcastReciever(GridAdapter gridAdapter,
                               SwipeRefreshLayout swipeRefreshLayout) {
        this.gridAdapter = gridAdapter;
        this.swipeRefreshLayout = swipeRefreshLayout;
    }
    @Override
    public void onReceive(Context context, Intent intent) {

        if(gridAdapter != null) {
            gridAdapter.setData(
                    Movie.getAll(context.getContentResolver()));
        }
        swipeRefreshLayout.setRefreshing(false);
        Toast.makeText(context, "Data updated", Toast.LENGTH_LONG).show();
    }
}
