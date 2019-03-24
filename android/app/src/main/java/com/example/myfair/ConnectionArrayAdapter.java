package com.example.myfair;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myfair.modelsandhelpers.Connection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ConnectionArrayAdapter extends ArrayAdapter<Connection> {
    public ConnectionArrayAdapter(@NonNull Context context) {
        super(context, R.layout.spinner_icon_item, R.id.tvName, Connection.getConnectionList());
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
        Connection connection = getItem(position);

        ImageView imgIcon = view.findViewById(R.id.imgIcon);
        TextView textView = view.findViewById(R.id.tvName);

        imgIcon.setImageResource(connection.getIconId());

        textView.setVisibility(View.GONE);
        textView.setText(connection.getName());

        return view;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LinearLayout view = (LinearLayout) super.getDropDownView(position, convertView, parent);
        Connection connection = getItem(position);

        ImageView imgIcon = view.findViewById(R.id.imgIcon);
        TextView textView = view.findViewById(R.id.tvName);

        imgIcon.setImageResource(connection.getIconId());

        textView.setText(connection.getName());
        textView.setVisibility(View.VISIBLE);

        return view;
    }
}
