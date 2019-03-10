package com.example.myfair.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.myfair.R;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;

public class SocialUsernameField extends LinearLayout {
    Spinner spinner;
    EditText field;

    public SocialUsernameField(Context context) {
        super(context);
        initialize(context);
    }

    public SocialUsernameField(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public SocialUsernameField(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    public SocialUsernameField(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context);
    }

    private void initialize(Context context) {
        setOrientation(HORIZONTAL);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_social_username_field, this);

        spinner = findViewById(R.id.spinner);
        field = findViewById(R.id.etField);
    }

    // must be called after the instance is added to a layout
    private void setSpinnerAdapter(String[] options) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}
