package com.example.myfair.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfair.R;
import com.example.myfair.db.Card;
import com.example.myfair.db.CardCreationListener;
import com.example.myfair.db.FirebaseDatabase;
import com.example.myfair.views.UniversityCardView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

public class CardCreationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "CardCreationActivity";
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;
    private ImageView ivBanner, ivProfile, ivCurrentImage;
    private Uri bannerUri, profileUri;
    private EditText etName, etCompany, etPosition;
    private String fullName, company, position;
    private Button btnDone;
    private ConstraintLayout lytCompany;
    private static final int PICK_BANNER_REQUEST = 1, PICK_PROFILE_REQUEST = 2;
    private LinearLayout lytPreview;
    private FirebaseDatabase database;
    private FirebaseUser user;
    private Card localCard;
    int form;

    /**
     * Standard onCreate override. Finds needed handles and initializes view.
     * @param savedInstanceState App's saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_creation);

        setupToolbar();

        database = new FirebaseDatabase();
        user = FirebaseAuth.getInstance().getCurrentUser();
        localCard = new Card();
        localCard.addCardCreationListener(new CardCreationListener() {
            @Override
            public void cardCreated() {
                finish();
            }
        });

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");

        lytCompany = findViewById(R.id.lytGeneralInfo);
        etName = findViewById(R.id.etName);
        etCompany = findViewById(R.id.etCompany);
        etPosition = findViewById(R.id.etPosition);
        lytPreview = findViewById(R.id.lytPreview);

        btnDone = findViewById(R.id.btnDone);

        btnDone.setOnClickListener(this);

        UniversityCardView v = new UniversityCardView(this, "empty", null);
        lytPreview.addView(v);
        v.setMargins();

        ivBanner = v.findViewById(R.id.ivBanner);
        ivProfile = v.findViewById(R.id.ivProfile);
        etName.addTextChangedListener(createTextWatcher(v.getNameView(), Card.FIELD_NAME));
        etCompany.addTextChangedListener(createTextWatcher(v.getUniversityView(), Card.FIELD_UNIVERSITY_NAME));
        etPosition.addTextChangedListener(createTextWatcher(v.getMajorView(), Card.FIELD_UNIVERSITY_MAJOR));
        ivBanner.setOnClickListener(imageUploadListener);
        ivProfile.setOnClickListener(imageUploadListener);

        changeForm(2);
        //initialize contents of text boxes to values inside database
    }

    private void openFileChooser(int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, requestCode);
    }

    private View.OnClickListener imageUploadListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.ivBanner) {
                openFileChooser(PICK_BANNER_REQUEST);
            } else {
                openFileChooser(PICK_PROFILE_REQUEST);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_BANNER_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            bannerUri = data.getData();

            Picasso.with(this).load(bannerUri).fit().centerInside().into(ivBanner);
        } else if (requestCode == PICK_PROFILE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            profileUri = data.getData();

            Picasso.with(this).load(profileUri).fit().centerInside().into(ivProfile);
        }
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        Log.d("ACTION_BAR_CARD_VIEWING", "Actionbar: " + actionBar);
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    /**
     * onClick override to handle all click listeners.
     * @param v - view that was clicked
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnDone:
                //send info to database
                if (validFields()) {
                    updateData();
                }
                //update back to home fragment
                break;
            default:
                break;
        }
    }

    private TextWatcher createTextWatcher(TextView textView, String cardField) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textView.setText(charSequence.toString());
                localCard.setValue(cardField, charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }

    /**
     * Helper function responsible for uploading photos and updating the information on a card
     * */
    private void updateData(){
        localCard.setValue(Card.FIELD_CARD_OWNER, user.getUid());
        Log.d("CardCreationLog", "Map for card: " + localCard.getMap());
        if (bannerUri != null) {
            uploadImage(bannerUri, Card.FIELD_BANNER_URI);
        } else {
            localCard.setValue(Card.FIELD_BANNER_URI, Card.VALUE_DEFAULT_IMAGE);
        }
        if (profileUri != null) {
            uploadImage(profileUri, Card.FIELD_PROFILE_URI);
        } else {
            localCard.setValue(Card.FIELD_PROFILE_URI, Card.VALUE_DEFAULT_IMAGE);
        }
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!localCard.containsKey(Card.FIELD_PROFILE_URI) || !localCard.containsKey(Card.FIELD_BANNER_URI)) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Log.e("CARD_CREATION_LOG", "Error in sleep: ", e.getCause());
                    }
                }
                localCard.sendToDb(Card.VALUE_NEW_CARD);
            }
        });

        t.start();
    }

    private void uploadImage(Uri uri, String cardField) {
        StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                + "." + getFileExtension(uri));

        mUploadTask = fileReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(CardCreationActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("CardCreationLog", "uri, choose image view " + localCard.getMap());
                        localCard.setValue(cardField, uri.toString());
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CardCreationActivity.this, "Upload Error", Toast.LENGTH_SHORT).show();
                localCard.setValue(cardField, Card.VALUE_DEFAULT_IMAGE);
            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private boolean validFields() {
        if(form == 2){
            return validate(etName, etCompany, etPosition);
        }

        return false;
    }

    private boolean validate(EditText etOne, EditText etTwo, EditText etThree){
        if (etOne.getText().toString().isEmpty()) {
            etOne.setError("Name is required.");
            etOne.requestFocus();
            return false;
        } else if (etTwo.getText().toString().isEmpty()) {
            etTwo.setError("Company Name is required.");
            etTwo.requestFocus();
            return false;
        } else if (etThree.getText().toString().isEmpty()) {
            etThree.setError("Position is required.");
            etThree.requestFocus();
            return false;
        }
        return true;
    }


    /**
     * get the form's state.
     * @return int state value
     */
    private int getForm(){
        if (lytCompany.getVisibility() == View.VISIBLE) return 2;
        else return 3;
    }

    /**
     * change form view
     * @param formID - state of the desired view setting
     */
    private void changeForm(int formID){
        form = formID;
        switch(formID){
            case 2:
                btnDone.setVisibility(View.VISIBLE);
                etName.setVisibility(View.VISIBLE);
                lytCompany.setVisibility(View.VISIBLE);
                break;
            default:
                Log.d("ChangeFormLog", "Form Not Yet Implemented");
        }
    }
}
