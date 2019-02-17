package com.example.myfair;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements
        CreateFragment.OnFragmentInteractionListener, AnalyticsFragment.OnFragmentInteractionListener,
        CollectionsFragment.OnFragmentInteractionListener, HistoryFragment.OnFragmentInteractionListener {

    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private boolean profileCreated;

    Fragment fragmentHistory;
    Fragment fragmentCollections;
    Fragment fragmentCreate;
    Fragment fragmentAnalytics;
    FragmentManager fm;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_history:
                    switchToFragment(fragmentHistory);
                    return true;

                case R.id.navigation_collections:
                    switchToFragment(fragmentCollections);
                    return true;
                case R.id.navigation_create:
                    switchToFragment(fragmentCreate);
                    return true;
                case R.id.navigation_analytics:
                    switchToFragment(fragmentAnalytics);
                    return true;
            }
            return false;
        }

    };

    private void switchToFragment(Fragment fragment) {
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragmentLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        checkProfile();
      
        fragmentHistory = new HistoryFragment();
        fragmentCollections = new CollectionsFragment();
        fragmentCreate = new CreateFragment();
        fragmentAnalytics = new AnalyticsFragment();

        fm = getSupportFragmentManager();

        BottomNavigationView navBar = (BottomNavigationView) findViewById(R.id.navigation);
        navBar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fm.beginTransaction().add(R.id.fragmentLayout, fragmentCollections).commit();
    }

    private void checkProfile(){
        final String TAG = "checkProfileCreated";
        final String uID = user.getUid();
                        // check if null
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("users").document(uID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // document snapshot succeeded
                        Map<String, Object> data;
                        User localUser = new User();
                        data = document.getData();
                        localUser.setMap(data);

                        if(profileComplete(localUser)) {
                            profileCreated = true;
                        } else {
                            profileCreated = false;
                            updateUI();
                        }

                        // log statements
                        // Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        // Log.d(TAG, "Data stored: " + data);
                        // Log.d(TAG, "User stored: " + localUser.getValue(localUser.FIELD_FIRST_NAME) + " " + localUser.getValue(localUser.FIELD_LAST_NAME));


                    } else {  // document doesn't exist yet
                        profileCreated = false;
                        Log.d(TAG, "No such document");
                        updateUI();
                    }
                } else {  // document snapshot failed
                    profileCreated = false;
                    Log.d(TAG, "get failed with ", task.getException());
                    updateUI();
                }
            }
        });
    }

    private boolean profileComplete(User localUser){
        boolean a = localUser.containsKey(User.FIELD_PROFILE_CREATED);

        if(a){      // localUser contains profile flag, check flag
            return localUser.getValue(User.FIELD_PROFILE_CREATED).equals(User.VALUE_TRUE);
        }
        return a;   // profile flag was never set, no need to check
    }

    private void updateUI() {
        Intent intent = new Intent(MainActivity.this, ProfileCreation.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // can leave empty
    }
}
