package com.example.myfair.fragments;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.myfair.activities.CardViewingActivity;
import com.example.myfair.activities.GenerateActivity;
import com.example.myfair.R;
import com.example.myfair.activities.MainActivity;
import com.example.myfair.db.Card;
import com.example.myfair.db.FirebaseDatabase;
import com.example.myfair.views.BottomSheet;
import com.example.myfair.views.BusinessCardView;
import com.example.myfair.views.CardInfoView;
import com.example.myfair.views.GenericCardView;
import com.example.myfair.views.UniversityCardView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;


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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private CardInfoView cardInfo;
    private CardView cvCards, cvBrochures, cvDocs;
    private ScrollView svMenu, svCardScroller;
    private int lastForm;

    static final String TAG = "CollectionsFragmentLog";
    private FirebaseDatabase db;
    private ImageButton  btnShare, btnBack, btnMenuBack;

    private LinearLayout lytListView;
    private OnFragmentInteractionListener mListener;
    private androidx.fragment.app.FragmentManager fm;

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
        fm = mainActivity.getSupportFragmentManager();
        db = new FirebaseDatabase();
        lastForm = 1;
        svCardScroller = v.findViewById(R.id.svCardScroller);



        // Return the fragment manager


        // find views
        lytListView = v.findViewById(R.id.lytListView);
        cardInfo = v.findViewById(R.id.cardInfo);
        btnBack = cardInfo.findViewById(R.id.btnInfoBack);
        btnShare = cardInfo.findViewById(R.id.btnShare);
        cvCards = v.findViewById(R.id.cvProfileCards);
        cvBrochures = v.findViewById(R.id.cvProfileBrochures);
        cvDocs = v.findViewById(R.id.cvProfileDocs);
        svMenu = v.findViewById(R.id.svProfileMenu);
        btnMenuBack = v.findViewById(R.id.btnMenuBack);

        btnBack.setOnClickListener(buttonListener);
        btnShare.setOnClickListener(buttonListener);
        btnMenuBack.setOnClickListener(buttonListener);
        cvCards.setOnClickListener(cvListener);
        cvBrochures.setOnClickListener(cvListener);
        cvDocs.setOnClickListener(cvListener);

        changeForm(2);

        // pull cards from database
        getIdList(db.userContacts(), lytListView);

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
     * onClick override to handle businessCard clicks.
     * @param view - view that was clicked
     */
    private View.OnClickListener businessCardClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            changeForm(3);
            cardInfo.setFromBusinessCardView((BusinessCardView) view, getContext());
            Log.d("CardInfoCreated", "card Info Visible");
        }
    };

    /**
     * onClick override to handle universityCard clicks.
     * @param view - view that was clicked
     */
    private View.OnClickListener universityCardClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            changeForm(3);
            cardInfo.setFromUniversityCardView((UniversityCardView) view, getContext());
            Log.d("CardInfoCreated", "card Info Visible");
        }
    };


    /**
     * onClick override to handle cv clicks.
     * @param view - view that is populated with card categories
     */
    private View.OnClickListener cvListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch(id){
                case R.id.cvProfileCards:
                    // start new intent
                    Intent intent = new Intent(getContext(), CardViewingActivity.class);
                    startActivity(intent);
                    break;
                case R.id.cvProfileBrochures:
                    changeForm(1);
                    break;
                case R.id.cvProfileDocs:
                    changeForm(1);
                    break;
                default:
                    Log.d("ErrorLog", view.getId() + "- button not yet implemented");
            }
        }
    };

    /**
     * onClick override to handle button clicks.
     * @param view - view that was clicked
     */
    private View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            Log.d("ButtonIDClicked", "ID: " + id);
            switch(id){
                case R.id.btnMenuBack:
                    changeForm(2);
                    break;
                case R.id.btnInfoBack:
                    changeForm(lastForm);
                    break;
                case R.id.btnShare:
                    Bundle bundle = new Bundle();
                    String str = cardInfo.getQrStr();
                    bundle.putString("encryptedString", str);
                    //Log.d("EncryptedString", str);
                    BottomSheet bottomSheet = new BottomSheet();
                    bottomSheet.setArguments(bundle);
                    bottomSheet.show(fm, "exampleBottomSheet");
                    break;
                default:
                    Log.d("ErrorLog", view.getId() + "- button not yet implemented");
            }
        }
    };

    /**
     * change form view
     * @param form - current view setting
     */
    private void changeForm(int form){
        switch (form){
            case 1:
                // Contacts
                svCardScroller.setVisibility(View.VISIBLE);
                cardInfo.setVisibility(View.GONE);
                svMenu.setVisibility(View.GONE);
                lastForm = form;
                break;
            case 2:
                //menu
                svCardScroller.setVisibility(View.GONE);
                cardInfo.setVisibility(View.GONE);
                svMenu.setVisibility(View.VISIBLE);
                break;
            case 3:
                // card info
                svCardScroller.setVisibility(View.GONE);
                cardInfo.setVisibility(View.VISIBLE);
                svMenu.setVisibility(View.GONE);
                break;
            default:
                Log.d("CollectionsFragmentLog", "form not implemented..");
        }
    }

    /**
     * Adds a card to the linear layout
     * @param listView - linear layout holding card views
     * @param v - card view being added to list
     * */
    private void addCardView(GenericCardView v, LinearLayout listView) {
        listView.addView(v);
        v.setMargins();
    }

    /**
     * Gets the list of IDs of cards and populates a linear layout with cardViews.
     * */
    private void getIdList(CollectionReference ref, final LinearLayout listView){
        ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String cID = document.getId();
                        HashMap<String,Object> map = (HashMap<String,Object>) document.getData();
                        //String type = (String) map.get(Card.FIELD_TYPE);
                        UniversityCardView v = new UniversityCardView(getContext(), cID, map);
                        v.setOnClickListener(universityCardClickListener);
                        addCardView(v, listView);
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }
}
