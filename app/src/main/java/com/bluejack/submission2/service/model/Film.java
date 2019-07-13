package com.bluejack.submission2.service.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Film implements Parcelable {
    private String photo, title, date, desc, back_photo, type, id;
    private double vote;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getBack_photo() {
        return back_photo;
    }

    public void setBack_photo(String back_photo) {
        this.back_photo = back_photo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getVote() {
        return vote;
    }

    public void setVote(double vote) {
        this.vote = vote;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.photo);
        dest.writeString(this.title);
        dest.writeString(this.date);
        dest.writeString(this.desc);
        dest.writeString(this.back_photo);
        dest.writeString(this.type);
        dest.writeString(this.id);
        dest.writeDouble(this.vote);
    }

    public Film() {
    }

    private Film(Parcel in) {
        this.photo = in.readString();
        this.title = in.readString();
        this.date = in.readString();
        this.desc = in.readString();
        this.back_photo = in.readString();
        this.type = in.readString();
        this.id = in.readString();
        this.vote = in.readDouble();
    }

    public static final Creator<Film> CREATOR = new Creator<Film>() {
        @Override
        public Film createFromParcel(Parcel source) {
            return new Film(source);
        }

        @Override
        public Film[] newArray(int size) {
            return new Film[size];
        }
    };
}

