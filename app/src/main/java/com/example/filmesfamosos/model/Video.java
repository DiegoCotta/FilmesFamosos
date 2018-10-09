package com.example.filmesfamosos.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by diegocotta on 08/10/2018.
 */

@Entity(tableName = "video")
public class Video implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("key")
    private String key;

    @SerializedName("name")
    private String name;

    @SerializedName("site")
    private String site;

    private int idMovie;

    public Video(int id, String key, String name, String site, int idMovie) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.site = site;
        this.idMovie = idMovie;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    protected Video(Parcel in) {
        id = in.readInt();
        key = in.readString();
        name = in.readString();
        site = in.readString();
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(site);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(int idMovie) {
        this.idMovie = idMovie;
    }
}
