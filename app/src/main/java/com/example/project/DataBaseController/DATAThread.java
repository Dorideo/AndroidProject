package com.example.project.DataBaseController;

import com.example.project.Calendar.Day;
import com.example.project.Calendar.Lesson;

import java.sql.PreparedStatement;
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

    public void run(){
        try {

            String query = "INSERT INTO SCHEDULE(name, date, lessons) VALUES(?, ?, ?);";

            PreparedStatement preparedStatement = db.getPreparedStatement(query);

            preparedStatement.setObject(1, day.getNum());
            preparedStatement.setObject(2, day.getDate());
            preparedStatement.setObject(3, day.lessonsToString());
            preparedStatement.execute();

            preparedStatement.close();

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
