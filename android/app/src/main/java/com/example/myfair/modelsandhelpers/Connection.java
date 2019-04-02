package com.example.myfair.modelsandhelpers;

import android.graphics.drawable.Drawable;

import com.example.myfair.R;
import com.example.myfair.db.User;

import androidx.annotation.NonNull;

/**
 * Connection object that stores an icon drawable id, a connection type, and its proper name
 */
public class Connection {
    private int icon;
    private String dbKey;
    private String name;

    /**
     * Standard Constructor for the Connection object
     * @param icon - icon's drawable id
     * @param dbKey - String that represents the connection type in the database
     * @param name - String that represents the displayed value for the connection
     */
    public Connection(@NonNull int icon, @NonNull String dbKey, @NonNull String name) {
        this.icon = icon;
        this.dbKey = dbKey;
        this.name = name;
    }

    /**
     * Getter for the icon drawable id
     * @return int for icon's drawable id
     */
    public int getIconId() {
        return icon;
    }

    /**
     * Getter for the database key value
     * @return String
     */
    public String getDbKey() {
        return dbKey;
    }

    /**
     * Getter for the displayed name of the connection
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the current connection list available in the database
     * @return Connection array
     */
    public static Connection[] getConnectionList() {
        return new Connection[]{
                new Connection(R.drawable.ic_twitter, User.FIELD_TWITTER_USERNAME, "Twitter"),
                new Connection(R.drawable.ic_github, User.FIELD_GITHUB_USERNAME, "Github"),
                new Connection(R.drawable.ic_linked_in, User.FIELD_LINKED_IN_USERNAME, "Linked In"),
                new Connection(R.drawable.ic_twitter, User.FIELD_INSTAGRAM_USERNAME, "Instagram")
        };
    }
}
