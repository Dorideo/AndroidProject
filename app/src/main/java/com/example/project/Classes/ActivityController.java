package com.example.project.Classes;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class ActivityController {
    public static Activity current;
    public String path;
    public static ActivityController activityController;

    public ActivityController(Activity activity){
        current = activity;
        activityController = this;
    }

    public static void addOnClickListener(Button b, Class dest){
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(current, dest);
                current.startActivity(intent);
            }
        });
    }

    public void setPath(String str){
        path = str;
    }


}
