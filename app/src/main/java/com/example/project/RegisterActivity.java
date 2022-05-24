package com.example.project;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.DataBaseController.DatabaseController;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    private TableLayout tableLayout;
    private TextView subject, month;
    private Button nextM, nextS, prevM, prevS;
    private ArrayList<String> subjects = new ArrayList<>();
    private int subIndex=0, monthIndex=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        DatabaseController db = DatabaseController.getInstance();
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    db.connect("10-B");

                }
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
            prevS = findViewById(R.id.previousSubject);

            nextS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    subIndex++;
                    if(subIndex == subjects.size()){
                        subIndex = 0;
                    }

                }
            });



        ArrayList<String> arr = new ArrayList<>();
            arr.add("col1");
            arr.add("col2");

            setTable(arr);
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