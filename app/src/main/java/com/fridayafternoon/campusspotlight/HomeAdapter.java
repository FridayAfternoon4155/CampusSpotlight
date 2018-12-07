package com.fridayafternoon.campusspotlight;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fridayafternoon.campusspotlight.HomeFragment.OnListFragmentInteractionListener;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Event} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class HomeAdapter extends ArrayAdapter<Event> {
    Activity aContext;
    private final OnListFragmentInteractionListener mListener;
    ArrayList<Event> events = new ArrayList<>();
    public View mView;
    public Event event;
    public TextView eventTitle;
    public TextView eventLocation;
    public TextView date_time;

    public HomeAdapter(Context context, int resource, ArrayList<Event> objects, OnListFragmentInteractionListener mListener) {
        super(context, resource, objects);
        this.mListener = mListener;
        this.events = objects;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Event event = getItem(position);

        convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_item, parent, false);

        mView = convertView;
        eventTitle = convertView.findViewById(R.id.eventTitle);
        eventLocation = convertView.findViewById(R.id.eventLocation);
        date_time = convertView.findViewById(R.id.date_time);

        assert event != null;
        eventTitle.setText(event.getTitle());
        eventLocation.setText(event.getLocation());
        date_time.setText(event.getDate());

        return convertView;
    }
}

