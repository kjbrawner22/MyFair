package com.example.myfair.modelsandhelpers;

import androidx.annotation.NonNull;

import com.example.myfair.R;
import com.example.myfair.db.User;

/**
 * Connection object that stores an icon drawable id, a connection type, and its proper name
 */
public class Connection {
    private int icon;
    private String dbKey;
    private String name;
    private String value;
    private boolean enabled;

    /**
     * Standard Constructor for the Connection object
     * @param icon - icon's drawable id
     * @param dbKey - String that represents the connection type in the database
     * @param name - String that represents the displayed value for the connection
     */
    public Connection(int icon, @NonNull String dbKey, @NonNull String name) {
        this.icon = icon;
        this.dbKey = dbKey;
        this.name = name;
        this.enabled = true;
    }

    /**
     * @param icon - icon's drawable id
     * @param dbKey - String that represents the connection type in the database
     * @param name - String that represents the displayed value for the connection
     * @param enabled - boolean that represents the enabled value for the spinner.
     */
    public Connection(int icon, String dbKey, String name, boolean enabled) {
        this.icon = icon;
        this.dbKey = dbKey;
        this.name = name;
        this.enabled = enabled;
    }

    /**
     * Enable the connection to be selected in the spinner
     * @param enabled - boolean to assign to enable/disable the connection in the spinner
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Getter to determine if the Connection is enabled or not.
     * @return boolean
     */
    public boolean isEnabled() {
        return enabled;
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
                new Connection(R.drawable.ic_linkedin, User.FIELD_LINKED_IN_USERNAME, "Linked In"),
                new Connection(R.drawable.ic_instagram, User.FIELD_INSTAGRAM_USERNAME, "Instagram"),
                new Connection(R.drawable.ic_phone_mobile, User.FIELD_CELL_NUMBER, "Mobile"),
                new Connection(R.drawable.ic_phone_work, User.FIELD_WORK_NUMBER, "Work"),
                new Connection(R.drawable.ic_phone_home, User.FIELD_HOME_NUMBER, "Home")
        };
    }

    /**
     * Check if the field is a phone number
     * @param key - String key reference for the connection data
     * @return boolean indication whether the field is a phone number
     */
    public static boolean isPhoneNumber(String key) {
        switch (key) {
            case User.FIELD_CELL_NUMBER:
            case User.FIELD_HOME_NUMBER:
            case User.FIELD_WORK_NUMBER:
                return true;
            default:
                return false;
        }
    }

    /**
     * Static helper that constructs internet URL from a String username
     * @param key - String value that represents the type of social username
     * @param value - String value that represents the social username
     * @return returns String of constructed URL
     */
    public static String getInternetUrl(String key, String value) {
        switch (key) {
            case User.FIELD_GITHUB_USERNAME:
                return "https://github.com/" + value;
            case User.FIELD_TWITTER_USERNAME:
                return "https://twitter.com/" + value;
            case User.FIELD_LINKED_IN_USERNAME:
                return "https://www.linkedin.com/in/" + value + "/";
            case User.FIELD_INSTAGRAM_USERNAME:
                return "https://www.instagram.com/" + value + "/";
            default:
                return null;
        }
    }

    /**
     * Generic setter for value variable
     * @param value - String value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Generic getter for value variable
     * @return returns
     */
    public String getValue() {
        return this.value;
    }
}
