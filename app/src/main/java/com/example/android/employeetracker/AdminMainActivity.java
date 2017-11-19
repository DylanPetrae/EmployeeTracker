package com.example.android.employeetracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AdminMainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

    }

    public void logoutUser( View view ){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void addEmployee( View view ){
        Intent i = new Intent( this, AddEmployeeActivity.class);
        startActivity(i);
    }

    public void changePassword ( View view ){
        Intent i = new Intent(this, ChangePasswordActivity.class);
        startActivity(i);
    }

    /*
    public void viewEmployeeList ( View view ){
        Intent i = new Intent(this, )
    }
    */
}
