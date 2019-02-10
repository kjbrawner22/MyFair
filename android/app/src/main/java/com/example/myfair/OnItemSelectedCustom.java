package com.example.myfair;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class OnItemSelectedCustom implements OnItemSelectedListener {

    final private String TAG = "OnItemSelectedCustom";

    public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
        Log.i(TAG, "OnItemListener successfully updated!"
                    + parent.getItemAtPosition(pos).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}