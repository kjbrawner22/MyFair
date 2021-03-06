package com.example.myfair.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myfair.R;
import com.example.myfair.db.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * LoginActivity that handles the authentication and logic dealing with user sign in/up/etc.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;

    /**
     * Standard overriden onCreate method, grab handles and initialize view
     * @param savedInstanceState application instance's state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // ensure that the sign-in form is displayed first
        ConstraintLayout lytEmailVerification = findViewById(R.id.lytEmailVerification);
        lytEmailVerification.setVisibility(View.GONE);
        changeForm(R.id.btnSignInForm);

        // grab an instance of the firebase authentication
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();

        //find all the buttons
        Button btnSignIn = findViewById(R.id.btnSignIn);
        Button btnSignUp = findViewById(R.id.btnSignUp);
        Button btnSignUpForm = findViewById(R.id.btnSignUpForm);
        Button btnSignInForm = findViewById(R.id.btnSignInForm);
        Button btnResendEmail = findViewById(R.id.btnResend);
        Button btnVerifyDone = findViewById(R.id.btnVerifyDone);
        ImageButton btnResetPasswordForm = findViewById(R.id.btnResetPasswordForm);
        Button btnResetPassword = findViewById(R.id.btnResetPassword);

        //set the click listeners
        btnSignUpForm.setOnClickListener(this);
        btnSignInForm.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
        btnResendEmail.setOnClickListener(this);
        btnVerifyDone.setOnClickListener(this);
        btnResetPasswordForm.setOnClickListener(this);
        btnResetPassword.setOnClickListener(this);
    }

    /**
     * onClick override to handle all the click listeners
     * NOTE: Must implement View.OnClickListener to work
     * @param v - view that was clicked
     */
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
            case R.id.btnResend:
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    user.reload();
                    if (!user.isEmailVerified()) {
                        sendVerificationEmail(user);
                    } else {
                        updateUI();
                    }
                }
                break;
            case R.id.btnVerifyDone:
                updateUI();
                break;
            case R.id.btnSignInForm:
            case R.id.btnSignUpForm:
                //change view to sign-up or sign-in form here
                Log.d("CHANGE_FORM", "Changing form...");
                changeForm(id);
                break;
            case R.id.btnResetPasswordForm:
                EditText etEmail = findViewById(R.id.etSignInEmail);
                showPasswordResetView(etEmail.getText().toString());
                break;
            case R.id.btnResetPassword:
                resetPassword();
                break;
        }
    }

    /**
     * Override the back button to change to the Sign in form.
     */
    @Override
    public void onBackPressed() {
        changeForm(R.id.btnSignInForm);
    }

    /**
     * validate the sign-in fields
     * @return boolean success or failure
     */
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

    /**
     * Attempt to sign the user in with firebase
     */
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

    /**
     * validate the sign-up fields
     * @return boolean success or failure
     */
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

    /**
     * sign-up a user in the Firebase authentication system.
     */
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
                            sendVerificationEmail(mAuth.getCurrentUser());
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

    /**
     * send a verification email to the current user
     * @param user - FirebaseUser object
     */
    private void sendVerificationEmail(FirebaseUser user) {
        if (user == null) {
            Toast.makeText(LoginActivity.this,
                    "Error occurred. Please try again later.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this,
                            "Verification email sent!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this,
                            "Email couldn't be sent at this time. Please try again later.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * change to either the sign-in, sign-up, or ResetPassword form view.
     * Set the other 2 views to GONE so no click listeners from the invisible views are triggered.
     * @param id - id of form to change to.
     */
    private void changeForm(int id) {
        ConstraintLayout lytSignIn = findViewById(R.id.lytSignIn);
        ConstraintLayout lytSignUp = findViewById(R.id.lytSignUp);
        ConstraintLayout lytResetPassword = findViewById(R.id.lytResetPassword);

        switch (id) {
            case R.id.btnSignUpForm:
                lytResetPassword.setVisibility(View.GONE);
                lytSignIn.setVisibility(View.GONE);
                lytSignUp.setVisibility(View.VISIBLE);
                break;
            case R.id.btnSignInForm:
                lytResetPassword.setVisibility(View.GONE);
                lytSignUp.setVisibility(View.GONE);
                lytSignIn.setVisibility(View.VISIBLE);
                break;
            case R.id.btnResetPasswordForm:
                lytSignIn.setVisibility(View.GONE);
                lytSignUp.setVisibility(View.GONE);
                lytResetPassword.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Show the PasswordReset form view
     * @param email String
     */
    private void showPasswordResetView(String email) {
        if (!email.isEmpty()) {
            EditText etEmail = findViewById(R.id.etResetEmail);
            etEmail.setText(email);
        }

        changeForm(R.id.btnResetPasswordForm);
    }


    /**
     * Attempt to send password reset email through Firebase auth system.
     */
    private void resetPassword() {
        EditText etEmail = findViewById(R.id.etResetEmail);
        final String email = etEmail.getText().toString();

        if (email.isEmpty()) {
            etEmail.setError("Email field is required.");
            etEmail.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Valid email address required.");
            etEmail.requestFocus();
        } else {
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d("PASSWORD_RESET", "Email sent.");
                        Toast.makeText(LoginActivity.this,
                                "An email has been sent to " + email
                                        + ". Please check your inbox.", Toast.LENGTH_LONG).show();
                        changeForm(R.id.btnSignInForm);
                    } else {
                        //TODO: add some error notification for user here.
                        Log.d("PASSWORD_RESET", "Failure.");
                    }
                }
            });
        }
    }

    /**
     * Show the email verification view.
     * @param email String
     */
    private void showEmailVerificationView(String email) {
        ConstraintLayout lytSignIn = findViewById(R.id.lytSignIn);
        ConstraintLayout lytSignUp = findViewById(R.id.lytSignUp);
        ConstraintLayout lytEmailVerification = findViewById(R.id.lytEmailVerification);
        TextView tvInfo = findViewById(R.id.tvInfo);

        lytSignIn.setVisibility(View.GONE);
        lytSignUp.setVisibility(View.GONE);

        String info = "We've sent an email to " + email + ".\nPlease check your inbox.";
        tvInfo.setText(info);

        lytEmailVerification.setVisibility(View.VISIBLE);
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

    /**
     * after user's information is reloaded from firebase, attempt to sign them in
     * and check if the email is verified and their profile is created
     */
    private final OnCompleteListener<Void> userReloadListener = new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if (task.isSuccessful()) {
                if (user.isEmailVerified()) {
                    Log.d("UPDATE_UI", "User is email verified and signed in");
                    db.document("users/" + user.getUid()).get()
                            .addOnCompleteListener(navigateUserListener);
                } else {
                    Log.d("UPDATE_UI", "User is not email verified.");
                    showEmailVerificationView(user.getEmail());
                }
            }
        }
    };

    /**
     * logic to navigate user to either profile creation or the main activity
     */
    private final OnCompleteListener<DocumentSnapshot> navigateUserListener = new OnCompleteListener<DocumentSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if (task.isSuccessful()) {
                DocumentSnapshot snapshot = task.getResult();
                if (snapshot != null && snapshot.exists()) {
                    User user = new User(snapshot.getData());
                    Intent intent;
                    if (user.profileCreated()) {
                        intent = new Intent(LoginActivity.this, MainActivity.class);
                    } else {
                        intent = new Intent(LoginActivity.this, ProfileCreationActivity.class);
                        intent.putExtra(User.FIELD_PROFILE_CREATED, false);
                    }
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(LoginActivity.this, ProfileCreationActivity.class);
                    intent.putExtra(User.FIELD_PROFILE_CREATED, false);
                    startActivity(intent);
                    finish();
                }
            } else {
                Log.d(TAG, "Couldn't access database.");
            }
        }
    };


    /**
     * Trigger the user's data to reload and then begin the authentication process with
     * the above listeners.
     */
    private void updateUI() {
        user = mAuth.getCurrentUser();
        if (user != null) {
            user.reload().addOnCompleteListener(userReloadListener);
        }
    }
}
