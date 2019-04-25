package com.example.myfair.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myfair.R;
import com.example.myfair.db.User;
import com.example.myfair.views.ProfileEditField;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * Edit User's profile information
 */
public class ProfileEditingActivity extends AppCompatActivity {

    private static final String TAG = "ProfileEditingActivity";
    private LinearLayout lytEditFields;
    private ArrayList<ProfileEditField> editFields;

    /**
     * standard onCreate override. initalize view and the toolbar
     * @param savedInstanceState - app's saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_editing);

        editFields = new ArrayList<>();

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

    /**
     * Handle clicks on the action bar's items
     * @param item - Menu item clicked
     * @return boolean on handler success result
     */
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_save:
                saveChanges();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Inflate the action menu
     * @param menu - Menu object to inflate the layout on
     * @return boolean success value
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu_profile_edit, menu);
        return true;
    }

    /**
     * Set the current Edit fields to correspond to the user's info
     * @param map - Map representing the User's information
     */
    private void setEditFields(HashMap<String, Object> map) {
        for (Map.Entry pair : map.entrySet()) {
            if (User.isPrivateField((String) pair.getKey())) {
                continue;
            }

            ProfileEditField editField = new ProfileEditField(this);
            lytEditFields.addView(editField);
            editField.setLabel((String) pair.getKey());
            editField.setField((String) pair.getValue());
            editField.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            editFields.add(editField);
        }
    }

    /**
     * Save the changes made to the database
     */
    private void saveChanges() {
        User u = new User();
        HashMap<String, Object> map = new HashMap<>();
        for (ProfileEditField field : editFields) {
            map.put(field.getLabel(), field.getField());
        }

        u.setMap(map);
        u.sendToDb();
    }
}
