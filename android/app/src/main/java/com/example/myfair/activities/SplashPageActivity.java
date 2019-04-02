package com.example.myfair.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.myfair.db.User;
import com.example.myfair.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class SplashPageActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_page);

        ImageView img = findViewById(R.id.spinnyLogo);
        img.setBackgroundResource(R.drawable.icon_spin_animation);

        AnimationDrawable frameAnim = (AnimationDrawable) img.getBackground();

        frameAnim.start();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.e("Starting","Starting");
        updateUI();
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        Log.e("updateUI","checking UI");
        user = mAuth.getCurrentUser();
        Log.e("updateUI","gotUI");
        if (user != null) {
            Log.e("updateUI","not null");
            user.reload().addOnCompleteListener(userReloadListener);
        }
        else{
            Intent intent = new Intent(SplashPageActivity.this, LoginActivity.class);
            intent.putExtra(User.FIELD_PROFILE_CREATED, false);
            startActivity(intent);
            finish();
        }

    }

    private final OnCompleteListener<Void> userReloadListener = new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if(task.isSuccessful()){
                Log.e("userReloadListener", "onComplete: Success");
                if(user.isEmailVerified()){
                    db.document("users/" + user.getUid()).get().addOnCompleteListener(navigateUserListener);
                }
            }
            else{
                Log.e("userReloadListener","onComplete: Failed=" + task.getException().getMessage());
                Intent intent = new Intent(SplashPageActivity.this, com.example.myfair.activities.LoginActivity.class);
                startActivity(intent);
            }
        }
    };

    private final OnCompleteListener<DocumentSnapshot> navigateUserListener = new OnCompleteListener<DocumentSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if (task.isSuccessful()) {
                Log.e("navigateUserListener", "onComplete: Success");
                DocumentSnapshot snapshot = task.getResult();
                if (snapshot != null && snapshot.exists()) {
                    User user = new User(snapshot.getData());
                    Intent intent;
                    if (user.profileCreated()) {
                        intent = new Intent(SplashPageActivity.this, com.example.myfair.activities.MainActivity.class);
                    } else {
                        intent = new Intent(SplashPageActivity.this, com.example.myfair.activities.LoginActivity.class);
                        intent.putExtra(User.FIELD_PROFILE_CREATED, false);
                    }
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashPageActivity.this, com.example.myfair.activities.LoginActivity.class);
                    intent.putExtra(User.FIELD_PROFILE_CREATED, false);
                    startActivity(intent);
                    finish();
                }
            }
            else{
                Log.e("navUserList", "onComplete: Failed=" + task.getException().getMessage());
            }
        }
    };

}
