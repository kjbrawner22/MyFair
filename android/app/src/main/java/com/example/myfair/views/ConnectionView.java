package com.example.myfair.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.myfair.R;
import com.example.myfair.modelsandhelpers.Connection;

/**
 * ConnectionView composed of a TextField to display the value of the connection
 * and a hidden String that stores the connection type
 */
public class ConnectionView extends LinearLayout {
    private ImageButton btnDelete;
    private String connectionType;
    private EditText etField;

    /**
     * below are the constructors that have been appended with the initialize method
     * @param context - app's current context
     */
    public ConnectionView(Context context) {
        super(context);
        initialize(context);
    }

    public ConnectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public ConnectionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);
    }

    /**
     * Custom constructor that builds and adds the view from the given parameters
     * explained below.
     *
     * @param context - app's current context
     * @param parent - view group to add the new ConnectionView to
     * @param connection - takes in a Connection object of the correct connection type
     * @param connectionText - the value for the connection to be added to the EditText field
     */
    public ConnectionView(Context context, ViewGroup parent, Connection connection, String connectionText) {
        super(context);
        initialize(context);
        parent.addView(this);

        connectionType = connection.getDbKey();

        etField.setText(connectionText);
        etField.setCompoundDrawablesWithIntrinsicBounds(connection.getIconId(), 0, 0, 0);
    }

    /**
     * Initialize and inflate the layout, then grab the handles to necessary views
     * @param context - app's current context
     */
    private void initialize(Context context) {
        setOrientation(HORIZONTAL);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_connection, this);

        btnDelete = findViewById(R.id.btnDelete);
        etField = findViewById(R.id.etField);
    }

    /**
     * get the text from the EditText field
     * @return String
     */
    public String getText() {
        return etField.getText().toString();
    }

    /**
     * get the connection type
     * @return String
     */
    public String getConnectionType() {
        return connectionType;
    }

    /**
     * Set the on click listener of the 'delete' button to remove the view from the parent view
     * @param parent - the parent view to delete the item from
     */
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
