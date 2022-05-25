package com.example.project;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.DataBaseController.DatabaseController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {
    private TableLayout tableLayout;
    private TextView subject, month;
    private Button nextM, nextS, prevM, prevS;
    private ArrayList<String> subjects = new ArrayList<>();
    private int subIndex=0, monthIndex=0;
    private String path;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        DatabaseController db = DatabaseController.getInstance();
        try {

            db.connect("10-C");


            db.addSubject("AndroidLesson", "10-C");


            subjects = db.getSubjects();

        }
        catch(Exception e){
            e.printStackTrace();
        }





        subject = findViewById(R.id.subject);
        month = findViewById(R.id.monthName);
        tableLayout = findViewById(R.id.table);
        nextM = findViewById(R.id.nextMonth);
        nextS = findViewById(R.id.nextSubject);
        prevM = findViewById(R.id.previousMonth);

        nextS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subIndex++;
                if(subIndex == subjects.size()){
                    subIndex = 0;
                }
                subject.setText(subjects.get(subIndex));
            }
        });



        ArrayList<String> arr = new ArrayList<>();
            arr.add("col1");
            arr.add("col2");

        try {
            setTable(db.getDates("10-C", "AndroidLesson", "9"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTable(ArrayList<String> arr){
        TableRow header = new TableRow(this);
        header.setBackgroundColor(Color.GRAY);
        header.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT));



        for(int i = 0 ;i < arr.size(); i++) {
            TextView labelDate = new TextView(this);
            labelDate.setText(arr.get(i));
            labelDate.setTextColor(Color.BLACK);


            labelDate.setPadding(5, 5, 5, 5);
            header.addView(labelDate);// add the column to the table row here
        }

        tableLayout.addView(header, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT));

    }
}