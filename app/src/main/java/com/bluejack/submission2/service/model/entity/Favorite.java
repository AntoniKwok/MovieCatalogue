package com.bluejack.submission2.service.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Favorite extends RealmObject implements Parcelable {

    @PrimaryKey
    private String uid;
    private String title, desc, releaseDate, cover_photo, backdrop_photo, type;
    private double vote;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getCover_photo() {
        return cover_photo;
    }

    public void setCover_photo(String cover_photo) {
        this.cover_photo = cover_photo;
    }

    public String getBackdrop_photo() {
        return backdrop_photo;
    }

    public void setBackdrop_photo(String backdrop_photo) {
        this.backdrop_photo = backdrop_photo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getVote() {
        return vote;
    }

    public void setVote(double vote) {
        this.vote = vote;
    }

    public Favorite(String uid, String title, String desc, String releaseDate, String cover_photo, String backdrop_photo, String type, double vote) {
        this.uid = uid;
        this.title = title;
        this.desc = desc;
        this.releaseDate = releaseDate;
        this.cover_photo = cover_photo;
        this.backdrop_photo = backdrop_photo;
        this.type = type;
        this.vote = vote;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uid);
        dest.writeString(this.title);
        dest.writeString(this.desc);
        dest.writeString(this.releaseDate);
        dest.writeString(this.cover_photo);
        dest.writeString(this.backdrop_photo);
        dest.writeString(this.type);
        dest.writeDouble(this.vote);
    }

    public Favorite() {
    }

    private Favorite(Parcel in) {
        this.uid = in.readString();
        this.title = in.readString();
        this.desc = in.readString();
        this.releaseDate = in.readString();
        this.cover_photo = in.readString();
        this.backdrop_photo = in.readString();
        this.type = in.readString();
        this.vote = in.readDouble();
    }

    public static final Parcelable.Creator<Favorite> CREATOR = new Parcelable.Creator<Favorite>() {
        @Override
        public Favorite createFromParcel(Parcel source) {
            return new Favorite(source);
        }

        @Override
        public Favorite[] newArray(int size) {
            return new Favorite[size];
        }
    };
}
