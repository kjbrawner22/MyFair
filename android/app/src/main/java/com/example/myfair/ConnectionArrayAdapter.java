package com.example.myfair;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myfair.modelsandhelpers.Connection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * ArrayAdapter that handles the spinner items for the AddConnectionsView
 */
public class ConnectionArrayAdapter extends ArrayAdapter<Connection> {

    /**
     * standard constructor with custom layout view
     * @param context - the current context of the app
     */
    public ConnectionArrayAdapter(@NonNull Context context) {
        super(context, R.layout.spinner_icon_item, R.id.tvName, Connection.getConnectionList());
    }

    /**
     * @param position - position of the view/object in the array
     * @param convertView - view to pass into the super method to modify
     * @param parent parent view holding the view shown when the dropdown isn't active
     * @return View to be displayed on the static spinner
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
        Connection connection = getItem(position);

        if (connection == null) {
            return view;
        }

        ImageView imgIcon = view.findViewById(R.id.imgIcon);
        TextView textView = view.findViewById(R.id.tvName);

        imgIcon.setImageResource(connection.getIconId());

        textView.setVisibility(View.GONE);
        textView.setText(connection.getName());

        return view;
    }

    /**
     * Utilize the Connection's 'isEnabled' method to determine if the item is enabled or not
     * @param position - item's position in the spinner
     * @return boolean result
     */
    @Override
    public boolean isEnabled(int position) {
        Connection connection = getItem(position);
        if (connection != null) {
            return connection.isEnabled();
        }

        return false;
    }

    /**
     * @param position - position of the view/object in the array
     * @param convertView - view to pass into the super method to modify
     * @param parent - parent view holding the dropdown views
     * @return View to be displayed in the dropdown
     */
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LinearLayout view = (LinearLayout) super.getDropDownView(position, convertView, parent);
        Connection connection = getItem(position);

        if (connection == null) {
            return view;
        }

        ImageView imgIcon = view.findViewById(R.id.imgIcon);
        TextView textView = view.findViewById(R.id.tvName);

        imgIcon.setImageResource(connection.getIconId());

        textView.setText(connection.getName());
        textView.setVisibility(View.VISIBLE);

        return view;
    }
}
