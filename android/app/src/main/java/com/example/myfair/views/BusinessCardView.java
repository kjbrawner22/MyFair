package com.example.myfair.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.myfair.R;
import com.example.myfair.db.Card;

import org.w3c.dom.Text;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

public class BusinessCardView extends CardView {
    private TextView name;
    private TextView company;
    private TextView position;

    public BusinessCardView(@NonNull Context context) {
        super(context);
        initialize(context);
    }

    public BusinessCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public BusinessCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    private void initialize(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_business_card, this);

        name = findViewById(R.id.tvName);
        company = findViewById(R.id.tvCompany);
        position = findViewById(R.id.tvPosition);
    }

    public void setFromCardModel(Card card) {
        setName(card.getValue(Card.FIELD_NAME));
        setCompany(card.getValue(Card.FIELD_COMPANY_NAME));
        setPosition(card.getValue(Card.FIELD_COMPANY_POSITION));
    }

    public void setName(String name) {
        this.name.setText(name);
    }

    public void setCompany(String company) {
        this.company.setText(company);
    }

    public void setPosition(String position) {
        this.position.setText(position);
    }
}
