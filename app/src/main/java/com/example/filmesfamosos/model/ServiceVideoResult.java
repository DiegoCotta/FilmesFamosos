package com.example.filmesfamosos.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by diegocotta on 10/10/2018.
 */

public class ServiceVideoResult implements Parcelable {

    @SerializedName("id")
    private int id;

    @SerializedName("results")
    private List<Video> videos;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Video> getMovies() {
        return videos;
    }

    public void setMovies(List<Video> videos) {
        this.videos = videos;
    }

    private ServiceVideoResult(Parcel in) {
        id = in.readInt();
        videos = in.createTypedArrayList(Video.CREATOR);
    }

    public static final Creator<ServiceVideoResult> CREATOR = new Creator<ServiceVideoResult>() {
        @Override
        public ServiceVideoResult createFromParcel(Parcel in) {
            return new ServiceVideoResult(in);
        }

        @Override
        public ServiceVideoResult[] newArray(int size) {
            return new ServiceVideoResult[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeTypedList(videos);
    }
}
