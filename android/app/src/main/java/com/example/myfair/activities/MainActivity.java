package com.example.myfair.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.myfair.R;
import com.example.myfair.fragments.AnalyticsFragment;
import com.example.myfair.fragments.CollectionsFragment;
import com.example.myfair.fragments.CreateFragment;
import com.example.myfair.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * MainActivity that is a central point for the main fragments identified in the 'implements' area
 */
public class MainActivity extends AppCompatActivity implements
        CreateFragment.OnFragmentInteractionListener, AnalyticsFragment.OnFragmentInteractionListener,
        CollectionsFragment.OnFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener {

    private Toolbar toolbar;

    private Fragment fragmentCollections;
    private Fragment fragmentCreate;
    private Fragment fragmentAnalytics;
    private Fragment fragmentProfile;
    private FragmentManager fm;

    public static final DisplayImageOptions displayOptions = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .build();

    private Resources res;

    /**
     * Handler for the navigation selection's by the user
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
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

    /**
     * @param fragment - Fragment to switch to.
     * @param title - string title of fragment
     */
    private void switchToFragment(Fragment fragment, String title) {
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragmentLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        toolbar.setTitle(title);
    }

    /**
     * standard onCreate override. initialize the toolbar, the fragments, and the navbar.
     * @param savedInstanceState - app's saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize Image Loader singleton
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));

        res = getResources();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(res.getString(R.string.fragment_title_profile));
        setSupportActionBar(toolbar);

        fragmentCollections = new CollectionsFragment();
        fragmentCreate = new CreateFragment();
        fragmentAnalytics = new AnalyticsFragment();
        fragmentProfile = new ProfileFragment();

        fm = getSupportFragmentManager();

        BottomNavigationView navBar = findViewById(R.id.navigation);
        navBar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fm.beginTransaction().add(R.id.fragmentLayout, fragmentProfile).commit();
    }

    //Not sure if we will need this
    @Override
    public void onFragmentInteraction(Uri uri) {
        // can leave empty
    }

}
