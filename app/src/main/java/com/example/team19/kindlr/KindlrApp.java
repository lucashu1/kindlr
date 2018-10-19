package com.example.team19.kindlr;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class KindlrApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
