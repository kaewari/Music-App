package com.example.onlinemusicapp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SOSong implements Serializable, Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("lyricPath")
    @Expose
    private Object lyricPath;
    @SerializedName("singer")
    @Expose
    private Singer singer;
    @SerializedName("verified")
    @Expose
    private Integer verified;
    @SerializedName("createdBy")
    @Expose
    private Integer createdBy;

    protected SOSong(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        name = in.readString();
        path = in.readString();
        singer = in.readParcelable(Singer.class.getClassLoader());
        if (in.readByte() == 0) {
            verified = null;
        } else {
            verified = in.readInt();
        }
        if (in.readByte() == 0) {
            createdBy = null;
        } else {
            createdBy = in.readInt();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(name);
        dest.writeString(path);
        dest.writeParcelable(singer, flags);
        if (verified == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(verified);
        }
        if (createdBy == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(createdBy);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SOSong> CREATOR = new Creator<SOSong>() {
        @Override
        public SOSong createFromParcel(Parcel in) {
            return new SOSong(in);
        }

        @Override
        public SOSong[] newArray(int size) {
            return new SOSong[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Object getLyricPath() {
        return lyricPath;
    }

    public void setLyricPath(Object lyricPath) {
        this.lyricPath = lyricPath;
    }

    public Singer getSinger() {
        return singer;
    }

    public void setSinger(Singer singer) {
        this.singer = singer;
    }

    public Integer getVerified() {
        return verified;
    }

    public void setVerified(Integer verified) {
        this.verified = verified;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }
}