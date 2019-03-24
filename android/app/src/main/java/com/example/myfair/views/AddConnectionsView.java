package com.example.myfair.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.example.myfair.ConnectionArrayAdapter;
import com.example.myfair.R;
import com.example.myfair.db.User;
import com.example.myfair.modelsandhelpers.Connection;

import androidx.annotation.Nullable;

public class AddConnectionsView extends LinearLayout {
    private Spinner spinner;
    private EditText field;
    private ImageButton btnAdd;

    private final String TAG = "AddConnectionsView";

    public AddConnectionsView(Context context) {
        super(context);
        initialize(context);
    }

    public AddConnectionsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public AddConnectionsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    public AddConnectionsView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context);
    }

    private void initialize(Context context) {
        setOrientation(HORIZONTAL);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_add_connections, this);

        spinner = findViewById(R.id.spinner);
        field = findViewById(R.id.etField);
        btnAdd = findViewById(R.id.btnAdd);
    }

    public void setAddConnectionClickListener(final Context context, final ViewGroup connectionsView) {
        btnAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "connection field: " + field);
                final String connectionText = field.getText().toString();
                if (!connectionText.isEmpty()) {
                    ConnectionView connection = new ConnectionView(context, connectionsView, (Connection)spinner.getSelectedItem(), connectionText);
                    connection.setOnDeleteListener(connectionsView);
                }
            }
        });
    }

    // must be called after the instance is added to a layout
    // will use default CONNECTIONS_LIST array in the User object if
    // no options are provided
    public void setSpinnerAdapter() {
        ConnectionArrayAdapter adapter = new ConnectionArrayAdapter(getContext());
        spinner.setAdapter(adapter);
    }
}