package com.example.myfair.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.myfair.activities.CardCreationActivity;
import com.example.myfair.R;
import com.example.myfair.activities.PacketCreationActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
  
    private Button btnLibrary;
    private ImageView createNewCard;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CreateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateFragment newInstance(String param1, String param2) {
        CreateFragment fragment = new CreateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Standard onCreate override. Finds needed handles and initializes view.
     * @param savedInstanceState App's saved instance state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /**
     * Standard onCreateView override. Finds needed handles and initializes the view
     * @param inflater - Inflater responsible for inflating the layout fragment.
     * @param container - ViewGroup associated with the inflater.
     * @param savedInstanceState - App's saved instance state
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create, container, false);

        FloatingActionButton createNewPacket = (FloatingActionButton) view.findViewById(R.id.fabPacket);
        createNewCard = (ImageView) view.findViewById(R.id.createFAB);

        createNewPacket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPacketCreation();
            }
        });

        createNewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCardCreation();
            }
        });

        return view;
    }

    /**
     * Opens Packet Creation Activity
     */
    private void openPacketCreation(){
        Intent intent = new Intent(getActivity(), PacketCreationActivity.class);
        startActivity(intent);
    }

    /**
     * Opens Card Creation Activity
     */
    private void openCardCreation() {
        Intent intent = new Intent(getActivity(), CardCreationActivity.class);
        startActivity(intent);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }

    }

    /**
     * Override of the onAttach method,
     * used to attach the listeners to the fragment.
     * */
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

    /**
     * Override of the onDetach method,
     * used to detach the listeners to the fragment.
     * */
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
}
