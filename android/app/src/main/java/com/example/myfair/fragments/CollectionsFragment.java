package com.example.myfair.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

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
import android.widget.ScrollView;

import com.example.myfair.activities.CardViewingActivity;
import com.example.myfair.R;
import com.example.myfair.activities.PacketViewingActivity;
import com.example.myfair.db.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CollectionsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CollectionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CollectionsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    public static final String PACKET_VIEWING_TOOLBAR_TITLE = "Received Packets";
    public static final String CARD_VIEWING_TOOLBAR_TITLE = "Card Wallet";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private CardView cvCards, cvPackets;
    private ScrollView svMenu;
    CollectionReference contactsLibrary;


    static final String TAG = "CollectionsFragmentLog";
    private FirebaseDatabase db;
    private OnFragmentInteractionListener mListener;

    public CollectionsFragment() {
        setHasOptionsMenu(true);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CollectionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CollectionsFragment newInstance(String param1, String param2) {
        CollectionsFragment fragment = new CollectionsFragment();
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
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /**
     * Standard onCreateOptionsMenu override.
     * Inflates options menu.
     * @param menu - Menu selected.
     * @param inflater - inflater responsible with inflating menu.
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.actionmenu_collections, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Standard onOptionsItemsSelected override.
     * Determines menu item selected and executes associated functions.
     * @param item - menu item selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_menu_search:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_menu_filter:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

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
        View v = inflater.inflate(R.layout.fragment_collections, container, false);

        FragmentActivity mainActivity = getActivity();

        db = new FirebaseDatabase();

        // find views
        cvCards = v.findViewById(R.id.cvProfileCards);
        cvPackets = v.findViewById(R.id.cvProfilePackets);
        svMenu = v.findViewById(R.id.svProfileMenu);
        svMenu.setVisibility(View.VISIBLE);
        cvCards.setOnClickListener(cvListener);
        cvPackets.setOnClickListener(cvListener);
        contactsLibrary = db.userContacts();

        return v;
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

    /**
     * onClick override to handle cv clicks.
     * @param view - view that is populated with card categories
     */
    private View.OnClickListener cvListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            Intent intent;
            switch(id){
                case R.id.cvProfileCards:
                    // start new intent
                    intent = new Intent(getContext(), CardViewingActivity.class);
                    intent.putExtra(CardViewingActivity.INTENT_TOOLBAR_TITLE, CARD_VIEWING_TOOLBAR_TITLE);
                    startActivity(intent);
                    break;
                case R.id.cvProfilePackets:
                    intent = new Intent(getContext(), PacketViewingActivity.class);
                    intent.putExtra(PacketViewingActivity.INTENT_TOOLBAR_TITLE, PACKET_VIEWING_TOOLBAR_TITLE);
                    startActivity(intent);
                    break;
                default:
                    Log.d("ErrorLog", view.getId() + "- button not yet implemented");
            }
        }
    };

}
