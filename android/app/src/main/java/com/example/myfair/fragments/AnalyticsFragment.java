package com.example.myfair.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myfair.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AnalyticsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AnalyticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnalyticsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;


    private OnFragmentInteractionListener mListener;

    public AnalyticsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AnalyticsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AnalyticsFragment newInstance(String param1, String param2) {
        AnalyticsFragment fragment = new AnalyticsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        //Construction for later Displaying in main fragment for a future iteration
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_analytics, container, false);

        CardView usersCard = v.findViewById(R.id.ownedCard);
        usersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Push to Expanded View
            }
        });

        CardView scannedCard = v.findViewById(R.id.scannedCard);
        scannedCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Push to Expanded View
            }
        });

        CardView historyCard = v.findViewById(R.id.historyCard);
        historyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Push to Expanded view
            }
        });

        CardView graphCard = v.findViewById(R.id.graphCard);
        graphCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Push to Expanded View
            }
        });
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
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

    /**
     * This interface must be implemented by com.example.myfair.activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other com.example.myfair.fragments contained in that
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


    //Statistics Suite

    //Number of Cards Scanned
    
    //User Card with most scans

    //Graphing. Most likely using GraphView OpenSource Graph Plotting

    //Show most recent card scanned and a timestamp
}
