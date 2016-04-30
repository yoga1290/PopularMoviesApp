package com.appspot.yoga1290.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.appspot.yoga1290.popularmoviesapp.model.Movie;
import com.appspot.yoga1290.popularmoviesapp.model.SettingsData;
import com.appspot.yoga1290.popularmoviesapp.sync.PostSyncBroadcastReciever;
import com.appspot.yoga1290.popularmoviesapp.sync.SyncService;
import com.appspot.yoga1290.popularmoviesapp.tasks.FetchPopularMovies;
import com.appspot.yoga1290.popularmoviesapp.tasks.FetchTopRatedMovies;
import com.appspot.yoga1290.popularmoviesapp.ui.GridAdapter;
import com.appspot.yoga1290.popularmoviesapp.ui.OnMenuItemClickListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MainFragment extends Fragment implements OnMenuItemClickListener{


    public MainFragment() {
        // Required empty public constructor
    }

    private GridAdapter gridAdapter;
    private PostSyncBroadcastReciever postSyncBroadcastReciever = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SwipeRefreshLayout rootView
                = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.gridView);


        // Load cached movies and trigger Sync:
        Movie movies[]= new Movie[]{};
        try{
            movies = Movie.getAll(getActivity().getContentResolver());
            if(movies.length==0) {
                String taskName = FetchTopRatedMovies.class.getSimpleName();

                SettingsData settingsData[] = SettingsData.get(getActivity().getContentResolver());
                if(settingsData.length>0) {
                    taskName = settingsData[0].getSortBy() == 0 ? FetchTopRatedMovies.class.getSimpleName() : FetchPopularMovies.class.getSimpleName();
                }
                Bundle syncBundle = new Bundle();
                syncBundle.putString(SyncService.TASK_NAME, taskName);
                SyncService.SyncNOW(getContext(), syncBundle);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        gridAdapter = new GridAdapter(getContext(), movies);

        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MainFragment.OnFragmentInteractionListener) getActivity())
                        .onMovieClick(((Movie)gridAdapter.getItem(position)));
            }
        });




        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) rootView;//.findViewById(R.id.activity_main_swipe_refresh_layout);

        postSyncBroadcastReciever = new PostSyncBroadcastReciever(gridAdapter, swipeRefreshLayout);//, getContentResolver());
        getActivity().registerReceiver(postSyncBroadcastReciever,
                new IntentFilter(getResources().getString(R.string.post_sync_broadcast_reciever)));

        swipeRefreshLayout.setKeepScreenOn(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Bundle syncBundle = new Bundle();
                String taskName = FetchTopRatedMovies.class.getSimpleName();
                SettingsData settingsData[] = SettingsData.get(getActivity().getContentResolver());
                if(settingsData.length>0) {
                    taskName = settingsData[0].getSortBy() == 0 ? FetchTopRatedMovies.class.getSimpleName() : FetchPopularMovies.class.getSimpleName();
                }
                syncBundle.putString(SyncService.TASK_NAME, taskName);
                SyncService.SyncNOW(getContext(), syncBundle);
            }
        });


        return rootView;
    }





    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(postSyncBroadcastReciever != null) {
            context.registerReceiver(postSyncBroadcastReciever,
                    new IntentFilter(getResources().getString(R.string.post_sync_broadcast_reciever)));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(postSyncBroadcastReciever != null) {
            getActivity().unregisterReceiver(postSyncBroadcastReciever);
        }
    }

    @Override
    public void OnMenuItemClick(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sortByPopular) {
            gridAdapter.setData(
                    Movie.getAllOrderBy(getActivity().getContentResolver(), "popularity")
            );
        }
        if (id == R.id.action_sortByTopRated) {
            gridAdapter.setData(
                    Movie.getAllOrderBy(getActivity().getContentResolver(), "vote_count")
            );
        }
        if(id == R.id.action_favorites) {
            gridAdapter.setData(
                    Movie.findByIsFavorite(getActivity().getContentResolver())
            );
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onMovieClick(Movie movie);
    }

}
