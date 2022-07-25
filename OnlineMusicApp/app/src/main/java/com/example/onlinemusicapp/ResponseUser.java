package com.example.onlinemusicapp;

import android.app.role.RoleManager;
import android.media.MediaRouter;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class ResponseUser {
    @SerializedName("user")
    @Expose
    private Users user;

    @SerializedName("token")
    @Expose
    private String token;

    public Users getUser() {
        return user;
    }
    public void setUser(Users user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }
    public String setToken(String token) {
        this.token = token;
        return token;
    }

    public int getRole() {

        Log.d("so so ", String.valueOf(this.user.getRole().compareTo("Administrators")));
        if (this.user.getRole().compareTo("Administrators") == 0) {
            return MusicAppGlobal.UserMode.Administrator;
        }
        else {
            return MusicAppGlobal.UserMode.User;
        }
    }

    public String getRoleString() {
        return this.user.getRole();
    }
}
