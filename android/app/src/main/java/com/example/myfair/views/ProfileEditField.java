package com.example.myfair.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myfair.R;

/**
 * ProfileEditField - Compound view that encapsulates a label with an EditText to edit the value
 *                    associated with that label
 */
public class ProfileEditField extends LinearLayout {

    private TextView label;
    private EditText field;

    /**
     * Default generated constructors with the initialize method appended
     * @param context
     */
    public  ProfileEditField(Context context) {
        super(context);
        initialize(context);
    }

    public ProfileEditField(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public ProfileEditField(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);
    }

    public ProfileEditField(Context context, AttributeSet attrs, int defStyle, int defStyleRes) {
        super(context, attrs, defStyle, defStyleRes);
        initialize(context);
    }

    /**
     * Initialize and inflate the view, then grab the handles for the views needed
     * @param context - app's current context
     */
    private void initialize(Context context) {
        this.setOrientation(VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_profile_edit_field, this);

        label = findViewById(R.id.tvLabel);
        field = findViewById(R.id.etField);
    }

    /**
     * Set the label's string value
     * @param label - String to update the label's value
     */
    public void setLabel(String label) {
            this.label.setText(label);
//        if (label.equals("twitter_username"))
//            this.label.setText("Twitter Username:");
//        else if (label.equals("github_username"))
//            this.label.setText("Github Username:");
//        else if (label.equals("linked_in_username"))
//            this.label.setText("LinkedIn Username:");
//        else if (label.equals("instagram_username"))
//            this.label.setText("Instagram Username:");
//        else if (label.equals("name"))
//            this.label.setText("Name:");
//        else
//            this.label.setText(label);
    }

    /**
     * Getter for the label's String value
     * @return String
     */
    public String getLabel() {
        return label.getText().toString();
    }

    /**
     * Setter for the field's string value
     * @param field - String value to update the field
     */
    public void setField(String field) {
        this.field.setText(field);
    }

    /**
     * Getter for the field's string value
     * @return String
     */
    public String getField() {
        return field.getText().toString();
    }
}
