package com.example.project.DataBaseController;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.project.Calendar.Day;
import com.example.project.Calendar.Lesson;
import com.example.project.MainActivity;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DatabaseController {
    private static DatabaseController controller = new DatabaseController();
    private ArrayList<String> subjects;
    private Connection connection = null;
    public static String currentGrade = "null";
    public String path;
    SQLiteDatabase db;
        private DatabaseController(){
        subjects = new ArrayList<>();
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public void connect(String grade) throws SQLException, IOException {



            try {
                db = MainActivity.activity.getBaseContext().openOrCreateDatabase(grade + ".db", Context.MODE_PRIVATE, null);
                path = db.getPath();
                String url = "jdbc:sqlite:" + path;

                connection = DriverManager.getConnection(url);
                if (connection != null) {
                    System.out.println("A new database has been created.");


            Statement statement = connection.createStatement();
            String query = "CREATE TABLE STUDENTS(student_id INTEGER PRIMARY KEY AUTOINCREMENT, student_full_name VARCHAR, student_age VARCHAR);";
            statement.execute(query);

            statement.close(); }
            }
            catch (SQLException e) {
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
        for(int i = 1; i < values.length; i++){
            stringBuilder.append(String.format(", %s %s", values[i], types[i]));
        }
        stringBuilder.append(")");

        String query = stringBuilder.toString();

        Statement statement = connection.createStatement();

        statement.execute(query);

        statement.close();
    }

    private void addStudents(String tableName) throws SQLException {
        Statement statement = connection.createStatement();

        ResultSet res = statement.executeQuery("SELECT student_id, student_full_name FROM STUDENTS");
        while(res.next()) {
            String v1 =  res.getString(1), v2 =  res.getString(2);
            statement.execute(String.format("INSERT INTO %s (student_id, student_full_name) VALUES (%s, '%s')", tableName,v1,v2));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addStudent(String grade, Student student) throws SQLException, IOException {
        if(!grade.equals(currentGrade)){
            connect(grade);
        }
        String query  = "INSERT INTO STUDENTS(student_full_name, student_age) " +
                "VALUES(?, ?);";


        PreparedStatement statement = connection.prepareStatement(query);
        statement.setObject(1, student.getFullName());
        statement.setObject(2, student.getAge());
        statement.execute();

        Statement statement1 = connection.createStatement();
        for(String subject : subjects){
            statement1.execute(String.format("INSERT INTO %s (student_full_name) VALUES ( '%s' )", subject,
                    student.getFullName()));
        }

        statement.close();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void update(String grade, String table, String where, String value, String whatToChange, String newValue) throws SQLException, IOException {
        if(!grade.equals(currentGrade)){
            connect(grade);
        }
        String query = String.format("UPDATE %s SET %s = '%s' WHERE %s = %s", table, whatToChange,newValue, where, value);
        Statement statement = connection.createStatement();
        statement.execute(query);

        statement.close();
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
        Statement statement = connection.createStatement();
        try {
            statement.execute(query);

            statement.close();
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
           Statement statement = connection.createStatement();

           statement.execute(query);

           statement.close();
    }

    public ResultSet getDays() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet res  = statement.executeQuery("SELECT * FROM SCHEDULE");
        return res;
    }

    public void addLesson(Lesson lesson, String date) throws SQLException {
        createColumn(lesson.getSubject(), date, "INT(255)");
    }

    public Statement getStatement() throws SQLException {
        return connection.createStatement();
    }
    public PreparedStatement getPreparedStatement(String query) throws SQLException {
        return connection.prepareStatement(query);
    }

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
        Statement statement = connection.createStatement();
        statement.execute(query);
        statement.close();
    }

    public static String getCurrentGrade() {
        return currentGrade;
    }

    public static DatabaseController getInstance(){
        return controller;
    }

    public ResultSet Table(String tableName) throws SQLException {
        Statement statement = connection.createStatement();

        ResultSet res = statement.executeQuery(String.format("SELECT * FROM %s", tableName));
        return res;
    }
    public ArrayList<String> getSubjects() throws SQLException {
        ArrayList<String> str = new ArrayList<>();

        System.out.println();

        ResultSet res = connection.getMetaData().getTables(null, null,
                null, null);

        while(res.next()){
            String s = res.getString("TABLE_NAME");
            if(!res.getString("TABLE_NAME").equals("STUDENTS") && !res.getString("TABLE_NAME").equals("Schedule")) {
                str.add(res.getString("TABLE_NAME"));
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
        String sql = "select * from " + subject + " LIMIT 0";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        ResultSetMetaData mrs = rs.getMetaData();
        for(int i = 1; i <= mrs.getColumnCount(); i++) {
            if(!mrs.getColumnLabel(i).equals("student_id") && !mrs.getColumnLabel(i).equals("student_full_name") && mrs.getColumnLabel(i).split("-")[1].equals(month))
                columns.add(mrs.getColumnLabel(i));
        }
        return columns;
    }
}
