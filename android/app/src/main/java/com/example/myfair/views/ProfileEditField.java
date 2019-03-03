package com.example.myfair.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myfair.R;

public class ProfileEditField extends LinearLayout {

    private TextView label;
    private EditText field;

    public  ProfileEditField(Context context) {
        super(context);
        initializeViews(context);
    }

    public ProfileEditField(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public ProfileEditField(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeViews(context);
    }

    public ProfileEditField(Context context, AttributeSet attrs, int defStyle, int defStyleRes) {
        super(context, attrs, defStyle, defStyleRes);
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        this.setOrientation(VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_profile_edit_field, this);

        label = findViewById(R.id.tvLabel);
        field = findViewById(R.id.etField);
    }

    public void setLabel(String label) {
        this.label.setText(label);
    }

    public String getLabel() {
        return label.getText().toString();
    }

    public void setField(String field) {
        this.field.setText(field);
    }

    public String getField() {
        return field.getText().toString();
    }
}
