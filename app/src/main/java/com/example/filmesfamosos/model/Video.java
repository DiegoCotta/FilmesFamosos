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
    private int idVideo;

    @SerializedName("id")
    private String externalId;

    @SerializedName("key")
    private String key;

    @SerializedName("name")
    private String name;

    @SerializedName("site")
    private String site;

    private int idMovie;

    public Video( String externalId, String key, String name, String site, int idMovie) {
        this.externalId = externalId;
        this.key = key;
        this.name = name;
        this.site = site;
        this.idMovie = idMovie;
    }

    protected Video(Parcel in) {
        idVideo = in.readInt();
        externalId = in.readString();
        key = in.readString();
        name = in.readString();
        site = in.readString();
        idMovie = in.readInt();
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


    public int getId() {
        return idVideo;
    }

    public void setId(int id) {
        this.idVideo = id;
    }

    public int getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(int idMovie) {
        this.idMovie = idMovie;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idVideo);
        dest.writeString(externalId);
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(site);
        dest.writeInt(idMovie);
    }

    public int getIdVideo() {
        return idVideo;
    }

    public void setIdVideo(int idVideo) {
        this.idVideo = idVideo;
    }
}
