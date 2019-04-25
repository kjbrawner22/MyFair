package com.example.myfair.views;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.myfair.R;
import com.example.myfair.db.Card;
import com.example.myfair.modelsandhelpers.Connection;

/**
 * Constructs custom view for the connection
 * and document information data
 */
public class ConnectionInfoView extends LinearLayout {
    private ImageView ivLogo, ivSelected;
    private TextView tvText;
    private String URL;
    private boolean checked;

    /**
     * Default constructor
     * @param context - desired context
     */
    public ConnectionInfoView(Context context) {
        super(context);
        initialize(context);
    }

    public ConnectionInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public ConnectionInfoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    public ConnectionInfoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context);
    }

    /**
     * Custom consturctor that initializes the view using specific data
     * @param context - desired context
     * @param parent - Layout for desired ViewGroup parent object
     * @param logoResourceId - Int for desired image resource
     * @param text - String to set the title of the view
     */
    public ConnectionInfoView(Context context, ViewGroup parent, int logoResourceId, String text) {
        super(context);
        initialize(context);
        parent.addView(this);
        ivLogo.setImageResource(logoResourceId);
        tvText.setText(text);
        checked = false;
    }

    /**
     * Custom consturctor that initializes the view using custom connection object
     * @param context - desired context
     * @param parent - layout for desired ViewGroup
     * @param connection Connection object for base data
     */
    public ConnectionInfoView(Context context, ViewGroup parent, Connection connection) {
        super(context);
        initialize(context);
        parent.addView(this);
        ivLogo.setImageResource(connection.getIconId());
        tvText.setText(connection.getValue());
        checked = false;
    }

    /**
     * initialize and inflate the view, then get handles to the view components needed
     * @param context - app's current context
     */
    private void initialize(Context context) {
        setOrientation(HORIZONTAL);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_connection_info, this);

        ivLogo = findViewById(R.id.ivLogo);
        ivSelected = findViewById(R.id.ivSelected);
        tvText = findViewById(R.id.tvText);
    }

    /**
     * Sets the radio button asset to checked state
     * @param checked - boolean variable that specifies button status
     */
    public void setChecked(boolean checked) {
        Resources resources = getResources();
        CardView toplevel = findViewById(R.id.toplevel);
        this.checked = checked;
        if (!checked) {
            ivSelected.setImageResource(R.drawable.ic_selected_no);
            toplevel.setCardBackgroundColor(resources.getColor(R.color.default_indicator_light_off));
        } else {
            ivSelected.setImageResource(R.drawable.ic_selected_yes);
            toplevel.setCardBackgroundColor(resources.getColor(R.color.colorPrimary));
        }
    }

    /**
     * Hides the radio button image
     */
    public void hideSelectors() {
        Resources resources = getResources();
        CardView toplevel = findViewById(R.id.toplevel);
        findViewById(R.id.selected).setVisibility(GONE);
        toplevel.setCardBackgroundColor(resources.getColor(R.color.colorPrimary));
    }

    /*
        Setters and Getters
     */

    public void setText(String text) {
        tvText.setText(text);
    }
    public String getText() { return tvText.getText().toString(); }
    public void setImage(int resourceId) {
        ivLogo.setImageResource(resourceId);
    }
    public void setURL(String URL) {
        this.URL = URL;
    }
    public String getURL(){
        return this.URL;
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        findViewById(R.id.toplevel).setOnClickListener(l);
    }

}
