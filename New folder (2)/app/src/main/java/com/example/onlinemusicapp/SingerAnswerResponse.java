package com.example.onlinemusicapp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SingerAnswerResponse  implements Serializable, Parcelable {
    @SerializedName("id")
    @Expose
    private int _id;

    @SerializedName("name")
    @Expose
    private String _name;

    @SerializedName("description")
    @Expose
    private String _description;

    protected SingerAnswerResponse(Parcel in) {
        _id = in.readInt();
        _name = in.readString();
        _description = in.readString();
    }

    public static final Creator<SingerAnswerResponse> CREATOR = new Creator<SingerAnswerResponse>() {
        @Override
        public SingerAnswerResponse createFromParcel(Parcel in) {
            return new SingerAnswerResponse(in);
        }

        @Override
        public SingerAnswerResponse[] newArray(int size) {
            return new SingerAnswerResponse[size];
        }
    };

    public void set_id(int id) {this._id = id;}
    public int get_id() {return this._id;}

    public void set_name(String name) {this._name = name;}
    public String get_name() {return this._name;}

    public void set_description(String description) {this._description = description;}
    public String get_description() {return this._description;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeString(_name);
        dest.writeString(_description);
    }
}
