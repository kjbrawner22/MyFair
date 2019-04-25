package com.example.myfair.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.Nullable;

import com.example.myfair.ConnectionArrayAdapter;
import com.example.myfair.R;
import com.example.myfair.modelsandhelpers.Connection;

/**
 * Compound view to display a spinner and an edit field that correspond to data
 * being entered for the social connections
 */
public class AddConnectionsView extends LinearLayout {
    private Spinner spinner;
    private EditText field;
    private ImageButton btnAdd;

    private final String TAG = "AddConnectionsView";

    /**
     * The below are standard constructors that redirect to the initialize method
     * @param context - app's current context
     */
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

    /**
     * initialize and inflate the view, then get handles to the view components needed
     * @param context - app's current context
     */
    private void initialize(Context context) {
        setOrientation(HORIZONTAL);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_add_connections, this);

        spinner = findViewById(R.id.spinner);
        field = findViewById(R.id.etField);
        btnAdd = findViewById(R.id.btnAdd);
    }

    /**
     * Set the click listener for when the 'add' button is clicked
     * @param context - current context of the app
     * @param connectionsView - view group to add the new ConnectionView(s) to
     */
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

    /**
     * set the spinner adapter to a new instance of ConnectionArrayAdapter
     * NOTE: should be called after the instance is added to a layout,
     *       otherwise the spinner won't get the adapter
     */
    public void setSpinnerAdapter() {
        if (getParent() != null) {
            ConnectionArrayAdapter adapter = new ConnectionArrayAdapter(getContext());
            spinner.setAdapter(adapter);
        }
    }
}
