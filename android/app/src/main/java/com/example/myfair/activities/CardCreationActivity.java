package com.example.myfair.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
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
import com.example.myfair.modelsandhelpers.Upload;
import com.example.myfair.views.UniversityCardView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class CardCreationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "CardCreationActivity";
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;
    private ImageView ivBanner, ivProfile, ivCurrentImage;
    private Uri mImageUri;
    private EditText etName, etCompany, etPosition;
    private String fullName, company, position;
    private Button btnDone;
    private ConstraintLayout lytCompany;
    private static final int PICK_IMAGE_REQUEST = 1;
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

        lytCompany = findViewById(R.id.lytCompany);
        etName = findViewById(R.id.etName);
        etCompany = findViewById(R.id.etCompany);
        etPosition = findViewById(R.id.etPosition);
        lytPreview = findViewById(R.id.lytPreview);

        Button btnCancel = findViewById(R.id.btnCancel);
        btnDone = findViewById(R.id.btnDone);

        btnCancel.setOnClickListener(this);
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

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private View.OnClickListener imageUploadListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ivCurrentImage = (ImageView) view;
            openFileChooser();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.with(this).load(mImageUri).fit().centerInside().into(ivCurrentImage);
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
            case R.id.btnCancel:
                finish();
                break;
            case R.id.btnDone:
                //send info to database
                if (validFields()) {
                    updateData();
                }
                //update back to home fragment
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
     * Helper function responsible for updating the information on a card
     * */
    private void updateData(){
        //TODO: include uploadFile method here somehow
        localCard.setValue(Card.FIELD_CARD_OWNER, user.getUid());
        Log.d("CardCreationLog", "Map for card: " + localCard.getMap());
        localCard.sendToDb(Card.VALUE_NEW_CARD);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(CardCreationActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Log.d("CardCreationLog", "uri, choose image view " + localCard.getMap());
                                    if(ivCurrentImage.getId() == R.id.ivBanner)
                                        localCard.setValue(Card.FIELD_BANNER_URI, uri);
                                    else
                                        localCard.setValue(Card.FIELD_PROFILE_URI, uri);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CardCreationActivity.this, "Upload Error", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }



    private boolean validFields() {
        if(form == 2){
            return validate(etName, etCompany, etPosition);
        }
        else
        {
            return false;
        }
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
