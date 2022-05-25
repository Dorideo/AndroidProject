package com.example.project.DataBaseController;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.project.Calendar.Day;
import com.example.project.Calendar.Lesson;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseController{
    private static DatabaseController controller = new DatabaseController();
    private ArrayList<String> subjects;
    private Connection connection = null;
    public static String currentGrade = "null";
    public String path = "/data/data/com.example.project/databases/";

    private DatabaseController(){
        subjects = new ArrayList<>();
    }
    public SQLiteDatabase database;



    @RequiresApi(api = Build.VERSION_CODES.O)
    public void connect(String grade) throws SQLException, IOException {

            try {
                database = SQLiteDatabase.openOrCreateDatabase(path + grade + ".db", null);
                try {
                    String query = "CREATE TABLE STUDENTS(student_id INTEGER PRIMARY KEY AUTOINCREMENT, student_full_name VARCHAR, student_age VARCHAR);";
                    database.execSQL(query);
                } catch (Exception e) {
                    System.out.println("Table has already been created");
                }
            }

            catch (Exception e) {
                System.out.println(e);
            }

            currentGrade = grade;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createTable(String grade, String tableName, String[] values, String[] types) throws SQLException, IOException {
        if(!currentGrade.equals( grade)){
            this.connect(grade);

        }

            StringBuilder stringBuilder = new StringBuilder("CREATE TABLE ").append(tableName).
                    append(String.format(" (%s %s", values[0], types[0]));
            for (int i = 1; i < values.length; i++) {
                stringBuilder.append(String.format(", %s %s", values[i], types[i]));
            }
            stringBuilder.append(");");

            String query = stringBuilder.toString();
            try {
                database.execSQL(query);
            }
            catch  (Exception e){
                System.out.println("Table has already been created");
            }
    }

    private void addStudents(String tableName) throws SQLException {
        Cursor cursor = database.rawQuery("SELECT student_id, student_full_name FROM STUDENTS", null);
        if(cursor.moveToFirst()) {
            do {
                String v1 = "" + cursor.getInt(0), v2 = cursor.getString(1);
                database.execSQL(String.format("INSERT INTO %s (student_id, student_full_name) VALUES (%s, '%s')", tableName, v1, v2));
                cursor.moveToNext();
            }
            while (cursor.isAfterLast());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addStudent(String grade, Student student) throws SQLException, IOException {
        if(!grade.equals(currentGrade)){
            connect(grade);
        }
        String query  = String.format("INSERT INTO STUDENTS(student_full_name, student_age) VALUES('%s', '%s');"
                , student.getFullName(), student.getAge());


        database.execSQL(query);

        for(String subject : subjects){
            database.execSQL(String.format("INSERT INTO %s (student_full_name) VALUES ( '%s' )", subject,
                    student.getFullName()));
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void update(String grade, String table, String where, String value, String whatToChange, String newValue) throws SQLException, IOException {
        if(!grade.equals(currentGrade)){
            connect(grade);
        }
        String query = String.format("UPDATE %s SET %s = '%s' WHERE %s = %s", table, whatToChange,newValue, where, value);
        database.execSQL(query);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addSubject(String subjectName, String grade) throws SQLException, IOException {
        if(!grade.equals(currentGrade)){
            connect(grade);
        }
        String[] values = {"student_id","student_full_name", "FOREIGN KEY(student_id) REFERENCES STUDENTS (student_id)"};
        String[] types = {"INTEGER","VARCHAR", ""};
        subjects.add(subjectName);
        createTable(grade, subjectName, values, types);
        addStudents(subjectName);
    }

    private void createColumn(String table_name, String column_name, String data_type) throws SQLException {
        String query = String.format("ALTER TABLE %s ADD COLUMN '%s' %s", table_name, column_name, data_type);
        //if the column was created it will throw an exception
        try {
            database.execSQL(query);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addRecord(String grade, String table_name, String date, int id, int record)
            throws SQLException, IOException {
           if(!grade.equals(currentGrade)){
                connect(grade);
           }
           createColumn(table_name, date, "INT(255)");

           String query = String.format("UPDATE %s SET %s = %d WHERE student_id = %d", table_name,date,record,id);

           database.execSQL(query);
    }

    public Cursor getDays() throws SQLException {
        Cursor res  = database.rawQuery("SELECT * FROM SCHEDULE", null);
        return res;
    }

    public void addLesson(Lesson lesson, String date) throws SQLException {
        createColumn(lesson.getSubject(), date, "INT(255)");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addDay(Day day){

        DATAThread dataThread = new DATAThread(day);
        dataThread.run();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void delete(String grade, String table, String where, String value) throws SQLException, IOException {
        if(!grade.equals(currentGrade)){
            connect(grade);
        }
        String query = String.format("DELETE FROM %s WHERE %s = %s", table, where, value);
        database.execSQL(query);
    }

    public static String getCurrentGrade() {
        return currentGrade;
    }

    public static DatabaseController getInstance(){
        return controller;
    }

    public Cursor Table(String tableName) throws SQLException {
        Cursor res = database.rawQuery(String.format("SELECT * FROM %s", tableName), null);
        return res;
    }
    public ArrayList<String> getSubjects() throws SQLException {
        ArrayList<String> str = new ArrayList<>();

        System.out.println();

        Cursor c = database.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                if(!c.getString(0).equals("STUDENTS") &&
                        !c.getString(0).equals("android_metadata") &&
                        !c.getString(0).equals("sqlite_sequence")){

                    str.add(c.getString(0));
                }
                c.moveToNext();
            }
        }


        return str;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<String> getDates(String grade, String subject, String month) throws SQLException, IOException {
        if(!currentGrade.equals(grade)) {
            connect(grade);
        }
        ArrayList<String> columns = new ArrayList<>();
        Cursor dbCursor = database.query(subject, null, null, null, null, null, null);
        String[] columnNames = dbCursor.getColumnNames();

        for(String date : columnNames){
            if(date.split("-")[1] == month){
                columns.add(date);
            }
        }

        return columns;
    }
}
