package com.example.myfair.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.myfair.activities.CardViewingActivity;
import com.example.myfair.activities.LoginActivity;
import com.example.myfair.activities.ProfileCreationActivity;
import com.example.myfair.R;
import com.example.myfair.activities.ProfileEditingActivity;
import com.example.myfair.activities.ScanActivity;
import com.example.myfair.db.Card;
import com.example.myfair.db.FirebaseDatabase;
import com.example.myfair.db.User;
import com.example.myfair.views.BottomSheet;
import com.example.myfair.views.BusinessCardView;
import com.example.myfair.views.CardInfoView;
import com.example.myfair.views.GenericCardView;
import com.example.myfair.views.UniversityCardView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String CARD_VIEWING_TOOLBAR_TITLE = "My Cards";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ScrollView profileMenu;
    private FirebaseDatabase db;
    private CardView profileCards, profileBrochures, profileDocuments;
    private TextView cardText, brochureText, documentText;

    private FirebaseAuth mAuth;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        setHasOptionsMenu(true);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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

        mAuth = FirebaseAuth.getInstance();
    }


    /**
     * Inflate the profile fragment action bar menu
     * @param menu - the Menu on which to inflate the action menu XML
     * @param inflater - the inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.action_menu_profile_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    /**
     * Handle actions within the action bar
     * @param item - which item was clicked
     * @return boolean value on if it was handled or not
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_create:
                intent = new Intent(getContext(), ProfileCreationActivity.class);
                break;
            case R.id.action_edit:
                intent = new Intent(getContext(), ProfileEditingActivity.class);
                break;
            case R.id.action_settings:
                //TODO: create a settings activity for management stuff
                intent = new Intent(getContext(), ProfileEditingActivity.class);
                break;
            case R.id.action_sign_out:
                signOut();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
        startActivity(intent);
        return true;
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
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        FloatingActionButton scanButton = v.findViewById(R.id.scanFAB);

        FragmentActivity mainActivity = getActivity();

        profileCards = v.findViewById(R.id.cvProfileCards);
        profileBrochures = v.findViewById(R.id.cvProfileBrochures);
        profileDocuments = v.findViewById(R.id.cvProfileDocs);
        profileMenu = v.findViewById(R.id.svProfileMenu);

        /*
            Programmatically adds shadows: ending up scrapping this
            brochureText = v.findViewById(R.id.textView6);
            brochureText.setShadowLayer(30, 0, 0, Color.BLACK);
            cardText = v.findViewById(R.id.textView2);
            cardText.setShadowLayer(30, 0, 0, Color.BLACK);
            documentText = v.findViewById(R.id.textView7);
            documentText.setShadowLayer(30, 0, 0, Color.BLACK);
         */



        db = new FirebaseDatabase();

        profileCards.setOnClickListener(menuCardListener);
        profileBrochures.setOnClickListener(menuCardListener);
        profileDocuments.setOnClickListener(menuCardListener);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ScanActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }

    /**
     * onClick override to handle menuCard clicks.
     * @param view - view that was clicked
     */
    private View.OnClickListener menuCardListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            Log.d("ButtonIDClicked", "ID: " + id);
            switch(id){
                case R.id.cvProfileCards:
                    Intent intent = new Intent(getContext(), CardViewingActivity.class);
                    intent.putExtra(CardViewingActivity.INTENT_TOOLBAR_TITLE, CARD_VIEWING_TOOLBAR_TITLE);
                    startActivity(intent);
                    break;
                case R.id.cvProfileBrochures:

                    break;
                case R.id.cvProfileDocs:

                    break;
                default:
                    Log.d("ErrorLog", view.getId() + "- button not yet implemented");
            }
        }
    };


    /**
     * Sign the user out and go back to the Login Activity
     */
    private void signOut() {
        mAuth.signOut();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        Objects.requireNonNull(getActivity()).finish();
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
