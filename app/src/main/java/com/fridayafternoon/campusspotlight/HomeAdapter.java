package com.fridayafternoon.campusspotlight;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fridayafternoon.campusspotlight.HomeFragment.OnListFragmentInteractionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Event} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    Activity aContext;
    ArrayList<Event> events = new ArrayList<>();
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    public Event event;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public TextView eventTitle;
        public TextView eventLocation;
        public TextView date_time;


        public ViewHolder(final View itemView) {
            super(itemView);
            mView = itemView;
            eventTitle = itemView.findViewById(R.id.eventTitle);
            eventLocation = itemView.findViewById(R.id.eventLocation);
            date_time = itemView.findViewById(R.id.date_time);
        }
    }

    public HomeAdapter(Activity context, ArrayList<Event> events, SendData data) {
        this.aContext = context;
        this.events = events;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.ViewHolder holder, int position) {
        event = events.get(position);
        assert event != null;
        holder.eventTitle.setText(event.getTitle());
        holder.eventLocation.setText(event.getLocation());
        holder.date_time.setText(event.getDate());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public interface SendData {
        void sendEvent(Event event);
    }
}

