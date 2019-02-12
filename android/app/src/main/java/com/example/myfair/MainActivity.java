package com.example.myfair;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private boolean profileCreated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        checkProfile();

        Button btnSignOut = findViewById(R.id.btnSignOut);
        Button btnProfileCreation = findViewById(R.id.btnProfileCreation);

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnProfileCreation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileCreation.class);
                startActivity(intent);
                finish();
            }
        });
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
                    if (document.exists()) {
                        // document snapshot succeeded
                        Map<String, Object> data;
                        User localUser = new User();
                        data = document.getData();
                        localUser.setMap(data);

                        if(profileComplete(localUser)) {
                            profileCreated = true;
                        } else {
                            profileCreated = false;
                            updateUI();
                        }

                        // log statements
                        // Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        // Log.d(TAG, "Data stored: " + data);
                        // Log.d(TAG, "User stored: " + localUser.getValue(localUser.FIELD_FIRST_NAME) + " " + localUser.getValue(localUser.FIELD_LAST_NAME));


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

    private boolean profileComplete(User localUser){
        boolean a = localUser.containsKey(User.FIELD_PROFILE_CREATED), b = false;

        if(a){      // localUser contains profile flag, check flag
            b = localUser.getValue(User.FIELD_PROFILE_CREATED).equals("true");
        }
        return b;   // profile flag was never set, no need to check
    }

    private void updateUI() {
        Intent intent = new Intent(MainActivity.this, ProfileCreation.class);
        startActivity(intent);
        finish();
    }
}
