package com.example.filmesfamosos.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by diegocotta on 08/10/2018.
 */
@Entity(tableName = "review")
public class Review implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int idReview;

    @SerializedName("id")
    private String externalId;

    @SerializedName("author")
    private String name;

    @SerializedName("content")
    private String content;

    private int idMovie;

    public Review(String externalId, String name, String content, int idMovie) {
        this.externalId = externalId;
        this.name = name;
        this.content = content;
        this.idMovie = idMovie;
    }

    protected Review(Parcel in) {
        idReview = in.readInt();
        externalId = in.readString();
        name = in.readString();
        content = in.readString();
        idMovie = in.readInt();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIdReview() {
        return idReview;
    }

    public void setIdReview(int idReview) {
        this.idReview = idReview;
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
        dest.writeInt(idReview);
        dest.writeString(externalId);
        dest.writeString(name);
        dest.writeString(content);
        dest.writeInt(idMovie);
    }


    public int getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(int idMovie) {
        this.idMovie = idMovie;
    }
}
