package com.example.myfair.views;

import android.content.Context;
import android.media.Image;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.myfair.R;
import com.example.myfair.modelsandhelpers.Connection;

public class ConnectionView extends LinearLayout {
    private ImageButton btnDelete;
    private String connectionType;
    private EditText etField;

    public ConnectionView(Context context) {
        super(context);
        initialize(context);
    }

    public ConnectionView(Context context, ViewGroup parent, Connection connection, String connectionText) {
        super(context);
        initialize(context);
        parent.addView(this);

        connectionType = connection.getDbKey();

        etField.setText(connectionText);
        etField.setCompoundDrawablesWithIntrinsicBounds(connection.getIconId(), 0, 0, 0);
    }

    public ConnectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public ConnectionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);
    }

    private void initialize(Context context) {
        setOrientation(HORIZONTAL);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_connection, this);

        btnDelete = findViewById(R.id.btnDelete);
        etField = findViewById(R.id.etField);
    }

    public String getText() {
        return etField.getText().toString();
    }

    public String getConnectionType() {
        return connectionType;
    }

    // set delete listener to remove view from the supplied parent
    public void setOnDeleteListener(final ViewGroup parent) {
        final ConnectionView temp = this;
        btnDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.removeView(temp);
            }
        });
    }
}
