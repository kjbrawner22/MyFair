package com.example.myfair;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // ensure that the sign-in form is displayed first
        changeForm(R.id.btnSignInForm);
        // grab an instance of the firebase authentication
        mAuth = FirebaseAuth.getInstance();

        //find all the buttons
        Button btnSignIn = findViewById(R.id.btnSignIn);
        Button btnSignUp = findViewById(R.id.btnSignUp);
        Button btnSignUpForm = findViewById(R.id.btnSignUpForm);
        Button btnSignInForm = findViewById(R.id.btnSignInForm);

        //set the click listeners
        btnSignUpForm.setOnClickListener(this);
        btnSignInForm.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
    }

    // Handle all the clicks needed
    @Override
    public void onClick(final View v) {
        //TODO: handle clicks and transfer current listener into here
        int id = v.getId();
        switch (id) {
            case R.id.btnSignIn:
                if (validSignInFields()) {
                    signIn();
                }
                break;
            case R.id.btnSignUp:
                //TODO: sign-up logic here
                break;
            case R.id.btnSignInForm:
            case R.id.btnSignUpForm:
                //change view to sign-up or sign-in form here
                changeForm(id);
                Log.d("CHANGE_FORM", "Changing form...");
                break;
        }
    }

    private boolean validSignInFields() {
        final EditText etEmail = findViewById(R.id.etEmail);
        final EditText etPassword = findViewById(R.id.etPassword);

        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (email.isEmpty()) {
            etEmail.setError("Email field is required.");
            etEmail.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Valid email address required.");
            etEmail.requestFocus();
        } else if (password.isEmpty()) {
            etPassword.setError("Password field is required.");
            etPassword.requestFocus();
        } else {
            return true;
        }

        return false;
    }

    private void signIn() {
        final String email = ((EditText)findViewById(R.id.etEmail)).getText().toString();
        final String password = ((EditText)findViewById(R.id.etPassword)).getText().toString();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success
                    Toast.makeText(LoginActivity.this, "Correct", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Sign in failure
                    Toast.makeText(LoginActivity.this, "Incorrect email or password.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void changeForm(int id) {
        ConstraintLayout lytSignIn = findViewById(R.id.lytSignIn);
        ConstraintLayout lytSignUp = findViewById(R.id.lytSignUp);

        if (id == R.id.btnSignUpForm) {
            lytSignIn.setVisibility(View.GONE);
            lytSignUp.setVisibility(View.VISIBLE);
        } else {
            lytSignIn.setVisibility(View.VISIBLE);
            lytSignUp.setVisibility(View.GONE);
        }
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
