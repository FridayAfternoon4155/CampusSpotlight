package com.fridayafternoon.campusspotlight;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fridayafternoon.campusspotlight.HomeFragment.OnListFragmentInteractionListener;
import com.fridayafternoon.campusspotlight.dummy.DummyContent.DummyItem;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    Activity aContext;
    private final OnListFragmentInteractionListener mListener;
    ArrayList<Event> events = new ArrayList<>();


    public HomeAdapter(ArrayList<Event> items, HomeFragment.OnListFragmentInteractionListener listener) {
        events = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.event = events.get(position);
//        holder.mIdView.setText(events.get(position).id);
//        holder.mContentView.setText(events.get(position).content);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.event);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
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
