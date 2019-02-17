package com.example.myfair;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.net.Uri;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements
        CreateFragment.OnFragmentInteractionListener, AnalyticsFragment.OnFragmentInteractionListener,
        CollectionsFragment.OnFragmentInteractionListener, HistoryFragment.OnFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener {

    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private boolean profileCreated;

    private Toolbar toolbar;

    private Fragment fragmentHistory;
    private Fragment fragmentCollections;
    private Fragment fragmentCreate;
    private Fragment fragmentAnalytics;
    private Fragment fragmentProfile;
    private FragmentManager fm;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_history:
                    switchToFragment(fragmentHistory, HistoryFragment.NAME);
                    return true;

                case R.id.navigation_collections:
                    switchToFragment(fragmentCollections, CollectionsFragment.NAME);
                    return true;
                case R.id.navigation_create:
                    switchToFragment(fragmentCreate, CreateFragment.NAME);
                    return true;
                case R.id.navigation_analytics:
                    switchToFragment(fragmentAnalytics, AnalyticsFragment.NAME);
                    return true;
                case R.id.navigation_profile:
                    switchToFragment(fragmentProfile, ProfileFragment.NAME);
                    return true;
            }
            return false;
        }

    };

    private void switchToFragment(Fragment fragment, String title) {
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragmentLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        toolbar.setTitle(title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        checkProfile();
      
        fragmentHistory = new HistoryFragment();
        fragmentCollections = new CollectionsFragment();
        fragmentCreate = new CreateFragment();
        fragmentAnalytics = new AnalyticsFragment();
        fragmentProfile = new ProfileFragment();

        fm = getSupportFragmentManager();

        BottomNavigationView navBar = (BottomNavigationView) findViewById(R.id.navigation);
        navBar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fm.beginTransaction().add(R.id.fragmentLayout, fragmentCollections).commit();
        toolbar.setTitle(CollectionsFragment.NAME);
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
                    if (document != null && document.exists()) {
                        // document snapshot succeeded
                        User localUser = new User(document.getData());

                        if(localUser.profileCreated()) {
                            profileCreated = true;
                        } else {
                            profileCreated = false;
                            updateUI();
                        }
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
