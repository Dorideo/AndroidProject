package com.example.project.Calendar;

import com.example.project.DataBaseController.DatabaseController;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

public class Schedule {
    private ArrayList<Day> days;
    private DatabaseController db;

    public Schedule(String grade) throws SQLException {
        days = new ArrayList<>();
        db = DatabaseController.getInstance();
        try {

            db.createTable(grade, "Schedule", new String[]{"name", "date", "lessons"},
                    new String[]{"INT(255)", "VARCHAR(255)", "VARCHAR(50000)"});
        } catch (SQLException e) {
            e.printStackTrace();
            ResultSet res = db.getDays();

            while(res.next()){
                Day day = new Day(res.getInt(1), res.getString(2));
                String j = res.getString(3);
                String json[] = j.split(" ");



                for(int i = 0; i < json.length-1; i+=3){

                    day.addWithoutChecking(new Lesson(json[i], Timestamp.valueOf(day.getDate() + " " +json[i+1] + ":00.00"), Integer.parseInt(json[i+2])));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private  ArrayList<String> getAllSubjects(){
        ArrayList<String> arr = new ArrayList<>();

        for(int i  = 0; i < days.size(); i++){

            for(int j = 0; j < days.get(i).getLesson().size(); j++){

                boolean b = true;

                for(int k = 0 ; k < arr.size(); k++){

                    if(arr.get(k).equals(days.get(i).getLesson(j).getSubject())){
                        b = false;
                        break;
                    }

                }

                if(b){
                    arr.add(days.get(i).getLesson(j).getSubject());
                }
            }
        }

        return arr;
    }

    private Day findDay(int num){
        for(int i = 0 ; i < days.size(); i++){
            if(days.get(i).getNum() == num){
                return days.get(i);
            }
        }
        return null;
    }

    private int getDay(int num){
        for(int i = 0 ; i < days.size(); i++){
            if(days.get(i).getNum() == num){
                return i;
            }
        }
        return -1;
    }

    public void compile(String grade) throws SQLException {
        LocalDate date = LocalDate.now();
        int currentDay = Calendar.getDayOfAWeek(1, 9, date.getYear()), currentYear = date.getYear();
        int currentMonth;
        boolean b;

        for(int num = 0; num < 7; num++){
            b = true;
            for(Day day : days){
                if(day.getNum() == num){
                    b = false;
                }
            }
            if (b) {
                days.add(new Day(num, "2022-01-01"));
            }
        }

        ArrayList<String> subjects = getAllSubjects();

        for(String subject : subjects){
            try {
                db.addSubject(subject, grade);
            }
            catch(Exception e){

            }
        }

        for(currentMonth = 8;  currentMonth != 6; currentMonth++){
            if (currentMonth == 1 && Calendar.isALeapYear(date.getYear() + 1)) {
                Calendar.daysInMonths[1]++;
            }
            for(int i = 1; i <= Calendar.daysInMonths[currentMonth];i++, currentDay++) {

                if (currentDay == 7)
                    currentDay = 0;

                int index = getDay(currentDay);

                days.get(index).setDate(String.format("%s-%s-%s", currentYear, currentMonth+1, i));

                db.addDay(days.get(index));

            }

            if (currentMonth == 1 && Calendar.isALeapYear(date.getYear() + 1)) {
                Calendar.daysInMonths[1]++;
            }

            if(currentMonth == 11){
                currentMonth = 0;
                currentYear++;
            }
        }

    }

    public void addEntry(String date, Lesson lesson) throws SQLException, IOException {
        for(Day day : days){
            if(day.getDate().equals(date)){
                if(day.addLesson(lesson)){
                    db.update(db.getCurrentGrade(), "SCHEDULE", "date", "'" + date + "'",
                            "lessons", day.lessonsToString());
                }
            }
        }
    }

    public boolean addDay(Day day) throws SQLException {
        for (Day d: days) {
            if(day.getNum() == d.getNum()){
                   return false;
            }
        }
        days.add(day);
        return true;
    }


    /*TODO ADDS A DAY THAT DOESNT FOLLOW NORMAL SCHEDULE
    public void addSpecialOccasion(Day day){
        for (Day d: days) {
            if(d.getDate().equals(day.getDate())){

            }
        }
    }*/


    private void sort(){
        Collections.sort(days);
    }
}
