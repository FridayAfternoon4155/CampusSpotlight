package com.fridayafternoon.campusspotlight;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fridayafternoon.campusspotlight.DashboardFragment.OnListFragmentInteractionListener;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Event} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class DashboardAdapter extends ArrayAdapter<Event> {

    private ArrayList<Event> events;
    private OnListFragmentInteractionListener mListener;

    public DashboardAdapter(Context context, int resource, ArrayList<Event> objects, DashboardFragment.OnListFragmentInteractionListener mListener) {
        super(context, resource, objects);
        this.mListener = mListener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public Event event;
        public TextView eventTitle;
        public TextView eventLocation;
        public TextView date_time;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            eventTitle = view.findViewById(R.id.eventTitle);
            eventLocation = view.findViewById(R.id.eventLocation);
            date_time = view.findViewById(R.id.date_time);

        }


    }
}
