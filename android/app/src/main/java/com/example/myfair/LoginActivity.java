package com.example.myfair;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        final EditText etEmail = findViewById(R.id.etEmail);
        final EditText etPassword = findViewById(R.id.etPassword);
        Button btnSignIn = findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (email.isEmpty()) {
                    etEmail.setError("Email field is required.");
                    etEmail.requestFocus();
                    return;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    etEmail.setError("Valid email address required.");
                    etEmail.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    etPassword.setError("Password field is required.");
                    etPassword.requestFocus();
                    return;
                }


                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Sign in failure
                            Toast.makeText(view.getContext(), "Incorrect email or password.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        updateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
