package com.example.myfair;

import android.content.Intent;
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

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements
        CreateFragment.OnFragmentInteractionListener, AnalyticsFragment.OnFragmentInteractionListener,
        CollectionsFragment.OnFragmentInteractionListener, HistoryFragment.OnFragmentInteractionListener {

    private FirebaseAuth mAuth;

    private TextView mTextMessage;

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
                    switchToHistory();
                    return true;

                case R.id.navigation_collections:
                    switchToCollections();
                    return true;

                case R.id.navigation_create:
                    switchToCreate();
                    return true;
                case R.id.navigation_analytics:
                    switchToAnalytics();
                    return true;
            }
            return false;
        }

    };

    public void switchToHistory() {
        fm.beginTransaction().replace(R.id.fragmentLayout, fragmentHistory).commit();
    }

    public void switchToCollections() {
        fm.beginTransaction().replace(R.id.fragmentLayout, fragmentCollections).commit();
    }

    public void switchToCreate() {
        fm.beginTransaction().replace(R.id.fragmentLayout, fragmentCreate).commit();
    }

    public void switchToAnalytics() {
        fm.beginTransaction().replace(R.id.fragmentLayout, fragmentAnalytics).commit();
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

        mTextMessage = (TextView) findViewById(R.id.textView);

        BottomNavigationView navBar = (BottomNavigationView) findViewById(R.id.navigation);
        navBar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fm.beginTransaction().add(R.id.fragmentLayout, fragmentAnalytics, "4").hide(fragmentAnalytics).commit();
        fm.beginTransaction().add(R.id.fragmentLayout, fragmentCreate, "3").hide(fragmentCreate).commit();
        fm.beginTransaction().add(R.id.fragmentLayout, fragmentCollections, "2").hide(fragmentCollections).commit();
        fm.beginTransaction().add(R.id.fragmentLayout, fragmentHistory, "1").commit();

        Button btnSignOut = findViewById(R.id.btnSignOut);
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // can leave empty
    }
}
