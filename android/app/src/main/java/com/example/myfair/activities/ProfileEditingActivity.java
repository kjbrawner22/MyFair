package com.example.myfair.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.myfair.R;
import com.example.myfair.db.User;
import com.example.myfair.views.ProfileEditField;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.Inflater;

import javax.annotation.Nullable;

public class ProfileEditingActivity extends AppCompatActivity {

    private static final String TAG = "ProfileEditingActivity";
    private FirebaseUser user;
    private FirebaseFirestore db;
    private LinearLayout lytEditFields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_editing);

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        lytEditFields = findViewById(R.id.lytEditFields);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final User u = new User();
        u.setFromDb().addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    setEditFields((HashMap<String, Object>) snapshot.getData());
                }
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_save:
                // handle saving data here
                saveChanges();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu_profile_edit, menu);
        return true;
    }

    private void setEditFields(HashMap<String, Object> map) {
        for (Map.Entry pair : map.entrySet()) {
            ProfileEditField editField = new ProfileEditField(this);
            lytEditFields.addView(editField);
            editField.setLabel((String) pair.getKey());
            editField.setField((String) pair.getValue());
            editField.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    private HashMap<String, Object> getUpdatedFields() {
        return null;
    }

    private void saveChanges() {
        HashMap<String, Object> map = getUpdatedFields();
        //TODO: save changes for Firestore
    }
}
