package com.example.onlinemusicapp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MusicAppGlobal extends Application {

    public static class UserMode {
        static final int Administrator = 1;
        static final int User = 0;
    }
    private static MusicAppGlobal instance;
    private static int userMode;

    static String accessToken;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        accessToken = retrieveTokenFromSharedPrefs();
        userMode = retrieveUserModeFromSharedPrefs();
    }

    public static MusicAppGlobal getInstance() { return instance;}

    private String retrieveTokenFromSharedPrefs() {
        //get from shared prefs
        //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences preferences = getSharedPreferences("MyMusicAppGlobal", Context.MODE_PRIVATE);
        return preferences.getString("user-access-token", null);
    }

    private int retrieveUserModeFromSharedPrefs() {
        //get from shared prefs
        //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences preferences = getSharedPreferences("MyMusicAppGlobal", Context.MODE_PRIVATE);
        return preferences.getInt("user-access-mode", UserMode.User);
    }

    public void setAccessToken(String token) {
        accessToken = token;
        //set into shared prefs
        //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences preferences = this.getSharedPreferences("MyMusicAppGlobal", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user-access-token","Bearer " + token);
        editor.apply();
    }
    public void setAccessMode(int mode) {
        SharedPreferences preferences = this.getSharedPreferences("MyMusicAppGlobal", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("user-access-mode", mode);
        editor.apply();
    }

    public void removeAccessToken() {
        //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences preferences = getSharedPreferences("MyMusicAppGlobal", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("user-access-token");
        editor.apply();
    }

    public void removeAccessMode() {
        SharedPreferences preferences = this.getSharedPreferences("MyMusicAppGlobal", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("user-access-mode");
        editor.apply();
    }

    public String getAccessToken() { return accessToken; }

    public int getUserMode() { return userMode; }

    public boolean hasLoggedIn() {
        return getAccessToken() != null;
    }
}
