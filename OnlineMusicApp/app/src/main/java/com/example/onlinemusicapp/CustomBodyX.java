package com.example.onlinemusicapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomBodyX {
    @SerializedName("name")
    @Expose
    private String _x_name;

    @SerializedName("songId")
    @Expose
    private int _x_songId;

    public void set_name(String _x_name) { this._x_name = _x_name; }
    public String get_name() { return this._x_name; }

    public void set_songId(int _x_songId) { this._x_songId = _x_songId; }
    public int get_songId() { return this._x_songId; }
}
