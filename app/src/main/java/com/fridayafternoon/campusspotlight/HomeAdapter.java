package com.fridayafternoon.campusspotlight;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fridayafternoon.campusspotlight.HomeFragment.OnListFragmentInteractionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Event} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    Activity aContext;
    ArrayList<Event> events = new ArrayList<>();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public Event event;
    SendData data;



    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public TextView eventTitle;
        public TextView eventLocation;
        public TextView date_time;
        public ImageButton pinItem;


        public ViewHolder(final View itemView) {
            super(itemView);
            mView = itemView;
            eventTitle = itemView.findViewById(R.id.eventTitle);
            eventLocation = itemView.findViewById(R.id.eventLocation);
            date_time = itemView.findViewById(R.id.date_time);
            pinItem = itemView.findViewById(R.id.GoingButton);

        }
    }

    public HomeAdapter(Activity context, ArrayList<Event> events, SendData data) {
        this.aContext = context;
        this.events = events;
        this.data = data;
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

        holder.pinItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(aContext, "Pinned Item", Toast.LENGTH_SHORT).show();
                event.setUser(mAuth.getCurrentUser().toString());
                data.addEvent(event);

            }
        });

        holder.eventTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(aContext, "Clicked Title", Toast.LENGTH_SHORT).show();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(events.get(position).getLink()));
                aContext.startActivity(browserIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public interface SendData {
        void deleteEvent(Event event);
        void addEvent(Event event);
    }

}

