package com.gh.sammie.busapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Bus implements Parcelable {

    private String name,password,username,barberId;
    private long rating;

    public Bus() {
    }

    protected Bus(Parcel in) {
        name = in.readString();
        password = in.readString();
        username = in.readString();
        barberId = in.readString();
        rating = in.readLong();
    }

    public static final Creator<Bus> CREATOR = new Creator<Bus>() {
        @Override
        public Bus createFromParcel(Parcel in) {
            return new Bus(in);
        }

        @Override
        public Bus[] newArray(int size) {
            return new Bus[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    public String getBarberId() {
        return barberId;
    }

    public void setBarberId(String barberId) {
        this.barberId = barberId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(password);
        dest.writeString(username);
        dest.writeString(barberId);
        dest.writeLong(rating);
    }
}
