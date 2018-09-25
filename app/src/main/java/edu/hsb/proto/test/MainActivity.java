package edu.hsb.proto.test;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class MainActivity extends AppCompatActivity {

    @Inject
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: Fragmente
        // TODO: Navigation
        // TODO: UI Map, Login, Prefs
        // TODO: Permission Handling
        // TODO: Bound Service for Location updates
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AndroidInjection.inject(this);
    }
}