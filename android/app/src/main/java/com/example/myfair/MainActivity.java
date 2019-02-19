package com.example.myfair;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.net.Uri;

import com.example.myfair.db.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements
        CreateFragment.OnFragmentInteractionListener, AnalyticsFragment.OnFragmentInteractionListener,
        CollectionsFragment.OnFragmentInteractionListener, HistoryFragment.OnFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener {

    private FirebaseAuth mAuth;
    private User user;

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
                    switchToFragment(fragmentCollections, getResources().getString(R.string.title_allCollections));
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

        user = new User();
        user.setFromDb();
      
        fragmentHistory = new HistoryFragment();
        fragmentCollections = new CollectionsFragment();
        fragmentCreate = new CreateFragment();
        fragmentAnalytics = new AnalyticsFragment();
        fragmentProfile = new ProfileFragment();

        fm = getSupportFragmentManager();

        BottomNavigationView navBar = (BottomNavigationView) findViewById(R.id.navigation);
        navBar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fm.beginTransaction().add(R.id.fragmentLayout, fragmentProfile).commit();
        toolbar.setTitle(ProfileFragment.NAME);


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void checkProfile(){
        Log.d("getCardInfo", "Stored Boolean: " + user.profileCreated());

        if (!user.profileCreated()){
            Log.d("getCardInfo", "Update UI");
            updateUI();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void updateUI() {
        Intent intent = new Intent(MainActivity.this, ProfileCreationActivity.class);
        startActivity(intent);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // can leave empty
    }

}
