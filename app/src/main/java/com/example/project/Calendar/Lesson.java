package com.example.project.Calendar;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Lesson implements Comparable<Lesson>{
    private String subject;
    private Timestamp timeStamp;
    private int duration;


    public Lesson(String subject, Timestamp timeStamp, int duration) {
        this.subject = subject;
        this.timeStamp = timeStamp;
        this.duration = duration;
    }

    public Lesson() {
        subject = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            timeStamp = Timestamp.valueOf(String.valueOf(LocalDateTime.now()));
        }
        duration = 45;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String format = formatter.format(timeStamp.toInstant());
        return format;
    }
    public Timestamp getTimeStamp(){
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int compareTo(Lesson lesson){
        if(timeStamp.before(lesson.timeStamp)){
            return -1;
        }
        else if(timeStamp.equals(lesson.timeStamp)){
            return 0;
        }
        return 1;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder(subject);
        stringBuilder.append(" ").append(this.getTime()).append(" ").append(duration).append(" ");
        return  stringBuilder.toString();
    }
}
