package com.stp.music_player.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SongPath implements Parcelable {
    private String path;
    private int id;

    public SongPath(String path, int id) {
        this.id = id;
        this.path = path;
    }

    protected SongPath(Parcel in) {
        super();
        path = in.readString();
        id = in.readInt();
    }


    public static final Creator<SongPath> CREATOR = new Creator<SongPath>() {
        @Override
        public SongPath createFromParcel(Parcel in) {
            return new SongPath(in);
        }

        @Override
        public SongPath[] newArray(int size) {
            return new SongPath[size];
        }
    };

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void readFromParcel(Parcel in) {
        id = in.readInt();
        path = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(path);
    }
}
