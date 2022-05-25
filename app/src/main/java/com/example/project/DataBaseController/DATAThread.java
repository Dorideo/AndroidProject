package com.example.project.DataBaseController;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.project.Calendar.Day;
import com.example.project.Calendar.Lesson;

import java.sql.SQLException;
import java.util.ArrayList;

public class DATAThread extends Thread{
    DatabaseController db = DatabaseController.getInstance();
    Day day;
    public DATAThread(Day day){
        super();
        this.day = day;
        super.setPriority(MAX_PRIORITY);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void run(){
        try {

            String query = String.format("INSERT INTO SCHEDULE(name, date, lessons) VALUES('%s', '%s', '%s');", day.getNum(), day.getDate(), day.lessonsToString());

            db.database.execSQL(query);

            ArrayList<Lesson> lessons = day.getLesson();

            for (int i = 0; i < lessons.size(); i++) {
                db.addLesson(lessons.get(i), day.getDate());
            }

            db = null;

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
