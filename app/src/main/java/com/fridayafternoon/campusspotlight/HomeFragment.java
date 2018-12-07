package com.fridayafternoon.campusspotlight;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class HomeFragment extends android.app.Fragment implements View.OnClickListener {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    ArrayList<Event> events = new ArrayList<>();
    Activity context = getActivity();
    HomeAdapter adapter;
    ListView eventList;
    String TAG = "info";


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HomeFragment() {
    }

    public static HomeFragment newInstance(int columnCount) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            events = getArguments().getParcelableArrayList("events");
            Log.i("info", "onCreate: array size: " + events.size());
        } else {

        }
        Log.i("info", "onCreate: array size" + events.size());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        View eventItem = inflater.inflate(R.layout.event_item, container, false);

        adapter = new HomeAdapter(getContext(), R.layout.activity_main, events, mListener);
        Log.i("info", "//=== VIEW IS NULL: " + Boolean.toString(view==null));
        eventList = view.findViewById(R.id.listViewHome);
        eventList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(events.get(position).getLink()));
                Log.i(TAG, "onItemClick: event position link" + events.get(position).getLink());
                startActivity(browserIntent);
            }
        });

        ImageButton pinButton = eventItem.findViewById(R.id.GoingButton);
        pinButton.setOnClickListener(this);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }


    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.GoingButton:
                Toast.makeText(context, "Clicked Going Button", Toast.LENGTH_SHORT).show();
        }


    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Event event);
    }

}
