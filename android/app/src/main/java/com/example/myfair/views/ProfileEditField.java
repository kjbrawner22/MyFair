package com.example.myfair.views;

import android.content.Context;
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

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_profile_edit_field, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        label = findViewById(R.id.tvLabel);
        field = findViewById(R.id.etField);
    }
}
