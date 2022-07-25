package com.example.onlinemusicapp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SongAnswerResponse implements Serializable, Parcelable {

    @SerializedName("id")
    @Expose
    private int _id;

    @SerializedName("name")
    @Expose
    private String _songName;

    @SerializedName("path")
    @Expose
    private String _path;

    @SerializedName("lyricPath")
    @Expose
    private String _lyricPath;

    @SerializedName("singer")
    @Expose
    private SingerAnswerResponse _singer;

    @SerializedName("verified")
    @Expose
    private int _verified;

    @SerializedName("createdBy")
    @Expose
    private int _createdBy;


    protected SongAnswerResponse(Parcel in) {
        _id = in.readInt();
        _songName = in.readString();
        _path = in.readString();
        _lyricPath = in.readString();
        _verified = in.readInt();
        _createdBy = in.readInt();
    }

    public static final Creator<SongAnswerResponse> CREATOR = new Creator<SongAnswerResponse>() {
        @Override
        public SongAnswerResponse createFromParcel(Parcel in) {
            return new SongAnswerResponse(in);
        }

        @Override
        public SongAnswerResponse[] newArray(int size) {
            return new SongAnswerResponse[size];
        }
    };

    public int get_id() {return this._id;}
    public void set_id(int id) {this._id = id;}

    public String get_songName() {return this._songName;}
    public void set_songName(String songName) {this._songName = songName;}

    public String get_path() {return this._path;}
    public void set_path(String path) {this._path = path;}

    public String get_lyricPath() {return this._lyricPath;}
    public void set_lyricPath(String lyricPath) {this._lyricPath = lyricPath;}

    public SingerAnswerResponse get_singer() {return this._singer;}
    public void set_singer(SingerAnswerResponse singer) {this._singer = singer;}

    public int get_verified() {return this._verified;}
    public void set_verified(int verified) {this._verified = verified;}

    public int get_createdBy() {return this._createdBy;}
    public void set_createdBy(int createdBy) {this._createdBy = createdBy;}

    public static List<SongAnswerResponse> getCopyListSongAnswerResponse(List<SongAnswerResponse> send) {
        if (send != null && send.size() > 0) {
            List<SongAnswerResponse> value = new ArrayList<>();
            for (SongAnswerResponse song : send) {
                value.add(song);
            }
            return value;
        }
        else {
            return new ArrayList<>();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeString(_songName);
        dest.writeString(_path);
        dest.writeString(_lyricPath);
        dest.writeInt(_verified);
        dest.writeInt(_createdBy);
    }
}
