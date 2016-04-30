package com.appspot.yoga1290.popularmoviesapp;

import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
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

public class DetailFragment extends Fragment {

    public static final String MOVIE_ID = "MOVIE_ID";
    private static final String TAG = DetailFragment.class.getSimpleName();


    private Movie movie;
    private ScrollView rootView;
    private PostReviewSyncBroadcastReciever postReviewSyncBroadcastReciever = null;

    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment newInstance(String movieId) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(DetailFragment.MOVIE_ID, movieId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(DetailFragment.MOVIE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        rootView = (ScrollView) inflater.inflate(R.layout.fragment_detail, container, false);

        // If inside the DetailActivity activity, it will handle the rest
        if (getArguments() == null) {
            return rootView;
        } else {
            display(getArguments().getString(DetailFragment.MOVIE_ID));
        }

        return rootView;
    }

    public void display(String movieId) {
        try {

//            String movieId = getArguments().getString(DetailFragment.MOVIE_ID);
            Log.i(TAG, "onCreateView:" + movieId);
            Movie movieById[] = Movie.findById(getActivity().getContentResolver(), movieId);


            if(movieById.length>0)
                movie = movieById[0];



            ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);

            ((TextView) rootView.findViewById(R.id.movie_title)).setText(movie.getTitle());

            ((TextView) rootView.findViewById(R.id.movie_date))
                    .setText(movie.getRelease_date().split("-")[0]);

            ((TextView) rootView.findViewById(R.id.textView_vote))
                    .setText(movie.getVote_average() + "");

            ((TextView) rootView.findViewById(R.id.description)).setText(movie.getOverview());


            Button favoriteButton = ((Button) rootView.findViewById(R.id.button_favorite));
            if(movie.isFavorite()) {
                favoriteButton.setBackgroundColor(Color.GREEN);
            } else {
                favoriteButton.setBackgroundColor(Color.GRAY);
            }

            favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    movie.setIsFavorite(!movie.isFavorite());
                    movie.save(getActivity().getContentResolver());

                    if(movie.isFavorite()) {
                        ((Button) v).setBackgroundColor(Color.GREEN);
                    } else {
                        ((Button) v).setBackgroundColor(Color.GRAY);
                    }

                }
            });



            Picasso
                    .with(getContext())
                    .load("http://image.tmdb.org/t/p/w342/" + movie.getPoster_path())
                    .into(imageView);

            // init trailer list
            TrailerListAdapter trailerListAdapter = new TrailerListAdapter(getContext(),
                    Trailer.findByMovieId(getActivity().getContentResolver(), movie.getId()));
            ListView trailersListView = (ListView)rootView.findViewById(R.id.listView_trailers);
            trailersListView.setAdapter(trailerListAdapter);

            // init review list
            ReviewListAdapter reviewListAdapter = new ReviewListAdapter(getContext(),
                    Review.findByMovieId(getActivity().getContentResolver(), movie.getId()));
            ListView reviewListView = (ListView)rootView.findViewById(R.id.listView_reviews);
            reviewListView.setAdapter(reviewListAdapter);

            // init Broadcast reciever:
            postReviewSyncBroadcastReciever = new PostReviewSyncBroadcastReciever(reviewListAdapter);
            getActivity().registerReceiver(postReviewSyncBroadcastReciever,
                    new IntentFilter(getResources().getString(R.string.post_review_sync_broadcast_reciever)));


            // Sync Reviews for this movieId:
            Bundle syncBundle = new Bundle();
            syncBundle.putString(SyncService.TASK_NAME, FetchMovieReviews.class.getSimpleName());
            syncBundle.putString(SyncService.MOVIE_ID, movie.getId());
            SyncService.SyncNOW(getContext(), syncBundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(postReviewSyncBroadcastReciever != null) {
            context.registerReceiver(postReviewSyncBroadcastReciever,
                    new IntentFilter(getResources().getString(R.string.post_review_sync_broadcast_reciever)));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(postReviewSyncBroadcastReciever != null) {
            getActivity().unregisterReceiver(postReviewSyncBroadcastReciever);
        }
    }

}
