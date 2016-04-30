package com.appspot.yoga1290.popularmoviesapp.ui;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appspot.yoga1290.popularmoviesapp.R;
import com.appspot.yoga1290.popularmoviesapp.model.Trailer;

/**
 * Created by yoga1290 on 4/2/16.
 */
public class TrailerListAdapter extends BaseAdapter {

    class TrailerOnClickListener implements Button.OnClickListener {

        private String id;
        private Context context;
        public TrailerOnClickListener(String id, Context context) {
            this.id = id;
            this.context = context;
        }
        @Override
        public void onClick(View v) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
                context.startActivity(intent);
            } catch (ActivityNotFoundException ex) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + id));
                context.startActivity(intent);
            }
        }
    }





    private Trailer trailers[];
    private LayoutInflater li;
    private Context context;
    public Trailer[] getTrailers() {
        return trailers;
    }
    public void setTrailers(Trailer[] trailers) {
        this.trailers = trailers;
        Log.i(TrailerListAdapter.class.getSimpleName(), "trailers.length = " + trailers.length);
        this.notifyDataSetChanged();
    }

    public TrailerListAdapter(Context context, Trailer trailers[]) {
        this.trailers = trailers;
        this.context = context;
        li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return trailers.length;
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
        //trailer_title
        LinearLayout rootView = (LinearLayout) li.inflate(R.layout.trailer_item,null);

        Button button = (Button) rootView.findViewById(R.id.button_trailer);
        button.setText(trailers[position].getName());
        button.setOnClickListener(new TrailerOnClickListener(trailers[position].getKey(), context));
        return rootView;
    }
}