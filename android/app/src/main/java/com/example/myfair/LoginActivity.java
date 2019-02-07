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
import android.widget.ProgressBar;
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

    // Handle all the click listeners
    @Override
    public void onClick(final View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnSignIn:
                if (validSignInFields()) {
                    signIn();
                }
                break;
            case R.id.btnSignUp:
                if (validSignUpFields()) {
                    signUp();
                }
                break;
            case R.id.btnSignInForm:
            case R.id.btnSignUpForm:
                //change view to sign-up or sign-in form here
                Log.d("CHANGE_FORM", "Changing form...");
                changeForm(id);
                break;
        }
    }

    // validate the sign-in fields, and return a boolean of the result
    private boolean validSignInFields() {
        final EditText etEmail = findViewById(R.id.etSignInEmail);
        final EditText etPassword = findViewById(R.id.etSignInPassword);

        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();

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
        final String email = ((EditText)findViewById(R.id.etSignInEmail)).getText().toString();
        final String password = ((EditText)findViewById(R.id.etSignInPassword)).getText().toString();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success
                    updateUI();
                } else {
                    // Sign in failure
                    Toast.makeText(LoginActivity.this, "Incorrect email and/or password.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // validate the sign-up fields, and return a boolean of the result
    private boolean validSignUpFields() {
        final EditText etEmail = findViewById(R.id.etSignUpEmail);
        final EditText etPassword = findViewById(R.id.etSignUpPassword);
        final EditText etConfirmPassword = findViewById(R.id.etConfirmPassword);

        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();
        final String confirmPassword = etConfirmPassword.getText().toString();

        if (email.isEmpty()) {
            etEmail.setError("Email field is required.");
            etEmail.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Valid email address required.");
            etEmail.requestFocus();
        } else if (password.isEmpty()) {
            etPassword.setError("Password field is required.");
            etPassword.requestFocus();
        } else if (confirmPassword.isEmpty()) {
            etConfirmPassword.setError("Must confirm your password.");
            etConfirmPassword.requestFocus();
        } else if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("The two entered passwords do not match.");
            etConfirmPassword.requestFocus();
        } else {
            return true;
        }

        return false;
    }

    // sign-up a user in the Firebase authentication system.
    private void signUp() {
        final String email = ((EditText) findViewById(R.id.etSignUpEmail))
                .getText().toString();
        final String password = ((EditText) findViewById(R.id.etSignUpPassword))
                .getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("SIGN_UP_USER", "createUserWithEmail:success");
                            updateUI();
                        } else {
                            Log.d("SIGN_UP_USER", "createUserWithEmail:failure");
                            Toast.makeText(LoginActivity.this,
                                    "Failed to create a new user.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    // change to either the sign-in or sign-up form view
    private void changeForm(int id) {
        ConstraintLayout lytSignIn = findViewById(R.id.lytSignIn);
        ConstraintLayout lytSignUp = findViewById(R.id.lytSignUp);

        if (id == R.id.btnSignUpForm) {
            lytSignIn.setVisibility(View.GONE);
            lytSignUp.setVisibility(View.VISIBLE);
        } else {
            lytSignUp.setVisibility(View.GONE);
            lytSignIn.setVisibility(View.VISIBLE);
        }
    }

    // check auth state on start of application
    @Override
    public void onStart() {
        super.onStart();
        updateUI();
    }

    // check auth state on resume of application
    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    // check auth state, and if the user is authenticated,
    // proceed to the main activity
    private void updateUI() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
