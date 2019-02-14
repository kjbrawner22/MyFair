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
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements
        CreateFragment.OnFragmentInteractionListener, AnalyticsFragment.OnFragmentInteractionListener,
        CollectionsFragment.OnFragmentInteractionListener, HistoryFragment.OnFragmentInteractionListener {

    private FirebaseAuth mAuth;

    private TextView mTextMessage;

    final Fragment fragment1 = new HistoryFragment();
    final Fragment fragment2 = new CollectionsFragment();
    final Fragment fragment3 = new CreateFragment();
    final Fragment fragment4 = new AnalyticsFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_history:
                    fm.beginTransaction().hide(active).show(fragment1).commit();
                    active = fragment1;
                    return true;

                case R.id.navigation_collections:
                    fm.beginTransaction().hide(active).show(fragment2).commit();
                    active = fragment2;
                    return true;

                case R.id.navigation_create:
                    fm.beginTransaction().hide(active).show(fragment3).commit();
                    active = fragment3;
                    return true;
                case R.id.navigation_analytics:
                    fm.beginTransaction().hide(active).show(fragment4).commit();
                    active = fragment4;
                    return true;
            }
            return false;
        }

    };

    public void switchToHistory() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fragmentLayout, new CollectionsFragment()).commit();
    }

    public void switchToCollections() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fragmentLayout, new CollectionsFragment()).commit();
    }

    public void switchToCreate() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fragmentLayout, new CollectionsFragment()).commit();
    }

    public void switchToAnalytics() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fragmentLayout, new CollectionsFragment()).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        mTextMessage = (TextView) findViewById(R.id.textView);

        BottomNavigationView navBar = (BottomNavigationView) findViewById(R.id.navigation);
        navBar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fm.beginTransaction().add(R.id.fragmentLayout, fragment4, "4").hide(fragment4).commit();
        fm.beginTransaction().add(R.id.fragmentLayout, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.fragmentLayout, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.fragmentLayout, fragment1, "1").commit();

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
