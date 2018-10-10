package com.example.filmesfamosos;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by diegocotta on 10/10/2018.
 */

public class MyApplication extends Application {
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
