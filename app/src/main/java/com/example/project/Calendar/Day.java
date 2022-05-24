package com.example.project.Calendar;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;



public class Day implements Comparable<Day> {
    private ArrayList<Lesson> lessons;
    private int name;
    private String date;

    public Day(){
        lessons = new ArrayList<>();
        name = 0;
    }
    public Day(int name, String date){
        this.name = name;
        this.date = date;
        lessons = new ArrayList<>();
    }

    public ArrayList<Lesson> getLesson(){
        return lessons;
    }

    public Lesson getLesson(int index){
        if(index > -1 && index < lessons.size()){
            return lessons.get(index);
        }
        throw new IndexOutOfBoundsException("There is no lesson under this index");
    }

    public String getName() {
        return Days.values()[name].toString();
    }

    public int getNum(){
        return name;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean isFree(Lesson lesson){
        int i = 0;

        while(i < lessons.size()-1 && lesson.compareTo(lessons.get(i)) > 0){
            i++;
        }
        if(i == 0){
            return true;
        }
        if(lesson.compareTo(lessons.get(i)) == 0){
            return false;
        }

        i = i>0?i-1:0;

        String[] time1 = lesson.getTime().split(":"),
                time2 = lessons.get(i).getTime().split(":");

        if (Integer.parseInt(time1[1])+Integer.parseInt(time1[0])*60
                >= Integer.parseInt(time2[1]) + Integer.parseInt(time2[0])*60 + lessons.get(i).getDuration()) {
            System.out.println(2);

            if (i >= 0 && i < lessons.size() - 1) {
                System.out.println(3);

                time2 = lessons.get(i+1).getTime().split(":");

                if(Integer.parseInt(time1[1])+ Integer.parseInt(time1[0])*60 + lesson.getDuration()
                        <= Integer.parseInt(time2[1]) + Integer.parseInt(time2[0])*60){

                    return true;

                }

                return false;

            }

            return true;

        }


        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean addLesson(Lesson lesson){
        if(isFree(lesson)) {
            lessons.add(lesson);
            Collections.sort(lessons);
            return true;
        }
        return false;
    }

    public void deleteLesson(int index){
            lessons.remove(index);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    protected void addWithoutChecking(Lesson lesson){
        lessons.add(lesson);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String lessonsToString(){
        StringBuilder stringBuilder = new StringBuilder();
        for (Lesson lesson: lessons) {
            stringBuilder.append(lesson.toString());
        }
        return stringBuilder.toString();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder(name).append(" ").append(date).append(this.lessonsToString());
        return stringBuilder.toString();
    }

    public int compareTo(Day day){
        Timestamp t1 = Timestamp.valueOf(date + " 00:00:00.00"), t2 = Timestamp.valueOf(day.date + "00:00:00.00");

        if(t1.before(t2)){
            return -1;
        }

        else if(t1.equals(t2)){
            return 0;
        }

        return 1;
    }
}
