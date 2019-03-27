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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageButton btnBack;
    private ImageButton btnShare;
    HashMap<String,Object> idListMap;
    CardInfoView cardInfo;
    LinearLayout lytCardList;
    FirebaseDatabase db;
    private androidx.fragment.app.FragmentManager fm;

    private FirebaseAuth mAuth;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        FloatingActionButton scanButton = v.findViewById(R.id.scanFAB);



        FragmentActivity mainActivity = getActivity();
        fm = mainActivity.getSupportFragmentManager();

        lytCardList = v.findViewById(R.id.lytCardList);
        cardInfo = v.findViewById(R.id.profileCardInfo);
        btnBack = cardInfo.findViewById(R.id.btnInfoBack);
        btnShare = cardInfo.findViewById(R.id.btnShare);

        db = new FirebaseDatabase();
        getIdList(db.userCards(), lytCardList);

        btnBack.setOnClickListener(buttonListener);
        btnShare.setOnClickListener(buttonListener);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ScanActivity.class);
                startActivity(intent);
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

    private void changeForm(int form){
        switch(form){
            case 1:
                lytCardList.setVisibility(View.VISIBLE);
                cardInfo.setVisibility(View.GONE);
                break;
            case 2:
                lytCardList.setVisibility(View.GONE);
                cardInfo.setVisibility(View.VISIBLE);
                break;
            default:
                Log.d("ChangeForm", "Form not implmented");
        }
    }

    private View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            Log.d("ButtonIDClicked", "ID: " + id);
            switch(id){
                case R.id.btnInfoBack:
                    changeForm(1);
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

    private View.OnClickListener businessCardClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            changeForm(2);
            cardInfo.setFromBusinessCardView((BusinessCardView) view, getContext());
            Log.d("CardInfoCreated", "card Info Visible");
        }
    };

    private View.OnClickListener universityCardClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            changeForm(2);
            cardInfo.setFromUniversityCardView((UniversityCardView) view, getContext());
            Log.d("CardInfoCreated", "card Info Visible");
        }
    };

    private void addCardView(GenericCardView v, LinearLayout listView) {
        listView.addView(v);
        v.setMargins();
    }

    private void getIdList(CollectionReference ref, final LinearLayout listView){
        ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                final String TAG = "profileGetIdList";
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String cID = document.getId();
                        HashMap<String,Object> map = (HashMap<String,Object>) document.getData();
                        String type = (String) map.get(Card.FIELD_TYPE);
                        if(type != null && type.equals(Card.VALUE_TYPE_BUSINESS)) {
                            BusinessCardView v = new BusinessCardView(getContext(), cID, map);
                            v.setOnClickListener(businessCardClickListener);
                            addCardView(v, listView);
                        }
                        else if(type != null){
                            UniversityCardView v = new UniversityCardView(getContext(), cID, map);
                            v.setOnClickListener(universityCardClickListener);
                            addCardView(v, listView);
                        }
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }
}
