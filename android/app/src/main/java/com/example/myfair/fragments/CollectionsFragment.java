package com.example.myfair.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

import com.example.myfair.activities.GenerateActivity;
import com.example.myfair.R;
import com.example.myfair.activities.MainActivity;
import com.example.myfair.db.Card;
import com.example.myfair.db.FirebaseDatabase;
import com.example.myfair.views.BottomSheet;
import com.example.myfair.views.BusinessCardView;
import com.example.myfair.views.CardInfoView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


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
    private int lastForm;

    static final String TAG = "CollectionsFragmentLog";
    private FirebaseDatabase db;
    private Button btnUser, btnContacts;
    private ImageButton  btnShare;

    private LinearLayout lytListView, lytListViewUser;
    private ImageButton btnBack;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.actionmenu_collections, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_collections, container, false);

        FragmentActivity mainActivity = getActivity();
        fm = mainActivity.getSupportFragmentManager();
        db = new FirebaseDatabase();
        lastForm = 1;

        FloatingActionButton shareFAB = v.findViewById(R.id.shareFAB);
        shareFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), GenerateActivity.class);
                startActivity(intent);
            }
        });


        // Return the fragment manager


        // find views
        lytListView = v.findViewById(R.id.lytListView);
        lytListViewUser = v.findViewById(R.id.lytListViewUser);
        cardInfo = v.findViewById(R.id.cardInfo);
        btnUser = v.findViewById(R.id.btnUserLib);
        btnContacts = v.findViewById(R.id.btnCollection);
        btnBack = cardInfo.findViewById(R.id.btnInfoBack);
        btnShare = cardInfo.findViewById(R.id.btnShare);
        btnBack.setOnClickListener(buttonListener);
        btnUser.setOnClickListener(buttonListener);
        btnContacts.setOnClickListener(buttonListener);
        btnShare.setOnClickListener(buttonListener);

        // pull cards from database
        getIdList(db.userCards(), lytListViewUser);
        getIdList(db.userContacts(), lytListView);

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

    private View.OnClickListener cardClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            changeForm(3);
            cardInfo.setFromBusinessCardView((BusinessCardView) view, getContext());
            Log.d("CardInfoCreated", "card Info Visible");
        }
    };

    private View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch(id){
                case R.id.btnInfoBack:
                    changeForm(lastForm);
                    break;
                case R.id.btnCollection:
                    changeForm(1);
                    break;
                case R.id.btnUserLib:
                    changeForm(2);
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

    private void changeForm(int form){
        switch (form){
            case 1:
                lytListView.setVisibility(View.VISIBLE);
                lytListViewUser.setVisibility(View.GONE);
                cardInfo.setVisibility(View.GONE);
                btnContacts.setVisibility(View.VISIBLE);
                btnUser.setVisibility(View.VISIBLE);
                lastForm = form;
                break;
            case 2:
                lytListView.setVisibility(View.GONE);
                lytListViewUser.setVisibility(View.VISIBLE);
                cardInfo.setVisibility(View.GONE);
                btnContacts.setVisibility(View.VISIBLE);
                btnUser.setVisibility(View.VISIBLE);
                lastForm = form;
                break;
            case 3:
                lytListView.setVisibility(View.GONE);
                lytListViewUser.setVisibility(View.GONE);
                cardInfo.setVisibility(View.VISIBLE);
                btnContacts.setVisibility(View.GONE);
                btnUser.setVisibility(View.GONE);
                break;
            default:
                Log.d("CollectionsFragmentLog", "form not implemented..");
        }
    }

    private void createCardView(Card c, LinearLayout listView) {
        BusinessCardView v = new BusinessCardView(getContext());
        listView.addView(v);
        v.setFromCardModel(c);
        v.setMargins();
        v.setOnClickListener(cardClickListener);
    }

    private void getIdList(CollectionReference ref, final LinearLayout listView){
        ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Card c = new Card();
                        c.setCardID(document.getId());
                        c.setMap(document.getData());
                        createCardView(c, listView);
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }
}
