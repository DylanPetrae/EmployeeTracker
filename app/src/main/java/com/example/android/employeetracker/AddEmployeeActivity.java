package com.example.android.employeetracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class AddEmployeeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_add);
    }

    //lol okay
    // Add Employee to database, using the data in the fields
    // public void addEmployee ()

    public void gotoControlPanel ( View view ){

        Intent i = new Intent(this, AdminMainActivity.class);
        startActivity(i);
    }
}