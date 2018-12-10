package com.fridayafternoon.campusspotlight;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends android.app.Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    DialogInterface.OnClickListener dialogClickListener;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    FirebaseStorage mStorage;
    StorageReference storageReference;
    String usersName;
    String email;



    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of a
     * this fragment using the provided parameters.
     *
     * @param displayName Parameter 1.
     * @param email Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    public static ProfileFragment newInstance(String displayName, String email) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, displayName);
        args.putString(ARG_PARAM2, email);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            usersName = getArguments().getString(ARG_PARAM1);
            email = getArguments().getString(ARG_PARAM2);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance();
        storageReference = mStorage.getReference();
        if (mAuth.getCurrentUser() != null) {
            usersName = mAuth.getCurrentUser().getDisplayName();
            email = mAuth.getCurrentUser().getEmail();
        }
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Button signOutButton = view.findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(this);

        //Displays Profile Name to Profile Page
        TextView profileName = view.findViewById(R.id.profileName);
        profileName.setText(usersName);
        Log.i("info", "onCreateView: " + usersName);

        //Displays Email to Profile Page
        TextView profileEmail = view.findViewById(R.id.profileEmail);
        profileEmail.setText(email);
        Log.i("info", "onCreateView: " + email);


        ImageView editButton = view.findViewById(R.id.editProfile);
        editButton.setOnClickListener(this);

        Button addEventButton = view.findViewById(R.id.addEventButton);
        addEventButton.setOnClickListener(this);

        // Inflate the layout for this fragment
        return view;


    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signOutButton:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(Html.fromHtml("<font color='#000000'>Are you sure?</font>"))
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        SharedPreferences prefs = getActivity().getSharedPreferences("info", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = prefs.edit();
                                        editor.putString("email", mAuth.getCurrentUser().getEmail());
                                        editor.commit();

                                        mAuth.signOut();
                                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No was clicked
                                        break;
                                }
                            }
                        });
                builder.create().show();


                break;
            case R.id.editProfile:
                Intent intent = new Intent(getActivity(), EditProfile.class);
                startActivity(intent);
                break;
            case R.id.addEventButton:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://events.uncc.edu/user/login?destination=node/add/event"));
                startActivity(browserIntent);
                break;

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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
