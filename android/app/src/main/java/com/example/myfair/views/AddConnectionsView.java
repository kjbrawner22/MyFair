package com.example.myfair.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.example.myfair.R;
import com.example.myfair.db.User;

import androidx.annotation.Nullable;

public class AddConnectionsView extends LinearLayout {
    private Spinner spinner;
    private EditText field;
    private ImageButton btnAdd;

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

    // must be called after the instance is added to a layout
    // will use default CONNECTIONS_LIST array in the User object if
    // no
    public void setSpinnerAdapter(String[] options) {
        if (options == null) {
            options = User.CONNECTIONS_LIST;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void setListView(ScrollView connectionsView) {
        btnAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //ConnectionView connection = new Connection(spinner.getSelectedItem().toString());
                //connectionsView.addChild(connection);
            }
        });
    }
}
