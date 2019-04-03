package com.example.myfair.modelsandhelpers;

import android.graphics.drawable.Drawable;

import com.example.myfair.R;
import com.example.myfair.db.User;

import androidx.annotation.NonNull;

public class Connection {
    private int icon;
    private String dbKey;
    private String name;

    public Connection(@NonNull int icon, @NonNull String dbKey, @NonNull String name) {
        this.icon = icon;
        this.dbKey = dbKey;
        this.name = name;
    }

    public int getIconId() {
        return icon;
    }

    public String getDbKey() {
        return dbKey;
    }

    public String getName() {
        return name;
    }

    public static Connection[] getConnectionList() {
        return new Connection[]{
                new Connection(R.drawable.ic_twitter, User.FIELD_TWITTER_USERNAME, "Twitter"),
                new Connection(R.drawable.ic_twitter, User.FIELD_GITHUB_USERNAME, "Github"),
                new Connection(R.drawable.ic_twitter, User.FIELD_LINKED_IN_USERNAME, "Linked In"),
                new Connection(R.drawable.ic_twitter, User.FIELD_INSTAGRAM_USERNAME, "Instagram")
        };
    }
}
