package com.example.project;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.Classes.ActivityController;

public class MainActivity extends AppCompatActivity {

    public static MainActivity  activity;

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        ActivityController activityController = new ActivityController(this);
        setContentView(R.layout.activity_main);



    }

}