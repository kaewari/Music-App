package com.example.onlinemusicapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaylistAnswerResponse {

    @SerializedName("id")
    @Expose
    private int _id;

    @SerializedName("name")
    @Expose
    private String _playlistName;

    @SerializedName("songs")
    @Expose
    private List<SongAnswerResponse> _songsList;

    @SerializedName("createdDate")
    @Expose
    private String _createdDate;

    @SerializedName("userId")
    @Expose
    private int _userID;


    public int get_id() {return this._id;}
    public void set_id(int id) {this._id = id;}

    public String get_playlistName() {return this._playlistName;}
    public void set_playlistName(String playlistName) {this._playlistName = playlistName;}

    public List<SongAnswerResponse> get_songsList() {return this._songsList;}
    public void set_songsList(List<SongAnswerResponse> songsList) {this._songsList = songsList;}

    public String get_createdDate() {return this._createdDate;}
    public void set_createdDate(String createdDate) {this._createdDate = createdDate;}

    public int get_userID() {return this._userID;}
    public void set_userID(int userID) {this._userID = userID;}

}
