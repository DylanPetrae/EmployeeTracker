package com.example.android.employeetracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void loginUser( View view ){

        // We should make a conditional block here to determine whether the inputted
        // login information is for an employee or admin account

        // If it is an admin's account, start the Admin Main Activity (control panel)
        Intent i = new Intent(this, AdminMainActivity.class);
        startActivity(i);

        // If it is an employee's account, start the Employee Main Activity
        // Intent i = new Intent( packageContext: this, AdminMainActivity.class);
        // startActivity(i);
    }

}
