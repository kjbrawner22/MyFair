package com.example.myfair;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements
        CreateFragment.OnFragmentInteractionListener, AnalyticsFragment.OnFragmentInteractionListener,
        CollectionsFragment.OnFragmentInteractionListener, HistoryFragment.OnFragmentInteractionListener {

    private FirebaseAuth mAuth;

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
        fragmentHistory = new HistoryFragment();
        fragmentCollections = new CollectionsFragment();
        fragmentCreate = new CreateFragment();
        fragmentAnalytics = new AnalyticsFragment();

        fm = getSupportFragmentManager();

        BottomNavigationView navBar = findViewById(R.id.navigation);
        navBar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // start on Collections tab
        fm.beginTransaction().add(R.id.fragmentLayout, fragmentCollections).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // can leave empty
    }
}
