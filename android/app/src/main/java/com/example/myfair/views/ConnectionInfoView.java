package com.example.myfair.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.myfair.R;

public class ConnectionInfoView extends LinearLayout {
    private ImageView ivLogo;
    private TextView tvText;

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

    public ConnectionInfoView(Context context, ViewGroup parent, int logoResourceId, String text) {
        super(context);
        initialize(context);
        parent.addView(this);
        ivLogo.setImageResource(logoResourceId);
        tvText.setText(text);
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
        tvText = findViewById(R.id.tvText);
    }

    public void setText(String text) {
        tvText.setText(text);
    }

    public void setImage(int resourceId) {
        ivLogo.setImageResource(resourceId);
    }
}
