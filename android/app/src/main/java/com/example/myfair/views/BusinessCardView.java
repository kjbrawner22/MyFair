package com.example.myfair.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfair.R;
import com.example.myfair.db.Card;

import org.w3c.dom.Text;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

public class BusinessCardView extends CardView implements View.OnClickListener {
    private TextView name;
    private TextView company;
    private TextView position;
    private Context context;

    public BusinessCardView(@NonNull Context context) {
        super(context);
        this.context = context;
        initialize(context);
    }

    public BusinessCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initialize(context);
    }

    public BusinessCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initialize(context);
    }

    public void onClick(View arg0){
        Toast.makeText(context, "View clicked.", Toast.LENGTH_SHORT).show();
    }

    private void initialize(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_business_card, this);

        name = findViewById(R.id.tvName);
        company = findViewById(R.id.tvCompany);
        position = findViewById(R.id.tvPosition);

        setOnClickListener(this);
    }

    public void setMargins() {
        MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();

        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());

        params.setMargins(margin, 0,margin, margin);

        setLayoutParams(params);

        Log.d("MARGINS", "" + margin);
    }

    public void setFromCardModel(Card card) {
        if(card.getValue(Card.FIELD_TYPE).equals(Card.VALUE_TYPE_BUSINESS)) {
            setName(card.getValue(Card.FIELD_NAME));
            setCompany(card.getValue(Card.FIELD_COMPANY_NAME));
            setPosition(card.getValue(Card.FIELD_COMPANY_POSITION));
        }
        else{
            setName(card.getValue(Card.FIELD_NAME));
            setCompany(card.getValue(Card.FIELD_UNIVERSITY_NAME));
            setPosition(card.getValue(Card.FIELD_UNIVERSITY_MAJOR));
        }
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
