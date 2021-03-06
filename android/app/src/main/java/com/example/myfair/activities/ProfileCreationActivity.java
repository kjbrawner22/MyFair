package com.example.myfair.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myfair.R;
import com.example.myfair.db.User;
import com.example.myfair.views.AddConnectionsView;
import com.example.myfair.views.ConnectionView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileCreationActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ProfileCreationTag";


    private FirebaseUser user;
    private FirebaseFirestore db;
    private EditText etFname, etLname;
    FloatingActionButton btnBack, btnForward;
    private ConstraintLayout lytName, lytSocial;
    private User dbUser;
    private AddConnectionsView addConnectionsView;
    private LinearLayout connectionsView;


    /**
     * standard onCreate override. get needed handles and initialize view.
     * @param savedInstanceState - app's saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_creation);

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        dbUser = new User();
        btnBack = findViewById(R.id.btnBack);
        btnForward = findViewById(R.id.btnForward);
        lytName = findViewById(R.id.lytName);
        lytSocial = findViewById(R.id.lytSocial);
        etFname = findViewById(R.id.etFname);
        etLname = findViewById(R.id.etLname);

        addConnectionsView = findViewById(R.id.addConnectionsView);
        addConnectionsView.setSpinnerAdapter();

        connectionsView = findViewById(R.id.container);

        addConnectionsView.setAddConnectionClickListener(this, connectionsView);

        changeForm(1);

        btnBack.setOnClickListener(this);
        btnForward.setOnClickListener(this);
    }

    /**
     * onClick override to handle all click listeners.
     * @param v - view that was clicked
     */
    @Override
    public void onClick(final View v) {
        int id = v.getId();

        int state = getFormState();
        switch (state) {
            case 1:
                if(id == R.id.btnForward) {
                    if (validProfileFields()) {
                        String name = etFname.getText().toString() + " " + etLname.getText().toString();
                        dbUser.setValue(User.FIELD_NAME, name);
                        changeForm(2);
                    }
                }
                break;
            case 2:
                if(id == R.id.btnForward){
                    addConnectionsToUser();
                    updateUser();
                } else if (id == R.id.btnBack){
                    changeForm(1);
                }
                break;
        }
    }

    /**
     * Add the user's connections to the User model
     */
    private void addConnectionsToUser() {
        for (int i = 0; i < connectionsView.getChildCount(); i++) {
            ConnectionView view = (ConnectionView) connectionsView.getChildAt(i);
            dbUser.setValue(view.getConnectionType(), view.getText());
        }
    }

    /**
     * Send the user's information to the database, then update the UI
     */
    private void updateUser() {
        dbUser.setValue(User.FIELD_PROFILE_CREATED, User.VALUE_TRUE);
        dbUser.sendToDb();
        updateUI();
    }

    /**
     * validate the name fields.
     * @return boolean success or failure
     */
    private boolean validProfileFields() {
        final String fname = etFname.getText().toString();
        final String lname = etLname.getText().toString();

        if (fname.isEmpty()) {
            etFname.setError("First name field is required.");
            etFname.requestFocus();
        } else if (lname.isEmpty()) {
            etLname.setError("Last name field is required.");
            etLname.requestFocus();
        } else {
            return true;
        }

        return false;
    }

    /**
     * get the form's state.
     * @return int state value
     */
    private int getFormState(){
        if(lytName.getVisibility() == View.VISIBLE) {
            return 1;
        } else {
            return 2;
        }
    }

    /**
     * change form view
     * @param state - state of the current view setting
     */
    private void changeForm(int state) {
        switch (state){
            case 1:
                btnBack.hide();
                lytName.setVisibility(View.VISIBLE);
                lytSocial.setVisibility(View.GONE);
                break;
            case 2:
                btnBack.show();
                lytSocial.setVisibility(View.VISIBLE);
                lytName.setVisibility(View.GONE);
                break;
            default:
                Log.d("PC ChangeForm", "wrong input param");
        }

    }

    /**
     * Update the UI and go to the Main Activity
     */
    private void updateUI() {
        boolean returnToMain = getIntent().getBooleanExtra(User.FIELD_PROFILE_CREATED, false);

        // if profile hasn't already been created, create a new main activity instance
        // otherwise, just finish this activity and go back to the previous
        // main activity instance
        if (!returnToMain) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        finish();
    }
}
