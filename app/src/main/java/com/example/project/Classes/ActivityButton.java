package com.example.project.Classes;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class ActivityButton {
    private Button button;
    private Class activity;
    private Activity current;


    public ActivityButton(Button button, Class activity, final Activity current) {
        this.button = button;
        this.activity = activity;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(current);
            }
        });
    }

    public ActivityButton(ActivityButton activityButton){
        button = activityButton.button;
        activity = activityButton.activity;
    }

    private void openActivity(Activity current){
        Intent intent  = new Intent(current, activity);
        current.startActivity(intent);
    }

    public void setCurrent(Activity current){
        this.current = current;
    }
}
