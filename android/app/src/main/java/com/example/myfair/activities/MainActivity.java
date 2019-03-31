package com.example.myfair.activities;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.net.Uri;

import com.example.myfair.R;
import com.example.myfair.db.User;
import com.example.myfair.fragments.AnalyticsFragment;
import com.example.myfair.fragments.CollectionsFragment;
import com.example.myfair.fragments.CreateFragment;
import com.example.myfair.fragments.HistoryFragment;
import com.example.myfair.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;

import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Method;

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

    private Resources res;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_history:
                    switchToFragment(fragmentHistory, res.getString(R.string.fragment_title_history));
                    return true;
                case R.id.navigation_collections:
                    switchToFragment(fragmentCollections, res.getString(R.string.fragment_title_collections));
                    return true;
                case R.id.navigation_create:
                    switchToFragment(fragmentCreate, res.getString(R.string.fragment_title_create));
                    return true;
                case R.id.navigation_analytics:
                    switchToFragment(fragmentAnalytics, res.getString(R.string.fragment_title_analytics));
                    return true;
                case R.id.navigation_profile:
                    switchToFragment(fragmentProfile, res.getString(R.string.fragment_title_profile));
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

        res = getResources();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(res.getString(R.string.fragment_title_profile));
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        fragmentHistory = new HistoryFragment();
        fragmentCollections = new CollectionsFragment();
        fragmentCreate = new CreateFragment();
        fragmentAnalytics = new AnalyticsFragment();
        fragmentProfile = new ProfileFragment();

        fm = getSupportFragmentManager();

        BottomNavigationView navBar = (BottomNavigationView) findViewById(R.id.navigation);
        navBar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fm.beginTransaction().add(R.id.fragmentLayout, fragmentProfile).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
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
